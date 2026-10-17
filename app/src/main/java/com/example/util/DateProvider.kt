package com.example.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField

object DateProvider {

    private val arabicDays = mapOf(
        DayOfWeek.SATURDAY to "السبت",
        DayOfWeek.SUNDAY to "الأحد",
        DayOfWeek.MONDAY to "الإثنين",
        DayOfWeek.TUESDAY to "الثلاثاء",
        DayOfWeek.WEDNESDAY to "الأربعاء",
        DayOfWeek.THURSDAY to "الخميس",
        DayOfWeek.FRIDAY to "الجمعة"
    )

    private val gregorianMonthsArabic = listOf(
        "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
        "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
    )

    private val hijriMonthsArabic = listOf(
        "محرم", "صفر", "ربيع الأول", "ربيع الآخر",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )

    data class FormattedDateInfo(
        val dayNameArabic: String,
        val gregorianFormatted: String, // e.g. "22 سبتمبر 2026"
        val hijriFormatted: String,     // e.g. "10 ربيع الأول 1448 هـ"
        val hijriDay: Int,
        val hijriMonthName: String,
        val hijriYear: Int,
        val gregorianDate: LocalDate
    )

    fun getCurrentDateInfo(targetDate: LocalDate = LocalDate.now()): FormattedDateInfo {
        val dayName = arabicDays[targetDate.dayOfWeek] ?: "اليوم"
        val gMonth = gregorianMonthsArabic.getOrElse(targetDate.monthValue - 1) { "" }
        val gFormatted = "${targetDate.dayOfMonth} $gMonth ${targetDate.year}"

        val (hDay, hMonth, hYear) = try {
            val hijrahDate = HijrahChronology.INSTANCE.date(targetDate)
            val day = hijrahDate.get(ChronoField.DAY_OF_MONTH)
            val month = hijrahDate.get(ChronoField.MONTH_OF_YEAR)
            val year = hijrahDate.get(ChronoField.YEAR)
            Triple(day, month, year)
        } catch (_: Exception) {
            // Algorithmic fallback using Umm Al-Qura Julian Day conversion
            convertGregorianToHijri(targetDate)
        }

        val hMonthName = hijriMonthsArabic.getOrElse(hMonth - 1) { "هـ" }
        val hFormatted = "$hDay $hMonthName $hYear هـ"

        return FormattedDateInfo(
            dayNameArabic = dayName,
            gregorianFormatted = gFormatted,
            hijriFormatted = hFormatted,
            hijriDay = hDay,
            hijriMonthName = hMonthName,
            hijriYear = hYear,
            gregorianDate = targetDate
        )
    }

    /**
     * Accurate astronomical arithmetic conversion from Gregorian to Hijri
     */
    private fun convertGregorianToHijri(date: LocalDate): Triple<Int, Int, Int> {
        val year = date.year
        val month = date.monthValue
        val day = date.dayOfMonth

        // Julian Day Calculation
        val a = (14 - month) / 12
        val y = year + 4800 - a
        val m = month + 12 * a - 3
        var jd = day + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045

        // Convert Julian Day to Hijri
        val l = jd - 1948440 + 10632
        val n = (l - 1) / 10631
        val l2 = l - 10631 * n + 354
        val j = ((10985 - l2) / 5316) * ((50 * l2) / 17719) + (l2 / 5670) * ((43 * l2) / 15238)
        val l3 = l2 - ((30 - j) / 15) * ((17719 * j) / 50) - (j / 16) * ((15238 * j) / 43) + 29
        val hMonth = (24 * l3) / 709
        val hDay = l3 - (709 * hMonth) / 24
        val hYear = 30 * n + j - 30

        return Triple(hDay.coerceIn(1, 30), hMonth.coerceIn(1, 12), hYear)
    }
}
