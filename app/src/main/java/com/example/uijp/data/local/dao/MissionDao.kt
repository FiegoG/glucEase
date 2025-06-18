package com.example.uijp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uijp.data.local.entity.MissionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MissionDao {

    // Mengambil semua misi sebagai Flow. UI akan otomatis update jika data di tabel ini berubah.
    @Query("SELECT * FROM missions")
    fun getAllMissions(): Flow<List<MissionEntity>>

    // Mengambil satu misi berdasarkan ID. Berguna untuk halaman detail.
    @Query("SELECT * FROM missions WHERE id = :missionId")
    fun getMissionById(missionId: String): Flow<MissionEntity?>

    // Memasukkan daftar misi. Jika ada misi dengan ID yang sama, akan diganti.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(missions: List<MissionEntity>)

    // Menghapus semua misi dari tabel. Berguna saat refresh data.
    @Query("DELETE FROM missions")
    suspend fun clearAll()
}