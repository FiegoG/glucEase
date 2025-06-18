package com.example.uijp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.uijp.data.local.dao.BloodSugarDao
import com.example.uijp.data.local.dao.SugarTrackerDao
import com.example.uijp.data.local.entity.ConsumedFoodEntity
import com.example.uijp.data.local.entity.DailySummaryEntity

@Database(
    entities = [DailySummaryEntity::class, ConsumedFoodEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sugarTrackerDao(): SugarTrackerDao
    abstract fun bloodSugarDao(): BloodSugarDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sugar_tracker_database"
                )
                    .fallbackToDestructiveMigration() // Hati-hati: ini akan menghapus DB jika skema berubah
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}