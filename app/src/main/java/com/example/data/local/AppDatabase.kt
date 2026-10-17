package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        TasbihEntity::class,
        PrayerLogEntity::class,
        QuranBookmarkEntity::class,
        AthkarProgressEntity::class,
        PrayerTimesCacheEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noorDao(): NoorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS prayer_times_cache (
                        cacheKey TEXT NOT NULL PRIMARY KEY,
                        date TEXT NOT NULL,
                        latitude REAL NOT NULL,
                        longitude REAL NOT NULL,
                        method INTEGER NOT NULL,
                        timezone TEXT NOT NULL,
                        fajr TEXT NOT NULL,
                        sunrise TEXT NOT NULL,
                        dhuhr TEXT NOT NULL,
                        asr TEXT NOT NULL,
                        maghrib TEXT NOT NULL,
                        isha TEXT NOT NULL,
                        imsak TEXT NOT NULL,
                        lastThird TEXT NOT NULL,
                        hijriFormatted TEXT NOT NULL,
                        timestamp INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "noor_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
