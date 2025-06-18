package com.example.uijp.data.repository

import android.util.Log
import com.example.uijp.data.local.dao.BloodSugarDao
import com.example.uijp.data.model.AddBloodSugarRequest
import com.example.uijp.data.model.ApiResponse
import com.example.uijp.data.model.BloodSugarRecord
import com.example.uijp.data.model.DashboardResponse
import com.example.uijp.data.network.BloodSugarApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class BloodSugarRepository(
    private val apiService: BloodSugarApiService,
    private val bloodSugarDao: BloodSugarDao
) {

    // ViewModel akan meng-observe Flow ini untuk mendapatkan data dashboard.
    // Data berasal dari API, kemudian disimpan ke Room, tapi presentasi ke UI
    // dalam kasus ini bisa langsung dari API karena data dashboard (chart, summary)
    // bersifat agregat dan tidak disimpan langsung di tabel BloodSugarRecord.
    fun getDashboardData(): Flow<Result<DashboardResponse>> = flow {
        try {
            val response = apiService.getDashboardData()
            if (response.isSuccessful && response.body()?.success == true) {
                val dashboardData = response.body()?.data
                if (dashboardData != null) {
                    // Simpan recentHistory ke database lokal
                    bloodSugarDao.insertAll(dashboardData.recentHistory)
                    // Emit data yang berhasil didapat
                    emit(Result.success(dashboardData))
                } else {
                    emit(Result.failure(Exception("Dashboard data is null")))
                }
            } else {
                emit(Result.failure(Exception(response.body()?.message ?: "Failed to fetch dashboard data")))
            }
        } catch (e: HttpException) {
            emit(Result.failure(e))
        } catch (e: IOException) {
            emit(Result.failure(Exception("Network error. Please check your connection.")))
        }
    }

    // ViewModel akan memanggil fungsi ini untuk menambahkan record baru
    suspend fun addBloodSugarRecord(level: Int, date: String, time: String): Result<BloodSugarRecord> {
        return try {
            val request = AddBloodSugarRequest(level, date, time)
            val response = apiService.addRecord(request)

            if (response.isSuccessful && response.body()?.success == true) {
                val newRecord = response.body()?.data
                if (newRecord != null) {
                    // Jika sukses di API, simpan juga ke database lokal
                    bloodSugarDao.insertAll(listOf(newRecord))
                    Result.success(newRecord)
                } else {
                    Result.failure(Exception("Failed to add record: Data is null"))
                }
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to add record"))
            }
        } catch (e: Exception) {
            Log.e("Repository", "Error adding record", e)
            Result.failure(e)
        }
    }

    // Contoh untuk mengambil data riwayat dari database lokal.
    // ViewModel akan meng-observe Flow ini. UI akan selalu update.
    val recentHistory: Flow<List<BloodSugarRecord>> = bloodSugarDao.getRecentHistory(20)

    val allHistory: Flow<List<BloodSugarRecord>> = bloodSugarDao.getAllHistory()

}