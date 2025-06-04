

// viewmodel/DetailDokterViewModel.kt
package com.example.uijp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.uijp.data.model.DoctorDetail
import com.example.uijp.data.model.DoctorSchedule
import com.example.uijp.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailDokterViewModel(private val doctorId: Int) : ViewModel() {

    private val consultationApiService = RetrofitClient.consultationApiService

    private val _uiState = MutableStateFlow(DetailDokterUiState())
    val uiState: StateFlow<DetailDokterUiState> = _uiState.asStateFlow()

    private val _doctorDetail = MutableStateFlow<DoctorDetail?>(null)
    val doctorDetail: StateFlow<DoctorDetail?> = _doctorDetail.asStateFlow()

    private val _selectedDate = MutableStateFlow("")
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _selectedTime = MutableStateFlow("")
    val selectedTime: StateFlow<String> = _selectedTime.asStateFlow()

    private val _availableSchedules = MutableStateFlow<List<DoctorSchedule>>(emptyList())
    val availableSchedules: StateFlow<List<DoctorSchedule>> = _availableSchedules.asStateFlow()

    init {
        loadDoctorDetail()
    }

    fun loadDoctorDetail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val response = consultationApiService.getDoctorDetail(doctorId)
                if (response.isSuccessful && response.body() != null) {
                    val doctorDetailResponse = response.body()!!
                    if (doctorDetailResponse.success) {
                        _doctorDetail.value = doctorDetailResponse.data
                        _availableSchedules.value = doctorDetailResponse.data.schedules
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = doctorDetailResponse.message
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Gagal memuat detail dokter"
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

    fun selectDate(date: String) {
        _selectedDate.value = date
        // Clear selected time when date changes
        _selectedTime.value = ""
    }

    fun selectTime(time: String) {
        _selectedTime.value = time
    }

    fun isDateSelected(date: String): Boolean {
        return _selectedDate.value == date
    }

    fun isTimeSelected(time: String): Boolean {
        return _selectedTime.value == time
    }

    fun getAvailableTimesForSelectedDate(): List<String> {
        val selectedDate = _selectedDate.value
        if (selectedDate.isEmpty()) return emptyList()

        return _availableSchedules.value
            .filter { it.available_date == selectedDate && it.is_booked == 0 }
            .map { it.available_time }
    }

    fun getAvailableDates(): List<Pair<String, String>> {
        // Group schedules by date and get unique dates
        val uniqueDates = _availableSchedules.value
            .groupBy { it.available_date }
            .keys
            .sorted()

        // Convert to day-date pairs (this is simplified, you might want to use proper date formatting)
        return uniqueDates.mapIndexed { index, date ->
            val dayNames = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
            val dayName = dayNames[index % 7]
            val dayNumber = date.split("-").lastOrNull()?.takeLast(2) ?: "${22 + index}"
            dayName to dayNumber
        }
    }

    fun canProceedToPayment(): Boolean {
        return _selectedDate.value.isNotEmpty() && _selectedTime.value.isNotEmpty()
    }

    fun retryLoadDoctorDetail() {
        loadDoctorDetail()
    }
}

data class DetailDokterUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

class DetailDokterViewModelFactory(private val doctorId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailDokterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailDokterViewModel(doctorId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}