package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.IslamicData
import com.example.ui.viewmodel.NoorViewModel
import com.example.util.LocationHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DefaultLocationAndSplashTest {

    private lateinit var app: Application

    @Before
    fun setUp() {
        app = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun verifyDefaultLocationIsCairoEgypt() {
        val defaultCity = IslamicData.cities[0]
        assertEquals("Default city English name must be Cairo", "Cairo", defaultCity.cityNameEnglish)
        assertEquals("Default city Arabic name must be القاهرة", "القاهرة", defaultCity.cityNameArabic)
        assertEquals(30.0444, defaultCity.latitude, 0.001)
        assertEquals(31.2357, defaultCity.longitude, 0.001)

        val viewModel = NoorViewModel(app)
        val state = viewModel.uiState.value
        assertEquals("Initial selectedCity must be Cairo", "Cairo", state.selectedCity.cityNameEnglish)
        assertEquals("Initial selectedCity Arabic must be القاهرة", "القاهرة", state.selectedCity.cityNameArabic)
        assertFalse("GPS location should not be active initially before GPS fix", state.isGpsLocationActive)
    }

    @Test
    fun verifyDynamicGpsUpdatesWhenAvailable() {
        val viewModel = NoorViewModel(app)
        val initialCity = viewModel.uiState.value.selectedCity.cityNameEnglish
        assertEquals("Cairo", initialCity)

        // Simulate GPS fix from Alexandria, Egypt
        val mockLocation = LocationHelper.DeviceLocation(
            latitude = 31.2001,
            longitude = 29.9187,
            cityNameArabic = "الإسكندرية",
            cityNameEnglish = "Alexandria",
            isEgypt = true
        )

        viewModel.updateGpsLocation(mockLocation)
        val updatedState = viewModel.uiState.value

        assertTrue("GPS location must be marked active", updatedState.isGpsLocationActive)
        assertEquals(31.2001, updatedState.userLatitude!!, 0.0001)
        assertEquals(29.9187, updatedState.userLongitude!!, 0.0001)
        assertTrue(updatedState.userLocationDisplayName?.contains("الإسكندرية") == true || updatedState.userLocationDisplayName?.contains("Alexandria") == true)
    }
}
