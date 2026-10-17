package com.example.util

import com.example.data.model.PrayerTimes
import java.time.LocalDate
import java.util.Calendar
import java.util.TimeZone
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.tan

object PrayerTimeCalculator {

    enum class CalculationMethod(val id: Int, val titleArabic: String, val titleEnglish: String, val fajrAngle: Double, val ishaAngle: Double, val ishaIntervalMin: Int) {
        KARACHI(1, "جامعة العلوم الإسلامية بكراتشي", "University of Islamic Sciences, Karachi", 18.0, 18.0, 0),
        NORTH_AMERICA(2, "الجمعية الإسلامية لأمريكا الشمالية (ISNA)", "Islamic Society of North America", 15.0, 15.0, 0),
        MUSLIM_WORLD_LEAGUE(3, "رابطة العالم الإسلامي", "Muslim World League", 18.0, 17.0, 0),
        UMM_AL_QURA(4, "تقويم أم القرى (مكة المكرمة)", "Umm Al-Qura University, Makkah", 18.5, 0.0, 90),
        EGYPTIAN(5, "الهيئة المصرية العامة للمساحة", "Egyptian General Authority of Survey", 19.5, 17.5, 0);

        companion object {
            fun fromId(id: Int): CalculationMethod {
                return values().firstOrNull { it.id == id } ?: EGYPTIAN
            }
        }
    }

    data class AstronomicalTimes(
        val fajr: Double,
        val sunrise: Double,
        val dhuhr: Double,
        val asr: Double,
        val maghrib: Double,
        val isha: Double,
        val imsak: Double,
        val lastThirdOfNight: Double,
        val midnight: Double
    )

