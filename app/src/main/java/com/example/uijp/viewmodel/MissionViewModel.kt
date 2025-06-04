package com.example.uijp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uijp.data.model.MissionDto
import com.example.uijp.data.model.MissionItemUiState
import com.example.uijp.data.network.MissionApiService
import com.example.uijp.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MissionViewModel(private val missionApiService: MissionApiService) : ViewModel() {

    // Untuk daftar misi di GamifikasiUI
    private val _missions = MutableStateFlow<List<MissionItemUiState>>(emptyList())
    val missions: StateFlow<List<MissionItemUiState>> = _missions.asStateFlow()

    // Untuk detail misi yang dipilih
    private val _selectedMissionDetail = MutableStateFlow<MissionDto?>(null)
    val selectedMissionDetail: StateFlow<MissionDto?> = _selectedMissionDetail.asStateFlow()

    // State umum untuk loading dan error (bisa dibuat lebih spesifik jika perlu)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
//         Bisa dipanggil jika halaman gamifikasi adalah halaman pertama yang butuh misi
         fetchUserMissions()
    }

    fun fetchUserMissions() {
        viewModelScope.launch {
            Log.d("MissionViewModel", "fetchUserMissions called. Current missions count: ${missions.value.size}")
            _isLoading.value = true
            _errorMessage.value = null
            try {
                Log.d("MissionViewModel", "Attempting to call missionApiService.getUserMissions()")
                val response = missionApiService.getUserMissions()
                Log.d("MissionViewModel", "Response received. Code: ${response.code()}, Successful: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("MissionViewModel", "Raw response body: $responseBody") // Log the whole body
                    val missionDtos = responseBody?.data?.missions ?: emptyList()
                    Log.d("MissionViewModel", "Parsed DTOs count: ${missionDtos.size}")
                    if (missionDtos.isNotEmpty()) {
                        Log.d("MissionViewModel", "First DTO: ${missionDtos.first()}")
                    }
                    _missions.value = missionDtos.map { dto ->
                        MissionItemUiState(
                            id = dto.id,
                            title = dto.title,
                            progress = dto.progress,
                            isDone = dto.progress >= 100
                        )
                    }
                    Log.d("MissionViewModel", "Updated _missions StateFlow. New count: ${_missions.value.size}")
                } else {
                    val errorBody = response.errorBody()?.string() // Read error body
                    Log.e("MissionViewModel", "API error: ${response.code()} - ${response.message()}. Error body: $errorBody")
                    _errorMessage.value = "Error fetching missions: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                Log.e("MissionViewModel", "Network/Exception in fetchUserMissions", e)
                _errorMessage.value = "Network error fetching missions: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
                Log.d("MissionViewModel", "fetchUserMissions finished. isLoading: ${_isLoading.value}")
            }
        }
    }

    fun fetchMissionDetail(missionId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _selectedMissionDetail.value = null // Reset dulu detail sebelumnya
            try {
                val response = missionApiService.getMissionDetail(missionId)
                if (response.isSuccessful) {
                    _selectedMissionDetail.value = response.body()?.data
                    if (_selectedMissionDetail.value == null && response.body()?.success == true) {
                        _errorMessage.value = "Mission detail data is empty."
                    } else if (response.body()?.success == false) {
                        _errorMessage.value = response.body()?.message ?: "Failed to get mission detail."
                    }
                } else {
                    if (response.code() == 404) {
                        _errorMessage.value = "Misi tidak ditemukan (Error 404)."
                    } else {
                        _errorMessage.value = "Error fetching mission detail: ${response.code()} - ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error fetching mission detail: ${e.localizedMessage}"
                Log.e("MissionViewModel", "fetchMissionDetail Exception: ", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Panggil ini saat ViewModel tidak lagi digunakan (misal di onCleared atau saat meninggalkan screen)
    fun clearSelectedMissionDetail() {
        _selectedMissionDetail.value = null
        _errorMessage.value = null // Juga bersihkan error message terkait detail
    }
}
