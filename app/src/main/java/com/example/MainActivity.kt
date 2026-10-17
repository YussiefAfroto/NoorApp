package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.core.view.WindowCompat
import com.example.ui.components.BookmarksSheet
import com.example.ui.components.CalculationMethodSheet
import com.example.ui.components.CityPickerSheet
import com.example.ui.components.NoorHeader
import com.example.ui.components.NoorNavBar
import com.example.ui.components.NoorNotificationsDialog
import com.example.ui.components.SplashScreen
import com.example.ui.screens.AthkarScreen
import com.example.ui.screens.HadithScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PrayerTimesScreen
import com.example.ui.screens.QiblaScreen
import com.example.ui.screens.QuranScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TasbihScreen
import com.example.ui.theme.CelestialBackground
import com.example.ui.theme.NoorTheme
import com.example.ui.viewmodel.NoorNavTab
import com.example.ui.viewmodel.NoorViewModel
import com.example.util.LocationHelper
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val viewModel: NoorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            NoorApp(viewModel = viewModel)
        }
    }
}

@Composable
fun NoorApp(viewModel: NoorViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val tasbihList by viewModel.tasbihList.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()
    val athkarProgressMap by viewModel.athkarProgressMap.collectAsState()
    val context = LocalContext.current
    var showSplash by remember { mutableStateOf(true) }

    val isArabic = uiState.selectedLanguage == "ar"
    val layoutDirection = if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr

    // Short and smooth splash duration
    LaunchedEffect(Unit) {
        delay(1200)
        showSplash = false
    }

    // --- Location permission launcher ---
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            val location = LocationHelper.getCurrentLocation(context)
            if (location != null) {
                viewModel.updateGpsLocation(location)
            }
        }
    }

    // Request permissions on first composition
    LaunchedEffect(Unit) {
        if (LocationHelper.hasLocationPermission(context)) {
            val location = LocationHelper.getCurrentLocation(context)
            if (location != null) {
                viewModel.updateGpsLocation(location)
            }
        }
        val perms = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
        if (!LocationHelper.hasLocationPermission(context)) {
            locationPermissionLauncher.launch(perms)
        }
    }

    // Handle back button when in Settings or deep screens
    if (uiState.currentTab == NoorNavTab.SETTINGS) {
        BackHandler {
            viewModel.closeSettings()
        }
    } else if (uiState.currentTab == NoorNavTab.TASBIH) {
        BackHandler {
            viewModel.selectTab(NoorNavTab.HOME)
        }
    } else if (uiState.selectedSurahForReading != null) {
        BackHandler {
            viewModel.openSurah(null)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        NoorTheme {
            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CelestialBackground),
                topBar = {
                    NoorHeader(
                        isArabic = isArabic,
                        selectedCity = uiState.selectedCity,
                        onCityClick = { viewModel.showCityPicker(true) },
                        onNotificationsClick = { viewModel.toggleNotificationsDialog(true) },
                        onSettingsClick = { viewModel.openSettings() }
                    )
                },
                bottomBar = {
                    NoorNavBar(
                        currentTab = uiState.currentTab,
                        isArabic = isArabic,
                        onTabSelected = { viewModel.selectTab(it) }
                    )
                },
                containerColor = CelestialBackground
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    AnimatedContent(
                        targetState = uiState.currentTab,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "TabContentTransition"
                    ) { tab ->
                        when (tab) {
                            NoorNavTab.HOME -> {
                                HomeScreen(
                                    isArabic = isArabic,
                                    prayerTimes = uiState.prayerTimes,
                                    nextPrayerName = uiState.nextPrayerName,
                                    countdown = uiState.nextPrayerCountdown,
                                    completedPrayersCount = uiState.completedPrayersCount,
                                    lastReadSurahNumber = uiState.lastReadSurahNumber,
                                    lastReadAyahNumber = uiState.lastReadAyahNumber,
                                    onNavigateToQuran = { viewModel.selectTab(NoorNavTab.QURAN) },
                                    onNavigateToPrayers = { viewModel.selectTab(NoorNavTab.PRAYER_TIMES) },
                                    onNavigateToHadith = { viewModel.selectTab(NoorNavTab.HADITH) },
                                    onNavigateToAzkar = { category ->
                                        viewModel.selectAthkarCategory(category)
                                        viewModel.selectTab(NoorNavTab.ATHKAR)
                                    },
                                    onOpenTasbih = { viewModel.selectTab(NoorNavTab.TASBIH) }
                                )
                            }
                            NoorNavTab.QURAN -> {
                                QuranScreen(
                                    isArabic = isArabic,
                                    searchQuery = uiState.quranSearchQuery,
                                    selectedSurah = uiState.selectedSurahForReading,
                                    bookmarks = bookmarks,
                                    bookmarkedSurahNumbers = uiState.bookmarkedSurahNumbers,
                                    favoriteSurahNumbers = uiState.favoriteSurahNumbers,
                                    lastReadSurahNumber = uiState.lastReadSurahNumber,
                                    lastReadAyahNumber = uiState.lastReadAyahNumber,
                                    lastReadTimestamp = uiState.lastReadTimestamp,
                                    onSearchQueryChange = { viewModel.setQuranSearchQuery(it) },
                                    onSelectSurah = { viewModel.openSurah(it) },
                                    onToggleBookmark = { ayah, surahAr, surahEn, isBookmarked ->
                                        viewModel.toggleBookmark(ayah, surahAr, surahEn, isBookmarked)
                                    },
                                    onToggleSurahBookmark = { viewModel.toggleSurahBookmark(it) },
                                    onToggleSurahFavorite = { viewModel.toggleSurahFavorite(it) },
                                    onSaveReadingProgress = { surahNum, ayahNum -> viewModel.saveQuranReadingProgress(surahNum, ayahNum) },
                                    getRelativeTimeString = { viewModel.getRelativeTimeString(it) }
                                )
                            }
                            NoorNavTab.PRAYER_TIMES -> {
                                PrayerTimesScreen(
                                    isArabic = isArabic,
                                    selectedCity = uiState.selectedCity,
                                    countdown = uiState.nextPrayerCountdown,
                                    prayerTimes = uiState.prayerTimes,
                                    nextPrayerName = uiState.nextPrayerName,
                                    onNavigateToQibla = { viewModel.selectTab(NoorNavTab.QIBLA) },
                                    onOpenCityPicker = { viewModel.showCityPicker(true) },
                                    onTogglePrayerCompleted = { viewModel.togglePrayerCompleted(it) },
                                    calculationMethodTitle = uiState.calculationMethodTitle,
                                    imsakTime = uiState.imsakTime,
                                    lastThirdTime = uiState.lastThirdTime,
                                    hijriDateText = uiState.hijriDateFormatted,
                                    userLocationDisplayName = uiState.userLocationDisplayName,
                                    onDateChanged = { date ->
                                        viewModel.refreshPrayerTimes(date, forceNetwork = false)
                                    }
                                )
                            }
                            NoorNavTab.QIBLA -> {
                                // MANDATORY: Qibla page is UNCHANGED as required
                                QiblaScreen(
                                    isArabic = isArabic,
                                    selectedCity = uiState.selectedCity
                                )
                            }
                            NoorNavTab.ATHKAR -> {
                                // MANDATORY: Azkar page is UNCHANGED as required
                                AthkarScreen(
                                    isArabic = isArabic,
                                    selectedCategory = uiState.selectedAthkarCategory,
                                    athkarProgressMap = athkarProgressMap,
                                    onSelectCategory = { viewModel.selectAthkarCategory(it) },
                                    onIncrementAthkar = { id, current, target ->
                                        viewModel.incrementAthkar(id, current, target)
                                    },
                                    onResetAthkar = { viewModel.resetAthkar(it) }
                                )
                            }
                            NoorNavTab.HADITH -> {
                                HadithScreen(
                                    isArabic = isArabic
                                )
                            }
                            NoorNavTab.SETTINGS -> {
                                SettingsScreen(
                                    isArabic = isArabic,
                                    adhanReminderEnabled = uiState.adhanReminderEnabled,
                                    preAdhanReminder = uiState.preAdhanReminderEnabled,
                                    iqamahReminder = uiState.iqamahReminderEnabled,
                                    morningReminder = uiState.morningAthkarReminder,
                                    eveningReminder = uiState.eveningAthkarReminder,
                                    fridayReminder = uiState.fridayKahfReminder,
                                    qiyamReminder = uiState.qiyamReminder,
                                    hapticEnabled = uiState.hapticEnabled,
                                    onBack = { viewModel.closeSettings() },
                                    onToggleAdhan = { viewModel.toggleAdhanReminder() },
                                    onTogglePreAdhan = { viewModel.togglePreAdhanReminder() },
                                    onToggleIqamah = { viewModel.toggleIqamahReminder() },
                                    onToggleMorning = { viewModel.toggleMorningAthkarReminder() },
                                    onToggleEvening = { viewModel.toggleEveningAthkarReminder() },
                                    onToggleFriday = { viewModel.toggleFridayKahfReminder() },
                                    onToggleQiyam = { viewModel.toggleQiyamReminder() },
                                    onToggleHaptic = { viewModel.toggleHaptic() },
                                    onToggleLanguage = { viewModel.toggleLanguage() }
                                )
                            }
                            NoorNavTab.TASBIH -> {
                                TasbihScreen(
                                    isArabic = isArabic,
                                    tasbihList = tasbihList,
                                    activeTasbihId = uiState.activeTasbihId,
                                    hapticEnabled = uiState.hapticEnabled,
                                    onSelectTasbih = { viewModel.selectActiveTasbih(it) },
                                    onIncrement = { viewModel.incrementActiveTasbih(it) },
                                    onReset = { viewModel.resetActiveTasbih() },
                                    onUpdateTarget = { viewModel.updateTasbihTarget(it) },
                                    onToggleHaptic = { viewModel.toggleHaptic() }
                                )
                            }
                        }
                    }
                }

                // Notifications Dialog
                if (uiState.isNotificationsDialogVisible) {
                    NoorNotificationsDialog(
                        onDismiss = { viewModel.toggleNotificationsDialog(false) }
                    )
                }

                // City Picker Modal Bottom Sheet
                if (uiState.isCityPickerVisible) {
                    CityPickerSheet(
                        isArabic = isArabic,
                        currentCity = uiState.selectedCity,
                        onSelectCity = { viewModel.selectCity(it) },
                        onDismiss = { viewModel.showCityPicker(false) }
                    )
                }

                // Bookmarks Modal Bottom Sheet
                if (uiState.isBookmarkSheetVisible) {
                    BookmarksSheet(
                        isArabic = isArabic,
                        bookmarks = bookmarks,
                        onDeleteBookmark = { bookmarkId ->
                            val b = bookmarks.firstOrNull { it.id == bookmarkId }
                            if (b != null) {
                                viewModel.toggleBookmark(
                                    ayah = com.example.data.model.Ayah(b.surahNumber, b.ayahNumber, b.arabicText, b.englishText),
                                    surahNameAr = b.surahNameArabic,
                                    surahNameEn = b.surahNameEnglish,
                                    isBookmarked = true
                                )
                            }
                        },
                        onDismiss = { viewModel.showBookmarkSheet(false) }
                    )
                }

                // Calculation Method Picker Modal Bottom Sheet
                if (uiState.isMethodPickerVisible) {
                    CalculationMethodSheet(
                        isArabic = isArabic,
                        currentMethodId = uiState.selectedCalculationMethodId,
                        onSelectMethod = { methodId ->
                            viewModel.selectCalculationMethod(methodId)
                        },
                        onDismiss = { viewModel.showMethodPicker(false) }
                    )
                }
            }
        }
    }

    AnimatedVisibility(
        visible = showSplash,
        enter = fadeIn(),
        exit = fadeOut(animationSpec = tween(400))
    ) {
        SplashScreen()
    }
}
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    androidx.compose.material3.Text(
        text = "مرحباً $name في تطبيق نُور",
        modifier = modifier
    )
}
