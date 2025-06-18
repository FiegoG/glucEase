package com.example.uijp.data.local.dao

import androidx.room.*
import com.example.uijp.data.local.entity.ConsumedFoodEntity
import com.example.uijp.data.local.entity.DailySummaryEntity
import com.example.uijp.data.local.relation.DailyTrackerWithConsumedFoods
import kotlinx.coroutines.flow.Flow

@Dao
interface SugarTrackerDao {

    // Mengambil data tracker lengkap untuk tanggal tertentu.
    // Menggunakan Flow agar UI otomatis update saat data di DB berubah.
    @Transaction
    @Query("SELECT * FROM daily_summary WHERE date = :date")
    fun getDailyTracker(date: String): Flow<DailyTrackerWithConsumedFoods?>

    // Memasukkan atau memperbarui summary harian
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailySummary(summary: DailySummaryEntity)

    // Memasukkan atau memperbarui list makanan yang dikonsumsi
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsumedFoods(foods: List<ConsumedFoodEntity>)

    // Menghapus makanan lama pada tanggal tertentu sebelum memasukkan yang baru
    @Query("DELETE FROM consumed_foods WHERE date = :date")
    suspend fun clearConsumedFoodsByDate(date: String)

    // Wrapper untuk sinkronisasi data dari API
    @Transaction
    suspend fun syncDailyTracker(date: String, summary: DailySummaryEntity, foods: List<ConsumedFoodEntity>) {
        clearConsumedFoodsByDate(date)
        insertDailySummary(summary)
        insertConsumedFoods(foods)
    }
}