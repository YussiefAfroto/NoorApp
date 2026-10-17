package com.example.data.repository

import com.example.data.local.AthkarProgressEntity
import com.example.data.local.NoorDao
import com.example.data.local.PrayerLogEntity
import com.example.data.local.QuranBookmarkEntity
import com.example.data.local.TasbihEntity
import com.example.data.model.Ayah
import com.example.data.model.CityPrayerPreset
import com.example.data.model.PrayerTime
import com.example.data.local.PrayerTimesCacheEntity
import com.example.data.remote.AlAdhanNetworkClient
import com.example.util.DateProvider
import com.example.util.PrayerTimeCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class PrayerDayResult(
    val prayerTimes: List<PrayerTime>,
    val imsak: String,
    val lastThird: String,
    val hijriFormatted: String,
    val timezone: String,
    val isOfflineFallback: Boolean,
    val calculationMethodTitle: String
)

class NoorRepository(private val noorDao: NoorDao) {

    fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date())
    }

    // --- Tasbih ---
    val allTasbih: Flow<List<TasbihEntity>> = noorDao.getAllTasbih()

    suspend fun initializeDefaultTasbihIfEmpty() {
        IslamicData.dhikrPresets.forEach { preset ->
            val existing = noorDao.getTasbihById(preset.id)
            if (existing == null) {
                noorDao.insertTasbih(
                    TasbihEntity(
                        id = preset.id,
                        arabicText = preset.arabicText,
                        transliteration = preset.transliteration,
                        englishMeaning = preset.englishMeaning,
                        count = 0,
                        target = preset.defaultTarget,
                        totalLifetimeCount = 0
                    )
                )
            }
        }
    }

    suspend fun incrementTasbih(id: String, currentCount: Int) {
        noorDao.incrementTasbih(id, currentCount + 1)
    }

    suspend fun resetTasbih(id: String) {
        noorDao.resetTasbih(id)
    }

    suspend fun updateTasbihTarget(id: String, target: Int) {
        val current = noorDao.getTasbihById(id)
        if (current != null) {
            noorDao.insertTasbih(current.copy(target = target, lastUpdated = System.currentTimeMillis()))
        }
    }

    // --- Prayer Logs ---
    fun getPrayerLogs(date: String): Flow<List<PrayerLogEntity>> {
        return noorDao.getPrayerLogsForDate(date)
    }

    suspend fun togglePrayerCompleted(prayerKey: String, date: String, currentStatus: Boolean) {
        val id = "${date}_$prayerKey"
        noorDao.insertPrayerLog(
            PrayerLogEntity(
                id = id,
                date = date,
                prayerKey = prayerKey,
                isCompleted = !currentStatus,
                completedTimestamp = if (!currentStatus) System.currentTimeMillis() else 0L
            )
        )
    }

    // --- Quran Bookmarks ---
    val allBookmarks: Flow<List<QuranBookmarkEntity>> = noorDao.getAllBookmarks()

    fun isAyahBookmarked(surahNumber: Int, ayahNumber: Int): Flow<Boolean> {
        return noorDao.isAyahBookmarked(surahNumber, ayahNumber)
    }

    suspend fun toggleBookmark(ayah: Ayah, surahNameArabic: String, surahNameEnglish: String, currentlyBookmarked: Boolean) {
        if (currentlyBookmarked) {
            noorDao.deleteBookmark(ayah.surahNumber, ayah.ayahNumber)
        } else {
            noorDao.insertBookmark(
                QuranBookmarkEntity(
                    surahNumber = ayah.surahNumber,
                    surahNameArabic = surahNameArabic,
                    surahNameEnglish = surahNameEnglish,
                    ayahNumber = ayah.ayahNumber,
                    arabicText = ayah.arabicText,
                    englishText = ayah.translationEnglish
                )
            )
        }
    }

    // --- Athkar Progress ---
    fun getAthkarProgress(date: String): Flow<List<AthkarProgressEntity>> {
        return noorDao.getAthkarProgressForDate(date)
    }

    suspend fun incrementAthkar(athkarId: String, currentCount: Int, targetCount: Int, date: String) {
        val newCount = currentCount + 1
        val isCompleted = newCount >= targetCount
        val id = "${date}_$athkarId"
        noorDao.insertAthkarProgress(
            AthkarProgressEntity(
                id = id,
                athkarId = athkarId,
                currentCount = newCount,
                targetCount = targetCount,
                isCompleted = isCompleted,
                date = date
            )
        )
    }

    suspend fun resetAthkar(athkarId: String, date: String) {
        val id = "${date}_$athkarId"
        noorDao.insertAthkarProgress(
            AthkarProgressEntity(
                id = id,
                athkarId = athkarId,
                currentCount = 0,
                targetCount = 1,
                isCompleted = false,
                date = date
            )
        )
    }

    // --- Dynamic Prayer Times (AlAdhan API + Multi-key Room Cache + Offline Fallback) ---
    suspend fun fetchPrayerTimes(
        latitude: Double,
        longitude: Double,
        method: Int = 5,
        completedPrayerKeys: Set<String>,
        date: LocalDate = LocalDate.now(),
        forceRefresh: Boolean = false
    ): PrayerDayResult = withContext(Dispatchers.IO) {
        val dateString = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.US))
        val latRounded = String.format(Locale.US, "%.3f", latitude)
        val lngRounded = String.format(Locale.US, "%.3f", longitude)
        val calcMethod = PrayerTimeCalculator.CalculationMethod.fromId(method)
        val defaultTz = PrayerTimeCalculator.getTimezoneOffsetHours(latitude, longitude, date)
        val cacheKey = "${dateString}_${latRounded}_${lngRounded}_m${method}"

        if (!forceRefresh) {
            val cached = noorDao.getPrayerTimesCache(cacheKey)
            if (cached != null) {
                return@withContext parseToPrayerDayResult(
                    cached = cached,
                    completedPrayerKeys = completedPrayerKeys,
                    date = date,
                    methodTitle = calcMethod.titleArabic
                )
            }
        }

        // Primary Source: AlAdhan REST API
        try {
            val response = AlAdhanNetworkClient.apiService.getTimings(
                date = dateString,
                latitude = latitude,
                longitude = longitude,
                method = method
            )
            val data = response.data
            val timings = data?.timings
            if (data != null && timings != null) {
                val tz = data.meta?.timezone ?: "Local"
                val hijri = data.date?.hijri
                val hijriFormatted = if (hijri != null) {
                    val mName = hijri.month?.ar ?: hijri.month?.en ?: ""
                    "${hijri.day} $mName ${hijri.year} هـ"
                } else {
                    DateProvider.getCurrentDateInfo(date).hijriFormatted
                }

                val entity = PrayerTimesCacheEntity(
                    cacheKey = cacheKey,
                    date = dateString,
                    latitude = latitude,
                    longitude = longitude,
                    method = method,
                    timezone = tz,
                    fajr = timings.fajr,
                    sunrise = timings.sunrise,
                    dhuhr = timings.dhuhr,
                    asr = timings.asr,
                    maghrib = timings.maghrib,
                    isha = timings.isha,
                    imsak = timings.imsak.ifEmpty { timings.fajr },
                    lastThird = timings.lastThird.ifEmpty { "02:30" },
                    hijriFormatted = hijriFormatted
                )
                noorDao.insertPrayerTimesCache(entity)

                return@withContext parseToPrayerDayResult(
                    cached = entity,
                    completedPrayerKeys = completedPrayerKeys,
                    date = date,
                    methodTitle = calcMethod.titleArabic,
                    isOfflineFallback = false
                )
            }
        } catch (_: Exception) {
            // Network unavailable: proceed to verified offline fallback
        }

        // Verified Offline Fallback
        val localCalc = PrayerTimeCalculator.calculatePrayerTimes(
            date = date,
            latitude = latitude,
            longitude = longitude,
            method = calcMethod,
            timeZoneOffsetHours = defaultTz
        )
        val (localLastThird, localImsak) = PrayerTimeCalculator.calculateExtraNightTimes(
            date = date,
            latitude = latitude,
            longitude = longitude,
            method = calcMethod,
            timeZoneOffsetHours = defaultTz
        )
        val dateInfo = DateProvider.getCurrentDateInfo(date)

        val rawList = listOf(
            PrayerTime("fajr", "الفجر", "Fajr", localCalc.fajr, getHourMinute(localCalc.fajrMillis).first, getHourMinute(localCalc.fajrMillis).second),
            PrayerTime("sunrise", "الشروق", "Sunrise", localCalc.sunrise, getHourMinute(localCalc.sunriseMillis).first, getHourMinute(localCalc.sunriseMillis).second),
            PrayerTime("dhuhr", "الظهر", "Dhuhr", localCalc.dhuhr, getHourMinute(localCalc.dhuhrMillis).first, getHourMinute(localCalc.dhuhrMillis).second),
            PrayerTime("asr", "العصر", "Asr", localCalc.asr, getHourMinute(localCalc.asrMillis).first, getHourMinute(localCalc.asrMillis).second),
            PrayerTime("maghrib", "المغرب", "Maghrib", localCalc.maghrib, getHourMinute(localCalc.maghribMillis).first, getHourMinute(localCalc.maghribMillis).second),
            PrayerTime("isha", "العشاء", "Isha", localCalc.isha, getHourMinute(localCalc.ishaMillis).first, getHourMinute(localCalc.ishaMillis).second)
        )

        val mappedPrayers = applyNextAndCompleted(rawList, completedPrayerKeys, date)

        PrayerDayResult(
            prayerTimes = mappedPrayers,
            imsak = localImsak,
            lastThird = localLastThird,
            hijriFormatted = dateInfo.hijriFormatted,
            timezone = "Offline",
            isOfflineFallback = true,
            calculationMethodTitle = calcMethod.titleArabic
        )
    }

    private fun parseToPrayerDayResult(
        cached: PrayerTimesCacheEntity,
        completedPrayerKeys: Set<String>,
        date: LocalDate,
        methodTitle: String,
        isOfflineFallback: Boolean = false
    ): PrayerDayResult {
        val fajrFmt = format24to12Arabic(cached.fajr)
        val sunriseFmt = format24to12Arabic(cached.sunrise)
        val dhuhrFmt = format24to12Arabic(cached.dhuhr)
        val asrFmt = format24to12Arabic(cached.asr)
        val maghribFmt = format24to12Arabic(cached.maghrib)
        val ishaFmt = format24to12Arabic(cached.isha)
        val imsakFmt = format24to12Arabic(cached.imsak)
        val lastThirdFmt = format24to12Arabic(cached.lastThird)

        val rawList = listOf(
            PrayerTime("fajr", "الفجر", "Fajr", fajrFmt.first, fajrFmt.second, fajrFmt.third),
            PrayerTime("sunrise", "الشروق", "Sunrise", sunriseFmt.first, sunriseFmt.second, sunriseFmt.third),
            PrayerTime("dhuhr", "الظهر", "Dhuhr", dhuhrFmt.first, dhuhrFmt.second, dhuhrFmt.third),
            PrayerTime("asr", "العصر", "Asr", asrFmt.first, asrFmt.second, asrFmt.third),
            PrayerTime("maghrib", "المغرب", "Maghrib", maghribFmt.first, maghribFmt.second, maghribFmt.third),
            PrayerTime("isha", "العشاء", "Isha", ishaFmt.first, ishaFmt.second, ishaFmt.third)
        )

        val mapped = applyNextAndCompleted(rawList, completedPrayerKeys, date)

        return PrayerDayResult(
            prayerTimes = mapped,
            imsak = imsakFmt.first,
            lastThird = lastThirdFmt.first,
            hijriFormatted = cached.hijriFormatted,
            timezone = cached.timezone,
            isOfflineFallback = isOfflineFallback,
            calculationMethodTitle = methodTitle
        )
    }

    private fun format24to12Arabic(rawTime: String): Triple<String, Int, Int> {
        val clean = rawTime.trim().split(" ")[0]
        val parts = clean.split(":")
        val h24 = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val min = parts.getOrNull(1)?.toIntOrNull() ?: 0
        val suffix = if (h24 >= 12) "م" else "ص"
        var h12 = h24 % 12
        if (h12 == 0) h12 = 12
        val formatted = String.format(Locale.US, "%02d:%02d %s", h12, min, suffix)
        return Triple(formatted, h24, min)
    }

    private fun applyNextAndCompleted(
        rawList: List<PrayerTime>,
        completedPrayerKeys: Set<String>,
        date: LocalDate
    ): List<PrayerTime> {
        val isToday = (date == LocalDate.now())
        val now = Calendar.getInstance()
        val currentTimeMinutes = if (isToday) now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE) else -1

        var nextIndex = -1
        if (isToday) {
            val prayerMinutes = rawList.map { it.hour24 * 60 + it.minute }
            for (i in rawList.indices) {
                if (rawList[i].key != "sunrise" && prayerMinutes[i] > currentTimeMinutes) {
                    nextIndex = i
                    break
                }
            }
            if (nextIndex == -1) nextIndex = 0
        }

        return rawList.mapIndexed { index, prayer ->
            prayer.copy(
                isNext = (index == nextIndex),
                isCompleted = completedPrayerKeys.contains(prayer.key)
            )
        }
    }

    private fun getHourMinute(millis: Long): Pair<Int, Int> {
        val c = Calendar.getInstance().apply { timeInMillis = millis }
        return c.get(Calendar.HOUR_OF_DAY) to c.get(Calendar.MINUTE)
    }

    // --- Legacy / Synchronous Calculation Preserved ---
    fun calculatePrayerTimes(
        city: CityPrayerPreset,
        completedPrayerKeys: Set<String>,
        date: java.time.LocalDate = java.time.LocalDate.now()
    ): List<PrayerTime> {
        val method = if (city.country == "Egypt" || city.cityNameEnglish == "Cairo") {
            PrayerTimeCalculator.CalculationMethod.EGYPTIAN
        } else {
            PrayerTimeCalculator.CalculationMethod.UMM_AL_QURA
        }

        val pt = PrayerTimeCalculator.calculatePrayerTimes(
            date = date,
            latitude = city.latitude,
            longitude = city.longitude,
            method = method
        )

        val (fajrH, fajrM) = getHourMinute(pt.fajrMillis)
        val (sunriseH, sunriseM) = getHourMinute(pt.sunriseMillis)
        val (dhuhrH, dhuhrM) = getHourMinute(pt.dhuhrMillis)
        val (asrH, asrM) = getHourMinute(pt.asrMillis)
        val (maghribH, maghribM) = getHourMinute(pt.maghribMillis)
        val (ishaH, ishaM) = getHourMinute(pt.ishaMillis)

        val rawList = listOf(
            PrayerTime("fajr", "الفجر", "Fajr", pt.fajr, fajrH, fajrM),
            PrayerTime("sunrise", "الشروق", "Sunrise", pt.sunrise, sunriseH, sunriseM),
            PrayerTime("dhuhr", "الظهر", "Dhuhr", pt.dhuhr, dhuhrH, dhuhrM),
            PrayerTime("asr", "العصر", "Asr", pt.asr, asrH, asrM),
            PrayerTime("maghrib", "المغرب", "Maghrib", pt.maghrib, maghribH, maghribM),
            PrayerTime("isha", "العشاء", "Isha", pt.isha, ishaH, ishaM)
        )

        return applyNextAndCompleted(rawList, completedPrayerKeys, date)
    }
}
