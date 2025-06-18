package com.example.uijp.data.repository

import android.util.Log
import com.example.uijp.data.local.dao.MissionDao
import com.example.uijp.data.local.entity.MissionEntity
import com.example.uijp.data.model.MissionDto
import com.example.uijp.data.network.MissionApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Mapper untuk mengubah DTO (dari API) menjadi Entity (untuk Room)
fun MissionDto.toEntity(): MissionEntity {
    return MissionEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        rewardType = this.rewardType,
        rewardValue = this.rewardValue,
        targetValue = this.targetValue,
        missionLogicType = this.missionLogicType,
        triggerEventKey = this.triggerEventKey,
        pointReward = this.pointReward,
        createdAt = this.createdAt,
        progress = this.progress,
        status = this.status
    )
}

class MissionRepository(
    private val missionApiService: MissionApiService,
    private val missionDao: MissionDao
) {

    // Fungsi ini akan menjadi Single Source of Truth untuk daftar misi
    fun getAllMissions(): Flow<List<MissionEntity>> {
        return missionDao.getAllMissions()
    }

    // Fungsi untuk mengambil detail misi dari database lokal
    fun getMissionDetail(missionId: String): Flow<MissionEntity?> {
        return missionDao.getMissionById(missionId)
    }

    // Fungsi untuk menyegarkan data dari network
    suspend fun refreshMissions() {
        try {
            // 1. Ambil data baru dari API
            val response = missionApiService.getUserMissions()
            if (response.isSuccessful) {
                val missionDtos = response.body()?.data?.missions ?: emptyList()

                // 2. Ubah DTO menjadi Entity
                val missionEntities = missionDtos.map { it.toEntity() }

                // 3. Hapus data lama (opsional, tergantung kebutuhan) dan simpan data baru ke Room
                 missionDao.clearAll() // -> Uncomment jika ingin selalu mengganti semua data lama
                missionDao.insertAll(missionEntities)
                Log.d("MissionRepository", "Missions refreshed and saved to DB.")
            } else {
                Log.e("MissionRepository", "API Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("MissionRepository", "Network exception in refreshMissions", e)
        }
    }

    // Fungsi untuk refresh detail misi, jika diperlukan.
    suspend fun refreshMissionDetail(missionId: String) {
        try {
            val response = missionApiService.getMissionDetail(missionId)
            if (response.isSuccessful) {
                response.body()?.data?.let { missionDto ->
                    missionDao.insertAll(listOf(missionDto.toEntity()))
                    Log.d("MissionRepository", "Mission detail for $missionId refreshed and saved.")
                }
            } else {
                Log.e("MissionRepository", "API Error fetching detail: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("MissionRepository", "Network exception in refreshMissionDetail", e)
        }
    }
}