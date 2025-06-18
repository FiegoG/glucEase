package com.example.uijp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uijp.data.model.MissionDto
import com.example.uijp.data.model.MissionItemUiState
import com.example.uijp.data.network.MissionApiService
import com.example.uijp.data.network.RetrofitClient
import com.example.uijp.data.repository.MissionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MissionViewModel(private val missionRepository: MissionRepository) : ViewModel() {

    // State umum untuk loading dan error (bisa dibuat lebih spesifik jika perlu)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Untuk daftar misi di GamifikasiUI
    val missions: StateFlow<List<MissionItemUiState>> = missionRepository.getAllMissions()
        .map { entities ->
            // Ubah List<MissionEntity> menjadi List<MissionItemUiState>
            entities.map { entity ->
                MissionItemUiState(
                    id = entity.id,
                    title = entity.title,
                    progress = entity.progress,
                    isDone = entity.progress >= 100
                )
            }
        }
        .catch { e ->
            _errorMessage.value = "Error reading from database: ${e.message}"
            emit(emptyList()) // Emit list kosong jika ada error
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Untuk detail misi yang dipilih
    private val _selectedMissionId = MutableStateFlow<String?>(null)
    private val _selectedMissionDetail = MutableStateFlow<MissionDto?>(null) // Tetap pakai DTO atau buat UI State baru
    val selectedMissionDetail: StateFlow<MissionDto?> = _selectedMissionDetail.asStateFlow()



    init {
//         Bisa dipanggil jika halaman gamifikasi adalah halaman pertama yang butuh misi
         fetchUserMissions()
    }

    fun fetchUserMissions() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                missionRepository.refreshMissions()
            } catch (e: Exception) {
                Log.e("MissionViewModel", "Exception during refresh", e)
                _errorMessage.value = "Failed to refresh missions: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
//        viewModelScope.launch {
//            Log.d("MissionViewModel", "fetchUserMissions called. Current missions count: ${missions.value.size}")
//            _isLoading.value = true
//            _errorMessage.value = null
//            try {
//                Log.d("MissionViewModel", "Attempting to call missionApiService.getUserMissions()")
//                val response = missionApiService.getUserMissions()
//                Log.d("MissionViewModel", "Response received. Code: ${response.code()}, Successful: ${response.isSuccessful}")
//
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    Log.d("MissionViewModel", "Raw response body: $responseBody") // Log the whole body
//                    val missionDtos = responseBody?.data?.missions ?: emptyList()
//                    Log.d("MissionViewModel", "Parsed DTOs count: ${missionDtos.size}")
//                    if (missionDtos.isNotEmpty()) {
//                        Log.d("MissionViewModel", "First DTO: ${missionDtos.first()}")
//                    }
//                    _missions.value = missionDtos.map { dto ->
//                        MissionItemUiState(
//                            id = dto.id,
//                            title = dto.title,
//                            progress = dto.progress,
//                            isDone = dto.progress >= 100
//                        )
//                    }
//                    Log.d("MissionViewModel", "Updated _missions StateFlow. New count: ${_missions.value.size}")
//                } else {
//                    val errorBody = response.errorBody()?.string() // Read error body
//                    Log.e("MissionViewModel", "API error: ${response.code()} - ${response.message()}. Error body: $errorBody")
//                    _errorMessage.value = "Error fetching missions: ${response.code()} - ${response.message()}"
//                }
//            } catch (e: Exception) {
//                Log.e("MissionViewModel", "Network/Exception in fetchUserMissions", e)
//                _errorMessage.value = "Network error fetching missions: ${e.localizedMessage}"
//            } finally {
//                _isLoading.value = false
//                Log.d("MissionViewModel", "fetchUserMissions finished. isLoading: ${_isLoading.value}")
//            }
//        }
    }

    fun fetchMissionDetail(missionId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _selectedMissionDetail.value = null

            // Refresh data dari network terlebih dahulu
            missionRepository.refreshMissionDetail(missionId)

            // Ambil data dari database lokal (yang sudah di-update)
            missionRepository.getMissionDetail(missionId).collect { entity ->
                if (entity != null) {
                    // Mapping dari Entity ke Dto atau UI State Model.
                    // Untuk sementara, kita buat DTO palsu dari entity agar tidak banyak mengubah UI.
                    _selectedMissionDetail.value = MissionDto(
                        id = entity.id, title = entity.title, description = entity.description,
                        rewardType = entity.rewardType, rewardValue = entity.rewardValue,
                        targetValue = entity.targetValue, missionLogicType = entity.missionLogicType,
                        triggerEventKey = entity.triggerEventKey, pointReward = entity.pointReward,
                        createdAt = entity.createdAt, progress = entity.progress, status = entity.status
                    )
                } else {
                    _errorMessage.value = "Mission with ID $missionId not found."
                }
                _isLoading.value = false // Set loading false setelah data diterima
            }
        }

//        viewModelScope.launch {
//            _isLoading.value = true
//            _errorMessage.value = null
//            _selectedMissionDetail.value = null // Reset dulu detail sebelumnya
//            try {
//                val response = missionApiService.getMissionDetail(missionId)
//                if (response.isSuccessful) {
//                    _selectedMissionDetail.value = response.body()?.data
//                    if (_selectedMissionDetail.value == null && response.body()?.success == true) {
//                        _errorMessage.value = "Mission detail data is empty."
//                    } else if (response.body()?.success == false) {
//                        _errorMessage.value = response.body()?.message ?: "Failed to get mission detail."
//                    }
//                } else {
//                    if (response.code() == 404) {
//                        _errorMessage.value = "Misi tidak ditemukan (Error 404)."
//                    } else {
//                        _errorMessage.value = "Error fetching mission detail: ${response.code()} - ${response.message()}"
//                    }
//                }
//            } catch (e: Exception) {
//                _errorMessage.value = "Network error fetching mission detail: ${e.localizedMessage}"
//                Log.e("MissionViewModel", "fetchMissionDetail Exception: ", e)
//            } finally {
//                _isLoading.value = false
//            }
//        }
    }

    // Panggil ini saat ViewModel tidak lagi digunakan (misal di onCleared atau saat meninggalkan screen)
    fun clearSelectedMissionDetail() {
        _selectedMissionDetail.value = null
        _errorMessage.value = null // Juga bersihkan error message terkait detail
    }
}
