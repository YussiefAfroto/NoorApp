package com.example.data.model

data class PrayerTime(
    val key: String,
    val nameArabic: String,
    val nameEnglish: String,
    val timeFormatted: String, // "05:12 AM"
    val hour24: Int,
    val minute: Int,
    val isNext: Boolean = false,
    val isCompleted: Boolean = false
) {
    val time: String get() = timeFormatted
}

data class PrayerTimes(
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val fajrMillis: Long = 0L,
    val sunriseMillis: Long = 0L,
    val dhuhrMillis: Long = 0L,
    val asrMillis: Long = 0L,
    val maghribMillis: Long = 0L,
    val ishaMillis: Long = 0L
)

data class CityPrayerPreset(
    val cityNameEnglish: String,
    val cityNameArabic: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val qiblaDegree: Float,
    val distanceToKaabaKm: Int
) {
    val qiblaAngleDegrees: Float get() = qiblaDegree
}

data class Ayah(
    val surahNumber: Int,
    val ayahNumber: Int,
    val arabicText: String,
    val translationEnglish: String,
    val tafsirShort: String = "",
    val isBookmarked: Boolean = false
)

data class Surah(
    val number: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val englishMeaning: String,
    val revelationType: String, // "Meccan" / "Medinan"
    val totalVerses: Int,
    val ayahs: List<Ayah> = emptyList()
)

data class AthkarItem(
    val id: String,
    val category: AthkarCategory,
    val titleArabic: String,
    val titleEnglish: String,
    val arabicText: String,
    val transliteration: String,
    val translationEnglish: String,
    val virtue: String,
    val repeatTarget: Int = 1
)

enum class AthkarCategory(val titleArabic: String, val titleEnglish: String) {
    MORNING("أذكار الصباح", "Morning Athkar"),
    EVENING("أذكار المساء", "Evening Athkar"),
    AFTER_PRAYER("أذكار بعد الصلاة", "After Prayer"),
    SLEEP("أذكار النوم", "Before Sleep"),
    QURANIC_DUA("أدعية قرآنية", "Quranic Duas"),
    DAILY_DUAS("أدعية يومية", "Daily Duas"),
    WAKING_UP("أذكار الاستيقاظ", "Waking Up"),
    MOSQUE("أذكار المسجد", "Mosque"),
    HOME("أذكار المنزل", "Home"),
    FOOD("أذكار الطعام والشراب", "Food & Drink"),
    TRAVEL("أذكار السفر", "Travel"),
    WUDU("أذكار الوضوء", "Ablution"),
    DISTRESS("أذكار الكرب والخوف", "Distress & Fear"),
    VARIOUS("أذكار متنوعة", "Various")
}

data class DhikrPreset(
    val id: String,
    val arabicText: String,
    val transliteration: String,
    val englishMeaning: String,
    val defaultTarget: Int = 33
)
