package com.example.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.core.content.ContextCompat
import java.util.Locale

object LocationHelper {

    data class DeviceLocation(
        val latitude: Double,
        val longitude: Double,
        val cityNameArabic: String?,
        val cityNameEnglish: String?,
        val isEgypt: Boolean
    )

    fun hasLocationPermission(context: Context): Boolean {
        val finePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarsePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return finePermission || coarsePermission
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context): DeviceLocation? {
        if (!hasLocationPermission(context)) return null

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return null

        val providers = locationManager.getProviders(true)
        var bestLocation: Location? = null

        for (provider in providers) {
            val l = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                bestLocation = l
            }
        }

        val location = bestLocation ?: return null
        val lat = location.latitude
        val lng = location.longitude
        val inEgypt = isLocationInEgypt(lat, lng)

        val (cityAr, cityEn) = getCityNames(context, lat, lng)

        return DeviceLocation(
            latitude = lat,
            longitude = lng,
            cityNameArabic = cityAr,
            cityNameEnglish = cityEn,
            isEgypt = inEgypt
        )
    }

    /**
     * Checks if coordinates fall within the geographic bounding box of Egypt
     * Lat: ~22.0°N to ~31.7°N
     * Lng: ~25.0°E to ~37.0°E
     */
    fun isLocationInEgypt(latitude: Double, longitude: Double): Boolean {
        return latitude in 21.8..31.8 && longitude in 24.8..37.0
    }

    private fun getCityNames(context: Context, latitude: Double, longitude: Double): Pair<String?, String?> {
        return try {
            if (Geocoder.isPresent()) {
                val geocoderAr = Geocoder(context, Locale("ar"))
                val geocoderEn = Geocoder(context, Locale.US)

                val addressesAr = geocoderAr.getFromLocation(latitude, longitude, 1)
                val addressesEn = geocoderEn.getFromLocation(latitude, longitude, 1)

                val cityAr = addressesAr?.firstOrNull()?.let {
                    it.locality ?: it.subAdminArea ?: it.adminArea
                }

                val cityEn = addressesEn?.firstOrNull()?.let {
                    it.locality ?: it.subAdminArea ?: it.adminArea
                }

                cityAr to cityEn
            } else {
                null to null
            }
        } catch (_: Exception) {
            null to null
        }
    }
}