    fun getTimezoneOffsetHours(latitude: Double, longitude: Double, date: LocalDate = LocalDate.now()): Double {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, date.year)
            set(Calendar.MONTH, date.monthValue - 1)
            set(Calendar.DAY_OF_MONTH, date.dayOfMonth)
        }
        val targetMillis = cal.timeInMillis

        val tz = when {
            latitude in 21.8..31.8 && longitude in 24.8..37.0 -> TimeZone.getTimeZone("Africa/Cairo")
            latitude in 16.0..32.5 && longitude in 36.5..55.5 -> TimeZone.getTimeZone("Asia/Riyadh")
            latitude in 22.5..26.5 && longitude in 51.0..56.5 -> TimeZone.getTimeZone("Asia/Dubai")
            else -> TimeZone.getDefault()
        }
        return tz.getOffset(targetMillis) / 3600000.0
    }

    fun calculatePrayerTimes(
        date: LocalDate = LocalDate.now(),
        latitude: Double,
        longitude: Double,
        method: CalculationMethod = CalculationMethod.EGYPTIAN,
        timeZoneOffsetHours: Double = getTimezoneOffsetHours(latitude, longitude, date)
    ): PrayerTimes {
        val julianDate = getJulianDate(date.year, date.monthValue, date.dayOfMonth)

        val times = computeDayTimes(julianDate, latitude, longitude, timeZoneOffsetHours, method)

        val fajrStr = formatTime12h(times.fajr)
        val sunriseStr = formatTime12h(times.sunrise)
        val dhuhrStr = formatTime12h(times.dhuhr)
        val asrStr = formatTime12h(times.asr)
        val maghribStr = formatTime12h(times.maghrib)
        val ishaStr = formatTime12h(times.isha)

        val baseCal = Calendar.getInstance().apply {
            set(Calendar.YEAR, date.year)
            set(Calendar.MONTH, date.monthValue - 1)
            set(Calendar.DAY_OF_MONTH, date.dayOfMonth)
        }

        return PrayerTimes(
            fajr = fajrStr,
            sunrise = sunriseStr,
            dhuhr = dhuhrStr,
            asr = asrStr,
            maghrib = maghribStr,
            isha = ishaStr,
            fajrMillis = toEpochMillis(baseCal, times.fajr),
            sunriseMillis = toEpochMillis(baseCal, times.sunrise),
            dhuhrMillis = toEpochMillis(baseCal, times.dhuhr),
            asrMillis = toEpochMillis(baseCal, times.asr),
            maghribMillis = toEpochMillis(baseCal, times.maghrib),
            ishaMillis = toEpochMillis(baseCal, times.isha)
        )
    }

    fun calculateExtraNightTimes(
        date: LocalDate = LocalDate.now(),
        latitude: Double,
        longitude: Double,
        method: CalculationMethod = CalculationMethod.EGYPTIAN,
        timeZoneOffsetHours: Double = getTimezoneOffsetHours(latitude, longitude, date)
    ): Pair<String, String> {
        val julianDate = getJulianDate(date.year, date.monthValue, date.dayOfMonth)
        val times = computeDayTimes(julianDate, latitude, longitude, timeZoneOffsetHours, method)
        return Pair(formatTime12h(times.lastThirdOfNight), formatTime12h(times.imsak))
    }

    private fun computeDayTimes(
        jd: Double,
        lat: Double,
        lng: Double,
        timezone: Double,
        method: CalculationMethod
    ): AstronomicalTimes {
        val d = jd - 2451545.0
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = fixAngle(q + 1.915 * sin(degToRad(g)) + 0.020 * sin(degToRad(2 * g)))

        val e = 23.439 - 0.00000036 * d
        val ra = radToDeg(atan2(cos(degToRad(e)) * sin(degToRad(l)), cos(degToRad(l)))) / 15.0
        val dhuhr = fixHour(12.0 + timezone - (lng / 15.0) - equationOfTime(q, ra))

        val declination = radToDeg(asin(sin(degToRad(e)) * sin(degToRad(l))))

        // Sunrise & Sunset (center of sun 0.833 deg below horizon)
        val sunAlt = -0.8333
        val sunriseHourDiff = sunAngleTime(sunAlt, lat, declination)
        val sunrise = fixHour(dhuhr - sunriseHourDiff)
        val sunset = fixHour(dhuhr + sunriseHourDiff)

        // Fajr
        val fajrHourDiff = sunAngleTime(-method.fajrAngle, lat, declination)
        val fajr = fixHour(dhuhr - fajrHourDiff)

        // Asr (Shafi'i: shadow = 1)
        val asrAlt = radToDeg(atan(1.0 / (1.0 + tan(degToRad(abs(lat - declination))))))
        val asrHourDiff = sunAngleTime(asrAlt, lat, declination)
        val asr = fixHour(dhuhr + asrHourDiff)

        // Maghrib
        val maghrib = sunset

        // Isha
        val isha = if (method.ishaIntervalMin > 0) {
            fixHour(maghrib + (method.ishaIntervalMin / 60.0))
        } else {
            val ishaHourDiff = sunAngleTime(-method.ishaAngle, lat, declination)
            fixHour(dhuhr + ishaHourDiff)
        }

        // Night duration: from sunset to next sunrise (~ 12h default approximation)
        val nightDuration = fixHour(fajr + 24.0 - sunset)
        val lastThirdOfNight = fixHour(sunset + (nightDuration * (2.0 / 3.0)))
        val midnight = fixHour(sunset + (nightDuration / 2.0))
        val imsak = fixHour(fajr - (10.0 / 60.0))

        return AstronomicalTimes(
            fajr = fajr,
            sunrise = sunrise,
            dhuhr = dhuhr,
            asr = asr,
            maghrib = maghrib,
            isha = isha,
            imsak = imsak,
            lastThirdOfNight = lastThirdOfNight,
            midnight = midnight
        )
    }

    private fun equationOfTime(q: Double, ra: Double): Double {
        return (q / 15.0) - fixHour(ra)
    }

    private fun sunAngleTime(angle: Double, lat: Double, declination: Double): Double {
        val cosH = (sin(degToRad(angle)) - sin(degToRad(lat)) * sin(degToRad(declination))) /
                (cos(degToRad(lat)) * cos(degToRad(declination)))
        val clamped = cosH.coerceIn(-1.0, 1.0)
        return radToDeg(acos(clamped)) / 15.0
    }

    private fun getJulianDate(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    private fun degToRad(d: Double): Double = d * Math.PI / 180.0
    private fun radToDeg(r: Double): Double = r * 180.0 / Math.PI

    private fun fixHour(h: Double): Double {
        var res = h - 24.0 * floor(h / 24.0)
        if (res < 0) res += 24.0
        return res
    }

    private fun fixAngle(a: Double): Double {
        var res = a - 360.0 * floor(a / 360.0)
        if (res < 0) res += 360.0
        return res
    }

    fun formatTime12h(hoursDecimal: Double): String {
        val totalMinutes = (hoursDecimal * 60).toInt() % 1440
        var h = totalMinutes / 60
        val m = totalMinutes % 60
        val suffix = if (h >= 12) "م" else "ص"
        if (h > 12) h -= 12
        if (h == 0) h = 12
        return String.format("%02d:%02d %s", h, m, suffix)
    }

    private fun toEpochMillis(baseCal: Calendar, hoursDecimal: Double): Long {
        val totalMinutes = (hoursDecimal * 60).toInt()
        val h = totalMinutes / 60
        val m = totalMinutes % 60
        val cal = baseCal.clone() as Calendar
        cal.set(Calendar.HOUR_OF_DAY, h % 24)
        cal.set(Calendar.MINUTE, m)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
