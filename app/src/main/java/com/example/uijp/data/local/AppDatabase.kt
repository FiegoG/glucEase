package com.example.uijp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.uijp.data.local.dao.ArticleDao
import com.example.uijp.data.local.dao.BloodSugarDao
import com.example.uijp.data.local.dao.MissionDao
import com.example.uijp.data.local.dao.SubscriptionDao
import com.example.uijp.data.local.dao.SugarTrackerDao
import com.example.uijp.data.local.dao.WeeklyReportDao
import com.example.uijp.data.local.entity.ArticleEntity
import com.example.uijp.data.local.entity.BloodSugarRecord
import com.example.uijp.data.local.entity.ConsumedFoodEntity
import com.example.uijp.data.local.entity.DailySummaryEntity
import com.example.uijp.data.local.entity.MissionEntity
import com.example.uijp.data.local.entity.PremiumPackageEntity
import com.example.uijp.data.local.entity.WeeklyReportEntity

@Database(
    entities = [
        DailySummaryEntity::class,
        ConsumedFoodEntity::class,
        BloodSugarRecord::class,
        ArticleEntity::class,
        PremiumPackageEntity::class,
        MissionEntity::class,
        WeeklyReportEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sugarTrackerDao(): SugarTrackerDao
    abstract fun bloodSugarDao(): BloodSugarDao
    abstract fun articleDao(): ArticleDao
    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun missionDao(): MissionDao
    abstract fun weeklyReportDao(): WeeklyReportDao

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
                    .fallbackToDestructiveMigration(true) // Hati-hati: ini akan menghapus DB jika skema berubah
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}