// file: com/example/uijp/viewmodel/LaporanMingguanViewModel.kt
package com.example.uijp.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.uijp.data.auth.AuthTokenManager
import com.example.uijp.data.local.AppDatabase
import com.example.uijp.data.local.entity.WeeklyReportEntity
import com.example.uijp.data.model.MissionItem // Atau model Misi Anda dari MissionsResponse
import com.example.uijp.data.model.WeeklyReportData
import com.example.uijp.data.network.MissionApiService
import com.example.uijp.data.network.RetrofitClient
import com.example.uijp.data.network.WeeklyReportApiService
import com.example.uijp.data.repository.WeeklyReportRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WeeklyReportViewModel(
    private val authTokenManager: AuthTokenManager,
    private val repository: WeeklyReportRepository
) : ViewModel() {

    private val _weeklyReportData = mutableStateOf<WeeklyReportEntity?>(null)
    val weeklyReportData: State<WeeklyReportEntity?> = _weeklyReportData

    private val _missions = mutableStateOf<List<MissionItem>>(emptyList())
    val missions: State<List<MissionItem>> = _missions

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    init {
        // Mulai observe data dari Repository saat ViewModel dibuat
        observeReport()
//        observeMissions()
        // Langsung fetch data terbaru dari network
        fetchLatestData()
    }

    private fun fetchLatestData() {
        val userId = authTokenManager.getUserId()
        if (userId == null) {
            _errorMessage.value = "User tidak login."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                // Jalankan refresh report dan mission secara bersamaan
                repository.refreshWeeklyReport(userId)
//                repository.refreshMissions()
            } catch (e: Exception) {
                _errorMessage.value = "Terjadi kesalahan saat sinkronisasi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

//    private fun observeMissions() {
//        viewModelScope.launch {
//            repository.getMissions()
//                .catch { e -> _errorMessage.value = "Gagal memuat misi dari cache: ${e.message}" }
//                .collect { missionItems ->
//                    _missions.value = missionItems
//                }
//        }
//    }

    private fun observeReport() {
        viewModelScope.launch {
            repository.getLatestWeeklyReport()
                .catch { e -> _errorMessage.value = "Gagal memuat data dari cache: ${e.message}" }
                .collect { reportEntity ->
                    _weeklyReportData.value = reportEntity
                }
        }
    }


//    private fun fetchLatestWeeklyReport(userId: String) {
//        viewModelScope.launch {
//            _isLoadingReport.value = true
//            _errorMessage.value = null
//            try {
//                val response = weeklyReportApiService.getLatestWeeklyReport(userId)
//                if (response.isSuccessful) {
//                    _weeklyReportData.value = response.body()?.data
//                    if (response.body()?.data == null && response.body()?.success == true) {
//                        // Laporan berhasil diambil tapi kosong
//                        _weeklyReportData.value = WeeklyReportData(null, null, null) // Data kosong
//                    }
//                } else {
//                    _errorMessage.value = "Gagal memuat laporan: ${response.message()}"
//                    _weeklyReportData.value = WeeklyReportData(null, null, null) // Tetap set data kosong
//                }
//            } catch (e: Exception) {
//                _errorMessage.value = "Error: ${e.message}"
//                _weeklyReportData.value = WeeklyReportData(null, null, null) // Tetap set data kosong
//            } finally {
//                _isLoadingReport.value = false
//            }
//        }
    }

//    private fun fetchUserMissions() {
//        viewModelScope.launch {
//            _isLoadingMissions.value = true
//            // _errorMessage.value = null // Jangan reset error message dari report
//            try {
//                // Asumsi MissionApiService.getUserMissions() mengembalikan Response<MissionsResponse>
//                // Dan MissionsResponse memiliki struktur data: List<MissionModel>
//                // atau jika Anda menggunakan MissionsListResponse dengan MissionItem
//                val response = missionApiService.getUserMissions() // Gunakan fungsi yang ada
//                if (response.isSuccessful) {
//                    // Sesuaikan ini dengan struktur MissionsResponse Anda.
//                    // Jika MissionsResponse.kt memiliki data.missions (List<MissionModel>), contoh:
//                    // _missions.value = response.body()?.data?.missions ?: emptyList()
//                    // Jika Anda ingin menggunakan model MissionItem baru, dan respons API berbeda:
//                    _missions.value = response.body()?.data ?: emptyList() // Asumsi MissionsResponse punya List<MissionItem> di 'data'
//                } else {
//                    if (_errorMessage.value == null) _errorMessage.value = "Gagal memuat misi." // Hanya set jika belum ada error lain
//                }
//            } catch (e: Exception) {
//                if (_errorMessage.value == null) _errorMessage.value = "Error memuat misi: ${e.message}"
//            } finally {
//                _isLoadingMissions.value = false
//            }
//        }
//    }


class LaporanMingguanViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeeklyReportViewModel::class.java)) {
            // Dapatkan instance dari semua dependensi yang dibutuhkan Repository
            val authTokenManager = AuthTokenManager.getInstance(context.applicationContext)
            val db = AppDatabase.getInstance(context.applicationContext)
            val weeklyReportApiService = RetrofitClient.weeklyReportApiService
            val missionApiService = RetrofitClient.missionApiService

            // Buat instance Repository
            val repository = WeeklyReportRepository(
                weeklyReportApiService,
                missionApiService,
                db.weeklyReportDao(),
                db.missionDao()
            )

            // Berikan Repository ke ViewModel
            return WeeklyReportViewModel(authTokenManager, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}