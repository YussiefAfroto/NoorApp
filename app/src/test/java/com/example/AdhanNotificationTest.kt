package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.PrayerTime
import com.example.ui.viewmodel.NoorViewModel
import com.example.util.AdhanNotificationHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AdhanNotificationTest {

    private lateinit var app: Application

    @Before
    fun setUp() {
        app = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun verifyTimeParsing() {
        val epoch1 = AdhanNotificationHelper.parseTimeToEpoch("05:15")
        assertNotNull("Should parse 05:15", epoch1)

        val epoch2 = AdhanNotificationHelper.parseTimeToEpoch("05:15 ص")
        assertNotNull("Should parse 05:15 ص", epoch2)

        val epoch3 = AdhanNotificationHelper.parseTimeToEpoch("01:30 م")
        assertNotNull("Should parse 01:30 م", epoch3)
    }

    @Test
    fun verifyAdhanSchedulingAndCancellation() {
        val testPrayers = listOf(
            PrayerTime(key = "fajr", nameArabic = "الفجر", nameEnglish = "Fajr", timeFormatted = "04:30 ص", hour24 = 4, minute = 30),
            PrayerTime(key = "sunrise", nameArabic = "الشروق", nameEnglish = "Sunrise", timeFormatted = "06:00 ص", hour24 = 6, minute = 0),
            PrayerTime(key = "dhuhr", nameArabic = "الظهر", nameEnglish = "Dhuhr", timeFormatted = "12:15 م", hour24 = 12, minute = 15),
            PrayerTime(key = "asr", nameArabic = "العصر", nameEnglish = "Asr", timeFormatted = "03:30 م", hour24 = 15, minute = 30),
            PrayerTime(key = "maghrib", nameArabic = "المغرب", nameEnglish = "Maghrib", timeFormatted = "06:00 م", hour24 = 18, minute = 0),
            PrayerTime(key = "isha", nameArabic = "العشاء", nameEnglish = "Isha", timeFormatted = "07:30 م", hour24 = 19, minute = 30)
        )

        // Schedule
        AdhanNotificationHelper.scheduleAllPrayerAdhans(app, testPrayers)

        // Cancel
        AdhanNotificationHelper.cancelAllPrayerAdhans(app)
    }

    @Test
    fun verifyViewModelToggleAdhanReminder() {
        val viewModel = NoorViewModel(app)
        
        // Initial state is true
        assertTrue("Adhan reminder should be enabled by default", viewModel.uiState.value.adhanReminderEnabled)

        // Toggle OFF
        viewModel.toggleAdhanReminder()
        assertFalse("Adhan reminder should be disabled after toggle", viewModel.uiState.value.adhanReminderEnabled)

        // Toggle ON
        viewModel.toggleAdhanReminder()
        assertTrue("Adhan reminder should be enabled after toggle back", viewModel.uiState.value.adhanReminderEnabled)
    }
}
