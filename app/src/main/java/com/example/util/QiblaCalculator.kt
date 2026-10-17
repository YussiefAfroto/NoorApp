package com.example.util

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

object QiblaCalculator {
    const val KAABA_LATITUDE = 21.422487
    const val KAABA_LONGITUDE = 39.826206
    private const val EARTH_RADIUS_KM = 6371.0

    /**
     * Calculates the great-circle initial bearing from user's (lat, lng) to the Kaaba in Makkah.
     * Returns true north azimuth in degrees [0, 360).
     */
    fun calculateBearing(latitude: Double, longitude: Double): Float {
        val phi1 = Math.toRadians(latitude)
        val phi2 = Math.toRadians(KAABA_LATITUDE)
        val deltaLambda = Math.toRadians(KAABA_LONGITUDE - longitude)

        val y = sin(deltaLambda) * cos(phi2)
        val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(deltaLambda)
        val initialBearingRad = atan2(y, x)
        val initialBearingDeg = (Math.toDegrees(initialBearingRad) + 360.0) % 360.0
        return initialBearingDeg.toFloat()
    }

    /**
     * Calculates the great-circle distance in kilometers from user's (lat, lng) to Kaaba, Makkah.
     */
    fun calculateDistanceKm(latitude: Double, longitude: Double): Int {
        val phi1 = Math.toRadians(latitude)
        val phi2 = Math.toRadians(KAABA_LATITUDE)
        val dLat = Math.toRadians(KAABA_LATITUDE - latitude)
        val dLon = Math.toRadians(KAABA_LONGITUDE - longitude)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(phi1) * cos(phi2) * sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return (EARTH_RADIUS_KM * c).roundToInt()
    }

    fun calculateQibla(latitude: Double, longitude: Double): Pair<Float, Int> {
        return Pair(calculateBearing(latitude, longitude), calculateDistanceKm(latitude, longitude))
    }
}
