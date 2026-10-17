package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoorDao {

    // --- Tasbih ---
    @Query("SELECT * FROM tasbih_counters ORDER BY lastUpdated DESC")
    fun getAllTasbih(): Flow<List<TasbihEntity>>

    @Query("SELECT * FROM tasbih_counters WHERE id = :id")
    suspend fun getTasbihById(id: String): TasbihEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasbih(tasbih: TasbihEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllTasbih(list: List<TasbihEntity>)

    @Query("UPDATE tasbih_counters SET count = :count, totalLifetimeCount = totalLifetimeCount + 1, lastUpdated = :timestamp WHERE id = :id")
    suspend fun incrementTasbih(id: String, count: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE tasbih_counters SET count = 0, lastUpdated = :timestamp WHERE id = :id")
    suspend fun resetTasbih(id: String, timestamp: Long = System.currentTimeMillis())

    // --- Prayer Logs ---
    @Query("SELECT * FROM prayer_logs WHERE date = :date")
    fun getPrayerLogsForDate(date: String): Flow<List<PrayerLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerLog(log: PrayerLogEntity)

    // --- Quran Bookmarks ---
    @Query("SELECT * FROM quran_bookmarks ORDER BY createdAt DESC")
    fun getAllBookmarks(): Flow<List<QuranBookmarkEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM quran_bookmarks WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber)")
    fun isAyahBookmarked(surahNumber: Int, ayahNumber: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: QuranBookmarkEntity)

    @Query("DELETE FROM quran_bookmarks WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber")
    suspend fun deleteBookmark(surahNumber: Int, ayahNumber: Int)

    @Query("DELETE FROM quran_bookmarks WHERE id = :id")
    suspend fun deleteBookmarkById(id: Long)

    // --- Athkar Progress ---
    @Query("SELECT * FROM athkar_progress WHERE date = :date")
    fun getAthkarProgressForDate(date: String): Flow<List<AthkarProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthkarProgress(progress: AthkarProgressEntity)

    @Query("UPDATE athkar_progress SET currentCount = :count, isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateAthkarCount(id: String, count: Int, isCompleted: Boolean)

    // --- Prayer Times Cache ---
    @Query("SELECT * FROM prayer_times_cache WHERE cacheKey = :cacheKey LIMIT 1")
    suspend fun getPrayerTimesCache(cacheKey: String): PrayerTimesCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerTimesCache(cache: PrayerTimesCacheEntity)
}
