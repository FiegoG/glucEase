// viewmodel/ConsultationViewModel.kt
package com.example.uijp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.uijp.data.model.Doctor
import com.example.uijp.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConsultationViewModel : ViewModel() {

    private val consultationApiService = RetrofitClient.consultationApiService

    private val _uiState = MutableStateFlow(ConsultationUiState())
    val uiState: StateFlow<ConsultationUiState> = _uiState.asStateFlow()

    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors: StateFlow<List<Doctor>> = _doctors.asStateFlow()

    init {
        loadDoctors()
    }

    fun loadDoctors() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val response = consultationApiService.getAllDoctors()

                if (response.isSuccessful && response.body() != null) {
                    val doctorsResponse = response.body()!!

                    if (doctorsResponse.success) {
                        _doctors.value = doctorsResponse.data
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = doctorsResponse.message
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Gagal memuat data dokter"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Terjadi kesalahan: ${e.message}"
                )
            }
        }
    }

    fun retryLoadDoctors() {
        loadDoctors()
    }
}

data class ConsultationUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

class ConsultationViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConsultationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConsultationViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}