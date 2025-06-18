// file: com/example/uijp/data/local/dao/WeeklyReportDao.kt
package com.example.uijp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uijp.data.local.entity.WeeklyReportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeeklyReportDao {

    // Menggunakan Flow agar UI bisa otomatis update saat data di database berubah
    @Query("SELECT * FROM weekly_report ORDER BY id DESC LIMIT 1")
    fun getLatestReport(): Flow<WeeklyReportEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: WeeklyReportEntity)

    @Query("DELETE FROM weekly_report")
    suspend fun clearAll()
}