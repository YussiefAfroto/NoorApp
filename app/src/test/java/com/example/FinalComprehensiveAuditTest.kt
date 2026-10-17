package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.IslamicData
import com.example.data.repository.QuranOfflineDataSource
import com.example.ui.screens.QuranJuzData
import com.example.ui.viewmodel.NoorViewModel
import com.example.util.AdhanNotificationHelper
import com.example.util.LocationHelper
import com.example.util.PrayerTimeCalculator
import com.example.util.QiblaCalculator
import org.json.JSONArray
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FinalComprehensiveAuditTest {

    private lateinit var context: Context
    private lateinit var app: Application

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        app = ApplicationProvider.getApplicationContext()
    }

    // ========================================================
    // 1. QURAN AUDIT TESTS
    // ========================================================
    @Test
    fun auditQuranCanonicalIntegrity() {
        val surahs = QuranOfflineDataSource.getSurahs(context)
        assertEquals("Total number of Surahs must be exactly 114", 114, surahs.size)

        val totalAyahs = surahs.sumOf { it.ayahs.size }
        assertEquals("Total Quran Ayahs must be exactly 6236", 6236, totalAyahs)

        val expectedAyahCounts = listOf(
            7, 286, 200, 176, 120, 165, 206, 75, 129, 109, 123, 111, 43, 52, 99, 128, 111, 110, 98, 135,
            112, 78, 118, 64, 77, 227, 93, 88, 69, 60, 34, 30, 73, 54, 45, 83, 182, 88, 75, 85,
            54, 53, 89, 59, 37, 35, 38, 29, 18, 45, 60, 49, 62, 55, 78, 96, 29, 22, 24, 13,
            14, 11, 11, 18, 12, 12, 30, 52, 52, 44, 28, 28, 20, 56, 40, 31, 50, 40, 46, 42,
            29, 19, 36, 25, 22, 17, 19, 26, 30, 20, 15, 21, 11, 8, 8, 19, 5, 8, 8, 11,
            11, 8, 3, 9, 5, 4, 7, 3, 6, 3, 5, 4, 5, 6
        )

        for (i in 0 until 114) {
            val s = surahs[i]
            assertEquals("Surah number must match index", i + 1, s.number)
            assertEquals("Surah ${s.number} (${s.nameArabic}) verse count mismatch", expectedAyahCounts[i], s.ayahs.size)
            assertTrue("Surah name Arabic must not be blank", s.nameArabic.isNotBlank())
            assertTrue("Surah name English must not be blank", s.nameEnglish.isNotBlank())

            for (j in s.ayahs.indices) {
                val a = s.ayahs[j]
                assertEquals("Ayah number must match index", j + 1, a.ayahNumber)
                assertTrue("Ayah ${s.number}:${a.ayahNumber} text must not be blank", a.arabicText.isNotBlank())
                assertFalse("Ayah must not have placeholder text", a.arabicText.contains("placeholder", ignoreCase = true))
            }
        }
    }

    @Test
    fun auditQuranJuzCoverage() {
        val allJuzs = QuranJuzData.all30Juzs
        assertEquals("Must have exactly 30 Juzs", 30, allJuzs.size)

        for (i in 1..30) {
            val juz = allJuzs[i - 1]
            assertEquals(i, juz.number)
            assertTrue(juz.nameArabic.isNotBlank())
            assertTrue(juz.surahNumbers.isNotEmpty())
        }
    }

    // ========================================================
    // 2. HADITH AUDIT TESTS
    // ========================================================
    @Test
    fun auditHadithDatasetIntegrity() {
        val jsonStr = context.assets.open("hadith/hadith_offline.json").bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(jsonStr)
        assertTrue("Hadith dataset must not be empty", jsonArray.length() >= 50)

        val seenMatn = mutableSetOf<String>()
        val categories = mutableSetOf<String>()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val matn = obj.getString("matnArabic").trim()
            assertTrue("Hadith matn must not be blank at index $i", matn.isNotBlank())
            assertFalse("Duplicate hadith found: $matn", seenMatn.contains(matn))
            seenMatn.add(matn)

            val cat = obj.getString("categoryArabic")
            assertTrue("Category must not be blank", cat.isNotBlank())
            categories.add(cat)

            assertTrue(obj.getString("collectionName").isNotBlank())
            assertTrue(obj.getString("sourceArabic").isNotBlank())
            assertTrue(obj.getString("gradeArabic").isNotBlank())
        }

        assertTrue("Must contain multiple Hadith categories", categories.size >= 5)
    }

    // ========================================================
    // 3. AZKAR AUDIT TESTS
    // ========================================================
    @Test
    fun auditAzkarDatasetIntegrity() {
        val jsonStr = context.assets.open("azkar/azkar_offline.json").bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(jsonStr)
        assertTrue("Azkar dataset must not be empty", jsonArray.length() >= 50)

        val seenIds = mutableSetOf<String>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val id = obj.getString("id")
            assertFalse("Duplicate dhikr ID: $id", seenIds.contains(id))
            seenIds.add(id)

            assertTrue("Arabic text must not be blank", obj.getString("arabicText").isNotBlank())
            assertTrue("Title Arabic must not be blank", obj.getString("titleArabic").isNotBlank())
            assertTrue("Repeat target must be > 0", obj.getInt("repeatTarget") > 0)
        }
    }

    // ========================================================
    // 4. PRAYER TIMES & EGAS METHOD 5 AUDIT TESTS
    // ========================================================
    @Test
    fun auditPrayerTimesCalculationEGASMethod5() {
        val date = LocalDate.of(2026, 10, 2)
        // Cairo coordinates
        val cairoLat = 30.0444
        val cairoLng = 31.2357
        val methodEGAS = PrayerTimeCalculator.CalculationMethod.EGYPTIAN

        val times = PrayerTimeCalculator.calculatePrayerTimes(
            date = date,
            latitude = cairoLat,
            longitude = cairoLng,
            method = methodEGAS,
            timeZoneOffsetHours = 2.0
        )

        assertNotNull(times.fajr)
        assertNotNull(times.sunrise)
        assertNotNull(times.dhuhr)
        assertNotNull(times.asr)
        assertNotNull(times.maghrib)
        assertNotNull(times.isha)

        assertTrue("Fajr must precede Sunrise", times.fajrMillis < times.sunriseMillis)
        assertTrue("Sunrise must precede Dhuhr", times.sunriseMillis < times.dhuhrMillis)
        assertTrue("Dhuhr must precede Asr", times.dhuhrMillis < times.asrMillis)
        assertTrue("Asr must precede Maghrib", times.asrMillis < times.maghribMillis)
        assertTrue("Maghrib must precede Isha", times.maghribMillis < times.ishaMillis)
    }

    // ========================================================
    // 5. QIBLA DYNAMIC CALCULATION AUDIT TESTS
    // ========================================================
    @Test
    fun auditDynamicQiblaCalculationsAcrossWorld() {
        // Cairo
        val cairoQibla = QiblaCalculator.calculateBearing(30.0444, 31.2357)
        assertEquals(136.1f, cairoQibla, 1.5f)

        // Alexandria
        val alexQibla = QiblaCalculator.calculateBearing(31.2001, 29.9187)
        assertEquals(135.4f, alexQibla, 1.5f)

        // Suez
        val suezQibla = QiblaCalculator.calculateBearing(29.9668, 32.5498)
        assertEquals(140.9f, suezQibla, 1.5f)

        // Riyadh
        val riyadhQibla = QiblaCalculator.calculateBearing(24.7136, 46.6753)
        assertEquals(243.8f, riyadhQibla, 2.0f)

        // London
        val londonQibla = QiblaCalculator.calculateBearing(51.5074, -0.1278)
        assertEquals(119.0f, londonQibla, 2.0f)
    }

    // ========================================================
    // 6. ADHAN & SETTINGS PERSISTENCE AUDIT TESTS
    // ========================================================
    @Test
    fun auditSettingsPersistenceAndAdhan() {
        val viewModel = NoorViewModel(app)
        val initialAdhan = viewModel.uiState.value.adhanReminderEnabled

        viewModel.toggleAdhanReminder()
        val toggledAdhan = viewModel.uiState.value.adhanReminderEnabled
        assertEquals(!initialAdhan, toggledAdhan)

        val prefs = app.getSharedPreferences("noor_settings", Context.MODE_PRIVATE)
        assertEquals(toggledAdhan, prefs.getBoolean("adhan_reminder_enabled", false))

        // Restore
        viewModel.toggleAdhanReminder()
        assertEquals(initialAdhan, viewModel.uiState.value.adhanReminderEnabled)
    }

    @Test
    fun auditAppVersionString() {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        assertEquals("1.0", pInfo.versionName)
    }
}
