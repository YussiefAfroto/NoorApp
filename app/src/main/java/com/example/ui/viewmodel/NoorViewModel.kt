package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.AthkarProgressEntity
import com.example.data.local.QuranBookmarkEntity
import com.example.data.local.TasbihEntity
import com.example.data.model.AthkarCategory
import com.example.data.model.Ayah
import com.example.data.model.CityPrayerPreset
import com.example.data.model.PrayerTime
import com.example.data.model.Surah
import com.example.data.repository.IslamicData
import com.example.data.repository.NoorRepository
import com.example.util.AdhanNotificationHelper
import com.example.util.LocationHelper
import com.example.util.PrayerTimeCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

enum class NoorNavTab {
    HOME,
    QURAN,
    PRAYER_TIMES,
    QIBLA,
    ATHKAR,
    HADITH,
    SETTINGS,
    TASBIH
}

data class NoorUiState(
    val selectedLanguage: String = "ar", // "ar" or "en"
    val selectedCity: CityPrayerPreset = IslamicData.cities[0],
    val currentTab: NoorNavTab = NoorNavTab.HOME,
    val previousTab: NoorNavTab = NoorNavTab.HOME,
    val prayerTimes: List<PrayerTime> = emptyList(),
    val nextPrayerCountdown: String = "00:00:00",
    val nextPrayerName: String = "",
    val completedPrayersCount: Int = 0,
    val totalObligatoryPrayers: Int = 5,
    val activeTasbihId: String = "subhanallah",
    val hapticEnabled: Boolean = true,
    val selectedAthkarCategory: AthkarCategory = AthkarCategory.MORNING,
    val selectedSurahForReading: Surah? = null,
    val quranSearchQuery: String = "",
    val compassDegrees: Float = 0f,
    val isCityPickerVisible: Boolean = false,
    val isBookmarkSheetVisible: Boolean = false,
    val isNotificationsDialogVisible: Boolean = false,
    val isTasbihSheetVisible: Boolean = false,
    val quranFontSize: Float = 24f,
    val adhanReminderEnabled: Boolean = true,
    val preAdhanReminderEnabled: Boolean = false,
    val iqamahReminderEnabled: Boolean = false,
    val morningAthkarReminder: Boolean = true,
    val eveningAthkarReminder: Boolean = true,
    val fridayKahfReminder: Boolean = true,
    val qiyamReminder: Boolean = false,
    val selectedAdhanVoice: String = "أذان الحرم المكي الشريف",
    val selectedHadithCategory: String = "الكل",
    val hadithSearchQuery: String = "",
    val isVisualThemeDark: Boolean = false,
    // Dynamic GPS & AlAdhan State
    val userLatitude: Double? = null,
    val userLongitude: Double? = null,
    val userLocationDisplayName: String? = null,
    val isGpsLocationActive: Boolean = false,
    val selectedCalculationMethodId: Int = 5, // Default Method 5 EGAS
    val calculationMethodTitle: String = "الهيئة المصرية العامة للمساحة",
    val imsakTime: String = "",
    val lastThirdTime: String = "",
    val hijriDateFormatted: String = "",
    val isOfflineFallback: Boolean = false,
    val isMethodPickerVisible: Boolean = false,
    val selectedDate: LocalDate = LocalDate.now(),
    // Quran Surah Bookmarks, Favorites & Reading Progress
    val bookmarkedSurahNumbers: Set<Int> = setOf(18),
    val favoriteSurahNumbers: Set<Int> = setOf(1, 55),
    val lastReadSurahNumber: Int = 18,
    val lastReadAyahNumber: Int = 46,
    val lastReadTimestamp: Long = System.currentTimeMillis() - 2 * 3600 * 1000L
)

class NoorViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = NoorRepository(database.noorDao())
    private val prefs = application.getSharedPreferences("noor_settings", android.content.Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(NoorUiState())
    val uiState: StateFlow<NoorUiState> = _uiState.asStateFlow()

    // Room Flows
    val tasbihList: StateFlow<List<TasbihEntity>> = repository.allTasbih
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarks: StateFlow<List<QuranBookmarkEntity>> = repository.allBookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _completedPrayerKeys = MutableStateFlow<Set<String>>(emptySet())
    val completedPrayerKeys: StateFlow<Set<String>> = _completedPrayerKeys.asStateFlow()

    private val _athkarProgressMap = MutableStateFlow<Map<String, AthkarProgressEntity>>(emptyMap())
    val athkarProgressMap: StateFlow<Map<String, AthkarProgressEntity>> = _athkarProgressMap.asStateFlow()

    init {
        val savedAdhan = prefs.getBoolean("adhan_reminder_enabled", true)
        val savedPreAdhan = prefs.getBoolean("pre_adhan_reminder_enabled", false)
        val savedIqamah = prefs.getBoolean("iqamah_reminder_enabled", false)
        val savedMorning = prefs.getBoolean("morning_athkar_reminder", true)
        val savedEvening = prefs.getBoolean("evening_athkar_reminder", true)
        val savedFriday = prefs.getBoolean("friday_kahf_reminder", true)
        val savedQiyam = prefs.getBoolean("qiyam_reminder", false)
        val savedHaptic = prefs.getBoolean("haptic_enabled", true)

        val savedBookmarkedSurahs = prefs.getStringSet("bookmarked_surahs", setOf("18"))?.mapNotNull { it.toIntOrNull() }?.toSet() ?: setOf(18)
        val savedFavoriteSurahs = prefs.getStringSet("favorite_surahs", setOf("1", "55"))?.mapNotNull { it.toIntOrNull() }?.toSet() ?: setOf(1, 55)
        val savedLastReadSurah = prefs.getInt("last_read_surah", 18)
        val savedLastReadAyah = prefs.getInt("last_read_ayah", 46)
        val savedLastReadTimestamp = prefs.getLong("last_read_timestamp", System.currentTimeMillis() - 2 * 3600 * 1000L)

        _uiState.value = _uiState.value.copy(
            adhanReminderEnabled = savedAdhan,
            preAdhanReminderEnabled = savedPreAdhan,
            iqamahReminderEnabled = savedIqamah,
            morningAthkarReminder = savedMorning,
            eveningAthkarReminder = savedEvening,
            fridayKahfReminder = savedFriday,
            qiyamReminder = savedQiyam,
            hapticEnabled = savedHaptic,
            bookmarkedSurahNumbers = savedBookmarkedSurahs,
            favoriteSurahNumbers = savedFavoriteSurahs,
            lastReadSurahNumber = savedLastReadSurah,
            lastReadAyahNumber = savedLastReadAyah,
            lastReadTimestamp = savedLastReadTimestamp
        )

        viewModelScope.launch {
            repository.initializeDefaultTasbihIfEmpty()
        }

        // Observe prayer logs for today
        viewModelScope.launch {
            repository.getPrayerLogs(repository.getTodayDateString()).collect { logs ->
                val completed = logs.filter { it.isCompleted }.map { it.prayerKey }.toSet()
                _completedPrayerKeys.value = completed
                refreshPrayerTimes(date = _uiState.value.selectedDate, forceNetwork = false)
            }
        }

        // Observe Athkar progress for today
        viewModelScope.launch {
            repository.getAthkarProgress(repository.getTodayDateString()).collect { progressList ->
                _athkarProgressMap.value = progressList.associateBy { it.athkarId }
            }
        }

        // Fetch initial dynamic prayer times
        refreshPrayerTimes(date = LocalDate.now(), forceNetwork = false)

        // Pre-warm offline Quran dataset in background
        viewModelScope.launch(Dispatchers.IO) {
            com.example.data.repository.QuranOfflineDataSource.getSurahs(application)
        }

        // Real-time ticking countdown to next prayer
        startPrayerCountdownTicker()
    }

    private fun startPrayerCountdownTicker() {
        viewModelScope.launch {
            while (true) {
                updatePrayerCountdown()
                delay(1000L)
            }
        }
    }

    private fun updatePrayerCountdown() {
        val prayers = _uiState.value.prayerTimes
        val nextPrayer = prayers.firstOrNull { it.isNext }

        val countdownText = if (nextPrayer != null) {
            val now = Calendar.getInstance()
            val prayerTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, nextPrayer.hour24)
                set(Calendar.MINUTE, nextPrayer.minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                if (before(now)) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }
            val diffMillis = prayerTime.timeInMillis - now.timeInMillis
            val diffSeconds = (diffMillis / 1000).coerceAtLeast(0)
            val h = diffSeconds / 3600
            val m = (diffSeconds % 3600) / 60
            val s = diffSeconds % 60
            String.format(Locale.US, "%02d:%02d:%02d", h, m, s)
        } else {
            "00:00:00"
        }

        _uiState.value = _uiState.value.copy(
            nextPrayerCountdown = countdownText,
            nextPrayerName = if (_uiState.value.selectedLanguage == "ar") nextPrayer?.nameArabic ?: "" else nextPrayer?.nameEnglish ?: ""
        )
    }

    fun refreshPrayerTimes(date: LocalDate = _uiState.value.selectedDate, forceNetwork: Boolean = false) {
        viewModelScope.launch {
            val state = _uiState.value
            val lat = if (state.isGpsLocationActive && state.userLatitude != null) {
                state.userLatitude
            } else {
                state.selectedCity.latitude
            }
            val lng = if (state.isGpsLocationActive && state.userLongitude != null) {
                state.userLongitude
            } else {
                state.selectedCity.longitude
            }

            val result = repository.fetchPrayerTimes(
                latitude = lat,
                longitude = lng,
                method = state.selectedCalculationMethodId,
                completedPrayerKeys = _completedPrayerKeys.value,
                date = date,
                forceRefresh = forceNetwork
            )

            val nextPrayer = result.prayerTimes.firstOrNull { it.isNext }
            val completedCount = result.prayerTimes.count { it.isCompleted && it.key != "sunrise" }

            _uiState.value = _uiState.value.copy(
                prayerTimes = result.prayerTimes,
                imsakTime = result.imsak,
                lastThirdTime = result.lastThird,
                hijriDateFormatted = result.hijriFormatted,
                isOfflineFallback = result.isOfflineFallback,
                calculationMethodTitle = result.calculationMethodTitle,
                nextPrayerName = if (_uiState.value.selectedLanguage == "ar") nextPrayer?.nameArabic ?: "" else nextPrayer?.nameEnglish ?: "",
                completedPrayersCount = completedCount,
                selectedDate = date
            )
            updatePrayerCountdown()

            if (_uiState.value.adhanReminderEnabled && result.prayerTimes.isNotEmpty()) {
                AdhanNotificationHelper.scheduleAllPrayerAdhans(getApplication(), result.prayerTimes)
            }
        }
    }

    fun recalculatePrayers() {
        refreshPrayerTimes(date = _uiState.value.selectedDate, forceNetwork = false)
    }

    fun updateGpsLocation(location: LocationHelper.DeviceLocation) {
        val currentMethod = if (location.isEgypt && _uiState.value.selectedCalculationMethodId != 5) {
            5 // Default to EGAS for Egypt
        } else {
            _uiState.value.selectedCalculationMethodId
        }
        val methodTitle = PrayerTimeCalculator.CalculationMethod.fromId(currentMethod).titleArabic

        val dispName = if (_uiState.value.selectedLanguage == "ar") {
            location.cityNameArabic ?: location.cityNameEnglish ?: "موقعي الحالي"
        } else {
            location.cityNameEnglish ?: location.cityNameArabic ?: "Current Location"
        }

        val (dynamicQibla, dynamicDist) = com.example.util.QiblaCalculator.calculateQibla(location.latitude, location.longitude)
        val dynamicCity = _uiState.value.selectedCity.copy(
            cityNameArabic = location.cityNameArabic ?: _uiState.value.selectedCity.cityNameArabic,
            cityNameEnglish = location.cityNameEnglish ?: _uiState.value.selectedCity.cityNameEnglish,
            country = if (location.isEgypt) "مصر" else _uiState.value.selectedCity.country,
            latitude = location.latitude,
            longitude = location.longitude,
            qiblaDegree = dynamicQibla,
            distanceToKaabaKm = dynamicDist
        )

        _uiState.value = _uiState.value.copy(
            selectedCity = dynamicCity,
            userLatitude = location.latitude,
            userLongitude = location.longitude,
            userLocationDisplayName = dispName,
            isGpsLocationActive = true,
            selectedCalculationMethodId = currentMethod,
            calculationMethodTitle = methodTitle
        )
        refreshPrayerTimes(date = _uiState.value.selectedDate, forceNetwork = false)
    }

    fun selectCalculationMethod(methodId: Int) {
        val calcMethod = PrayerTimeCalculator.CalculationMethod.fromId(methodId)
        _uiState.value = _uiState.value.copy(
            selectedCalculationMethodId = methodId,
            calculationMethodTitle = calcMethod.titleArabic,
            isMethodPickerVisible = false
        )
        refreshPrayerTimes(date = _uiState.value.selectedDate, forceNetwork = true)
    }

    fun showMethodPicker(show: Boolean) {
        _uiState.value = _uiState.value.copy(isMethodPickerVisible = show)
    }

    fun selectCity(city: CityPrayerPreset) {
        val inEgypt = city.country == "Egypt" || city.cityNameEnglish == "Cairo" || LocationHelper.isLocationInEgypt(city.latitude, city.longitude)
        val method = if (inEgypt) 5 else if (city.country.contains("Saudi") || city.cityNameEnglish == "Riyadh") 4 else _uiState.value.selectedCalculationMethodId
        val methodTitle = PrayerTimeCalculator.CalculationMethod.fromId(method).titleArabic

        _uiState.value = _uiState.value.copy(
            selectedCity = city,
            isGpsLocationActive = false,
            userLocationDisplayName = null,
            isCityPickerVisible = false,
            selectedCalculationMethodId = method,
            calculationMethodTitle = methodTitle
        )
        refreshPrayerTimes(date = _uiState.value.selectedDate, forceNetwork = false)
    }

    fun showCityPicker(show: Boolean) {
        _uiState.value = _uiState.value.copy(isCityPickerVisible = show)
    }

    fun showBookmarkSheet(show: Boolean) {
        _uiState.value = _uiState.value.copy(isBookmarkSheetVisible = show)
    }

    fun togglePrayerCompleted(prayerKey: String) {
        viewModelScope.launch {
            val isCurrentCompleted = _completedPrayerKeys.value.contains(prayerKey)
            repository.togglePrayerCompleted(prayerKey, repository.getTodayDateString(), isCurrentCompleted)
        }
    }

    // --- Tasbih Actions ---
    fun selectActiveTasbih(id: String) {
        _uiState.value = _uiState.value.copy(activeTasbihId = id)
    }

    fun incrementActiveTasbih(currentCount: Int) {
        viewModelScope.launch {
            val id = _uiState.value.activeTasbihId
            repository.incrementTasbih(id, currentCount)
        }
    }

    fun resetActiveTasbih() {
        viewModelScope.launch {
            val id = _uiState.value.activeTasbihId
            repository.resetTasbih(id)
        }
    }

    fun updateTasbihTarget(newTarget: Int) {
        viewModelScope.launch {
            val id = _uiState.value.activeTasbihId
            repository.updateTasbihTarget(id, newTarget)
        }
    }

    // --- Athkar Actions ---
    fun selectAthkarCategory(category: AthkarCategory) {
        _uiState.value = _uiState.value.copy(selectedAthkarCategory = category)
    }

    fun incrementAthkar(athkarId: String, currentCount: Int, targetCount: Int) {
        viewModelScope.launch {
            repository.incrementAthkar(athkarId, currentCount, targetCount, repository.getTodayDateString())
        }
    }

    fun resetAthkar(athkarId: String) {
        viewModelScope.launch {
            repository.resetAthkar(athkarId, repository.getTodayDateString())
        }
    }

    // --- Quran Actions ---
    fun setQuranSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(quranSearchQuery = query)
    }

    fun openSurah(surah: Surah?) {
        if (surah == null) {
            _uiState.value = _uiState.value.copy(selectedSurahForReading = null)
        } else {
            val verifiedSurah = com.example.data.repository.QuranOfflineDataSource.getSurahByNumber(getApplication(), surah.number) ?: surah
            _uiState.value = _uiState.value.copy(selectedSurahForReading = verifiedSurah)
        }
    }

    fun toggleBookmark(ayah: Ayah, surahNameAr: String, surahNameEn: String, isBookmarked: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(ayah, surahNameAr, surahNameEn, isBookmarked)
        }
    }

    // --- Compass / Qibla ---
    fun updateCompassDegree(degree: Float) {
        _uiState.value = _uiState.value.copy(compassDegrees = degree)
    }

    // --- Navigation & Global Header Actions ---
    fun selectTab(tab: NoorNavTab) {
        val current = _uiState.value.currentTab
        _uiState.value = _uiState.value.copy(
            previousTab = if (current != NoorNavTab.SETTINGS) current else _uiState.value.previousTab,
            currentTab = tab
        )
    }

    fun openSettings() {
        val current = _uiState.value.currentTab
        _uiState.value = _uiState.value.copy(
            previousTab = if (current != NoorNavTab.SETTINGS) current else _uiState.value.previousTab,
            currentTab = NoorNavTab.SETTINGS
        )
    }

    fun closeSettings() {
        _uiState.value = _uiState.value.copy(
            currentTab = _uiState.value.previousTab
        )
    }

    fun toggleNotificationsDialog(visible: Boolean? = null) {
        _uiState.value = _uiState.value.copy(
            isNotificationsDialogVisible = visible ?: !_uiState.value.isNotificationsDialogVisible
        )
    }

    fun openTasbihSheet() {
        _uiState.value = _uiState.value.copy(isTasbihSheetVisible = true)
    }

    fun closeTasbihSheet() {
        _uiState.value = _uiState.value.copy(isTasbihSheetVisible = false)
    }

    fun setQuranFontSize(size: Float) {
        _uiState.value = _uiState.value.copy(quranFontSize = size.coerceIn(18f, 36f))
    }

    fun toggleAdhanReminder() {
        val next = !_uiState.value.adhanReminderEnabled
        _uiState.value = _uiState.value.copy(adhanReminderEnabled = next)
        prefs.edit().putBoolean("adhan_reminder_enabled", next).apply()
        if (next) {
            AdhanNotificationHelper.scheduleAllPrayerAdhans(getApplication(), _uiState.value.prayerTimes)
        } else {
            AdhanNotificationHelper.cancelAllPrayerAdhans(getApplication())
        }
    }

    fun togglePreAdhanReminder() {
        val next = !_uiState.value.preAdhanReminderEnabled
        _uiState.value = _uiState.value.copy(preAdhanReminderEnabled = next)
        prefs.edit().putBoolean("pre_adhan_reminder_enabled", next).apply()
    }

    fun toggleIqamahReminder() {
        val next = !_uiState.value.iqamahReminderEnabled
        _uiState.value = _uiState.value.copy(iqamahReminderEnabled = next)
        prefs.edit().putBoolean("iqamah_reminder_enabled", next).apply()
    }

    fun toggleMorningAthkarReminder() {
        val next = !_uiState.value.morningAthkarReminder
        _uiState.value = _uiState.value.copy(morningAthkarReminder = next)
        prefs.edit().putBoolean("morning_athkar_reminder", next).apply()
    }

    fun toggleEveningAthkarReminder() {
        val next = !_uiState.value.eveningAthkarReminder
        _uiState.value = _uiState.value.copy(eveningAthkarReminder = next)
        prefs.edit().putBoolean("evening_athkar_reminder", next).apply()
    }

    fun toggleFridayKahfReminder() {
        val next = !_uiState.value.fridayKahfReminder
        _uiState.value = _uiState.value.copy(fridayKahfReminder = next)
        prefs.edit().putBoolean("friday_kahf_reminder", next).apply()
    }

    fun toggleQiyamReminder() {
        val next = !_uiState.value.qiyamReminder
        _uiState.value = _uiState.value.copy(qiyamReminder = next)
        prefs.edit().putBoolean("qiyam_reminder", next).apply()
    }

    fun toggleHaptic() {
        val next = !_uiState.value.hapticEnabled
        _uiState.value = _uiState.value.copy(hapticEnabled = next)
        prefs.edit().putBoolean("haptic_enabled", next).apply()
    }

    fun setSelectedAdhanVoice(voice: String) {
        _uiState.value = _uiState.value.copy(selectedAdhanVoice = voice)
    }

    fun setSelectedHadithCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedHadithCategory = category)
    }

    fun setHadithSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(hadithSearchQuery = query)
    }

    fun toggleVisualTheme() {
        _uiState.value = _uiState.value.copy(isVisualThemeDark = !_uiState.value.isVisualThemeDark)
    }
    fun toggleLanguage() {
        val newLanguage = if (_uiState.value.selectedLanguage == "ar") "en" else "ar"
        _uiState.value = _uiState.value.copy(selectedLanguage = newLanguage)
    }

    // --- Quran Persistence Functions ---
    fun toggleSurahBookmark(surahNumber: Int) {
        val current = _uiState.value.bookmarkedSurahNumbers.toMutableSet()
        if (current.contains(surahNumber)) {
            current.remove(surahNumber)
        } else {
            current.add(surahNumber)
        }
        _uiState.value = _uiState.value.copy(bookmarkedSurahNumbers = current)
        prefs.edit().putStringSet("bookmarked_surahs", current.map { it.toString() }.toSet()).apply()
    }

    fun toggleSurahFavorite(surahNumber: Int) {
        val current = _uiState.value.favoriteSurahNumbers.toMutableSet()
        if (current.contains(surahNumber)) {
            current.remove(surahNumber)
        } else {
            current.add(surahNumber)
        }
        _uiState.value = _uiState.value.copy(favoriteSurahNumbers = current)
        prefs.edit().putStringSet("favorite_surahs", current.map { it.toString() }.toSet()).apply()
    }

    fun saveQuranReadingProgress(surahNumber: Int, ayahNumber: Int = 1) {
        val now = System.currentTimeMillis()
        _uiState.value = _uiState.value.copy(
            lastReadSurahNumber = surahNumber,
            lastReadAyahNumber = ayahNumber,
            lastReadTimestamp = now
        )
        prefs.edit()
            .putInt("last_read_surah", surahNumber)
            .putInt("last_read_ayah", ayahNumber)
            .putLong("last_read_timestamp", now)
            .apply()
    }

    fun getRelativeTimeString(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diffMs = now - timestamp
        if (diffMs < 0) return "الآن"
        val diffMin = diffMs / (1000 * 60)
        val diffHour = diffMin / 60
        val diffDay = diffHour / 24

        return when {
            diffMin < 1 -> "الآن"
            diffMin < 60 -> "منذ $diffMin دقيقة"
            diffHour == 1L -> "منذ ساعة"
            diffHour == 2L -> "منذ ساعتين"
            diffHour in 3..10 -> "منذ $diffHour ساعات"
            diffHour < 24 -> "منذ $diffHour ساعة"
            diffDay == 1L -> "أمس"
            diffDay == 2L -> "منذ يومين"
            diffDay in 3..10 -> "منذ $diffDay أيام"
            else -> "منذ $diffDay يومًا"
        }
    }
}
