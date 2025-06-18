package com.example.uijp.viewmodel
// 4. ViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uijp.data.auth.AuthTokenManager
import com.example.uijp.data.model.AddBloodSugarRequest
import com.example.uijp.data.model.BloodSugarRecord
import com.example.uijp.data.model.ChartDataItem
import com.example.uijp.data.model.SummaryData
import com.example.uijp.data.network.BloodSugarApiService
import com.example.uijp.data.network.RetrofitClient
import com.example.uijp.data.repository.BloodSugarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response

data class BloodSugarUiState(
    val isLoading: Boolean = false,
    val chartData: List<ChartDataItem> = emptyList(),
    val recentHistory: List<BloodSugarRecord> = emptyList(),
    val allHistory: List<BloodSugarRecord> = emptyList(),
    val summary: SummaryData? = null,
    val errorMessage: String? = null,
    val isAddingRecord: Boolean = false,
    val addSuccess: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val currentUserId: String? = null
)

class BloodSugarViewModel(
    private val repository: BloodSugarRepository,
    private val authTokenManager: AuthTokenManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BloodSugarUiState())
    val uiState: StateFlow<BloodSugarUiState> = _uiState.asStateFlow()

        private val apiService = RetrofitClient.bloodSugarApiService

    init {
//        RetrofitClient.initialize(context)
        checkUserAuth()
        if (authTokenManager.isLoggedIn()) {
            loadDashboardData()
            observeLocalHistory()
        }
    }

    private fun observeLocalHistory() {
        viewModelScope.launch {
            repository.recentHistory.collect { localRecentHistory ->
                // Update UI State dengan data dari database lokal
                _uiState.update { it.copy(recentHistory = localRecentHistory) }
            }
        }
    }

    private fun checkUserAuth() {
        val isLoggedIn = authTokenManager.isLoggedIn()
        val userId = authTokenManager.getUserId()

        _uiState.value = _uiState.value.copy(
            isUserLoggedIn = isLoggedIn,
            currentUserId = userId
        )
    }

    fun loadDashboardData() {
        if (!authTokenManager.isLoggedIn()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Please login first to view your blood sugar data"
            )
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            repository.getDashboardData().collect { result ->
                result.onSuccess { dashboardData ->
                    // Data dari API (termasuk chart & summary) digunakan untuk update UI.
                    // recentHistory dari API sudah disimpan ke Room oleh repository,
                    // dan `observeLocalHistory` akan otomatis menangkap perubahannya.
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            chartData = dashboardData.chartData,
                            summary = dashboardData.summary
                        )
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to refresh data: ${error.message}"
                        )
                    }
                }
            }
        }

//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
//
//            try {
//                val response = apiService.getDashboardData()
//
//                if (response.isSuccessful) {
//                    val apiResponse = response.body()
//                    if (apiResponse?.success == true && apiResponse.data != null) {
//                        _uiState.value = _uiState.value.copy(
//                            isLoading = false,
//                            chartData = apiResponse.data.chartData,
//                            recentHistory = apiResponse.data.recentHistory,
//                            summary = apiResponse.data.summary,
//                            errorMessage = null
//                        )
//                    } else {
//                        _uiState.value = _uiState.value.copy(
//                            isLoading = false,
//                            errorMessage = apiResponse?.message ?: "Failed to load data"
//                        )
//                    }
//                } else {
//                    handleApiError(response.code())
//                }
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    isLoading = false,
//                    errorMessage = "Network error: ${e.message}"
//                )
//            }
//        }
    }

    fun loadHistory(page: Int = 1, limit: Int = 20) {
        if (!authTokenManager.isLoggedIn()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Please login first to view your history"
            )
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            repository.getDashboardData().collect { result ->
                result.onSuccess { dashboardData ->
                    // Data dari API (termasuk chart & summary) digunakan untuk update UI.
                    // recentHistory dari API sudah disimpan ke Room oleh repository,
                    // dan `observeLocalHistory` akan otomatis menangkap perubahannya.
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            chartData = dashboardData.chartData,
                            summary = dashboardData.summary
                        )
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to refresh data: ${error.message}"
                        )
                    }
                }
            }
        }

//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(isLoading = true)
//
//            try {
//                val response = apiService.getHistory(page, limit)
//
//                if (response.isSuccessful) {
//                    val apiResponse = response.body()
//                    if (apiResponse?.success == true && apiResponse.data != null) {
//                        _uiState.value = _uiState.value.copy(
//                            isLoading = false,
//                            allHistory = apiResponse.data.records,
//                            errorMessage = null
//                        )
//                    } else {
//                        _uiState.value = _uiState.value.copy(
//                            isLoading = false,
//                            errorMessage = apiResponse?.message ?: "Failed to load history"
//                        )
//                    }
//                } else {
//                    handleApiError(response.code())
//                }
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    isLoading = false,
//                    errorMessage = "Network error: ${e.message}"
//                )
//            }
//        }
    }

    fun addBloodSugarRecord(level: Int, date: String, time: String) {
        if (!authTokenManager.isLoggedIn()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Please login first to add records"
            )
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isAddingRecord = true, addSuccess = false) }

            val result = repository.addBloodSugarRecord(level, date, time)

            result.onSuccess {
                _uiState.update { it.copy(isAddingRecord = false, addSuccess = true) }
                // Data tidak perlu di-refresh manual. Karena record baru sudah
                // ditambahkan ke Room oleh repository, `observeLocalHistory`
                // akan otomatis memperbarui `recentHistory` di UI.
                // Kita hanya perlu me-refresh data agregat seperti chart dan summary.
                loadDashboardData()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isAddingRecord = false,
                        errorMessage = "Failed to add record: ${error.message}"
                    )
                }
            }
        }

//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(isAddingRecord = true, errorMessage = null)
//
//            try {
//                val request = AddBloodSugarRequest(level, date, time)
//                val response = apiService.addRecord(request)
//
//                if (response.isSuccessful) {
//                    val apiResponse = response.body()
//                    if (apiResponse?.success == true) {
//                        _uiState.value = _uiState.value.copy(
//                            isAddingRecord = false,
//                            addSuccess = true,
//                            errorMessage = null
//                        )
//                        // Refresh data after adding
//                        loadDashboardData()
//                    } else {
//                        _uiState.value = _uiState.value.copy(
//                            isAddingRecord = false,
//                            errorMessage = apiResponse?.message ?: "Failed to add record"
//                        )
//                    }
//                } else {
//                    handleApiError(response.code())
//                }
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    isAddingRecord = false,
//                    errorMessage = "Network error: ${e.message}"
//                )
//            }
//        }
    }

    private fun handleApiError(code: Int) {
        val errorMessage = when (code) {
            401 -> {
                // Token expired or invalid, logout user
                authTokenManager.clearAuthData()
                checkUserAuth()
                "Session expired. Please login again."
            }
            403 -> "Access denied"
            404 -> "Data not found"
            500 -> "Server error. Please try again later."
            else -> "Error: $code"
        }

        _uiState.value = _uiState.value.copy(
            isLoading = false,
            isAddingRecord = false,
            errorMessage = errorMessage
        )
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun clearAddSuccess() {
        _uiState.value = _uiState.value.copy(addSuccess = false)
    }

    fun refreshData() {
        if (authTokenManager.isLoggedIn()) {
            loadDashboardData()
        }
    }
}