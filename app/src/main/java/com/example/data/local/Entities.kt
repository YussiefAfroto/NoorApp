package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasbih_counters")
data class TasbihEntity(
    @PrimaryKey val id: String,
    val arabicText: String,
    val transliteration: String,
    val englishMeaning: String,
    val count: Int = 0,
    val target: Int = 33,
    val totalLifetimeCount: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "prayer_logs")
data class PrayerLogEntity(
    @PrimaryKey val id: String,
    val date: String,
    val prayerKey: String,
    val isCompleted: Boolean = false,
    val completedTimestamp: Long = 0L
)

@Entity(tableName = "quran_bookmarks")
data class QuranBookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val surahNumber: Int,
    val surahNameArabic: String,
    val surahNameEnglish: String,
    val ayahNumber: Int,
    val arabicText: String,
    val englishText: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "athkar_progress")
data class AthkarProgressEntity(
    @PrimaryKey val id: String,
    val athkarId: String,
    val currentCount: Int = 0,
    val targetCount: Int = 1,
    val isCompleted: Boolean = false,
    val date: String
)

@Entity(tableName = "prayer_times_cache")
data class PrayerTimesCacheEntity(
    @PrimaryKey val cacheKey: String,
    val date: String,
    val latitude: Double,
    val longitude: Double,
    val method: Int,
    val timezone: String,
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val imsak: String,
    val lastThird: String,
    val hijriFormatted: String,
    val timestamp: Long = System.currentTimeMillis()
)
