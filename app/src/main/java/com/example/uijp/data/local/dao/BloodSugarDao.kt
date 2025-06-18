package com.example.uijp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uijp.data.model.BloodSugarRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface BloodSugarDao {

    /**
     * Menyimpan daftar record. Jika ada record dengan primary key yang sama,
     * record lama akan diganti dengan yang baru (REPLACE).
     * Ini sangat berguna untuk sinkronisasi.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<BloodSugarRecord>)

    /**
     * Mengambil semua riwayat gula darah, diurutkan dari yang terbaru.
     * Menggunakan Flow agar UI bisa otomatis update saat ada data baru.
     */
    @Query("SELECT * FROM blood_sugar_records ORDER BY created_at DESC")
    fun getAllHistory(): Flow<List<BloodSugarRecord>>

    /**
     * Mengambil riwayat terbaru (misal untuk dashboard).
     * Juga menggunakan Flow.
     */
    @Query("SELECT * FROM blood_sugar_records ORDER BY created_at DESC LIMIT :limit")
    fun getRecentHistory(limit: Int): Flow<List<BloodSugarRecord>>

    /**
     * Menghapus semua data dari tabel.
     * Berguna saat logout atau untuk membersihkan cache.
     */
    @Query("DELETE FROM blood_sugar_records")
    suspend fun clearAll()
}