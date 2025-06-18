// file: com/example/uijp/data/repository/WeeklyReportRepository.kt
package com.example.uijp.data.repository

import android.util.Log
import com.example.uijp.data.local.dao.MissionDao
import com.example.uijp.data.local.dao.WeeklyReportDao
import com.example.uijp.data.local.entity.WeeklyReportEntity
import com.example.uijp.data.model.MissionItem
//import com.example.uijp.data.model.toMissionEntity // Kita akan buat fungsi extension ini
import com.example.uijp.data.network.MissionApiService
import com.example.uijp.data.network.WeeklyReportApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeeklyReportRepository(
    private val weeklyReportApiService: WeeklyReportApiService,
    private val missionApiService: MissionApiService,
    private val weeklyReportDao: WeeklyReportDao,
    private val missionDao: MissionDao // Tambahkan missionDao
) {

    // Mengambil Laporan: Selalu dari DAO (sumber lokal)
    // Flow akan otomatis memancarkan data baru jika ada perubahan di DB
    fun getLatestWeeklyReport(): Flow<WeeklyReportEntity?> {
        return weeklyReportDao.getLatestReport()
    }

    // Mengambil Misi: Selalu dari DAO
//    fun getMissions(): Flow<List<MissionItem>> {
//        // Ambil dari DAO, dan map dari MissionEntity ke MissionItem
//        return missionDao.getAllMissions().map { entities ->
//            entities.map { it.toMissionItem() }
//        }
//    }

    // Fungsi untuk sinkronisasi data dari network ke database lokal
    suspend fun refreshWeeklyReport(userId: String) {
        try {
            val response = weeklyReportApiService.getLatestWeeklyReport(userId)
            if (response.isSuccessful) {
                response.body()?.data?.let { data ->
                    // Konversi dari WeeklyReportData (API Model) ke WeeklyReportEntity (DB Model)
                    val reportEntity = WeeklyReportEntity(
                        id = data.reportInfo?.id ?: 0, // Pastikan ada ID
                        reportInfo = data.reportInfo,
                        sugarIntake = data.sugarIntake,
                        bloodSugar = data.bloodSugar
                    )
                    // Hapus data lama dan masukkan data baru
                    weeklyReportDao.clearAll()
                    weeklyReportDao.insertReport(reportEntity)
                }
            } else {
                Log.e("ReportRepository", "Failed to fetch report: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("ReportRepository", "Error fetching report: ${e.message}")
            // Jika gagal, data lama di DB tidak akan dihapus. UI akan tetap menampilkan data lama.
        }
    }

    // Fungsi untuk sinkronisasi misi
//    suspend fun refreshMissions() {
//        try {
//            val response = missionApiService.getUserMissions()
//            if (response.isSuccessful) {
//                response.body()?.data?.let { missionItems ->
//                    // Konversi List<MissionItem> ke List<MissionEntity>
//                    val missionEntities = missionItems.map { it.toMissionEntity() }
//                    missionDao.clearAllMissions() // Hapus misi lama
//                    missionDao.insertAll(missionEntities) // Masukkan misi baru
//                }
//            } else {
//                Log.e("ReportRepository", "Failed to fetch missions: ${response.message()}")
//            }
//        } catch (e: Exception) {
//            Log.e("ReportRepository", "Error fetching missions: ${e.message}")
//        }
//    }
}

// Buat file extension function untuk mapping (misal: di data/model/Mapper.kt)
//fun MissionItem.toMissionEntity(): MissionEntity {
//    return MissionEntity(id, title, description, status, targetValue, currentProgress)
//}
//
//fun MissionEntity.toMissionItem(): MissionItem {
//    return MissionItem(id, title, description, status, targetValue, currentProgress)
//}