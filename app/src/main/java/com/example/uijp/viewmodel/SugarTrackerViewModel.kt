package com.example.uijp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uijp.data.model.*
import com.example.uijp.data.network.RetrofitClient
import com.example.uijp.data.repository.SugarTrackerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SugarTrackerViewModel(
    private val repository: SugarTrackerRepository
) : ViewModel() {

    private val apiService = RetrofitClient.sugarTrackerApiService

    // State untuk daily tracker
    private val _dailyTrackerState = MutableStateFlow<UiState<SugarTrackerData>>(UiState.Loading)
    val dailyTrackerState: StateFlow<UiState<SugarTrackerData>> = _dailyTrackerState

    // State untuk food list
    private val _foodListState = MutableStateFlow<UiState<List<Food>>>(UiState.Loading)
    val foodListState: StateFlow<UiState<List<Food>>> = _foodListState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // State untuk loading actions
    private val _isActionLoading = MutableStateFlow(false)
    val isActionLoading: StateFlow<Boolean> = _isActionLoading

    // State untuk message
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init {
        loadDailyTracker()
    }

    fun loadDailyTracker(date: String? = null) {
        viewModelScope.launch {
            // Cukup panggil repository, ia yang akan handle loading, success, error
            repository.getDailyTracker(date).collectLatest { uiState ->
                _dailyTrackerState.value = uiState
            }
        }
    }

    fun loadFoodList(search: String? = null) {
        viewModelScope.launch {
            _foodListState.value = UiState.Loading
            try {
                val response = apiService.getFoodList(search)
                if (response.isSuccessful && response.body()?.success == true) {
                    _foodListState.value = UiState.Success(response.body()!!.data.foods)
                } else {
                    _foodListState.value = UiState.Error(
                        response.body()?.message ?: "Gagal memuat daftar makanan"
                    )
                }
            } catch (e: Exception) {
                _foodListState.value = UiState.Error(
                    e.message ?: "Terjadi kesalahan saat memuat daftar makanan"
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchFoods(query: String) {
        _searchQuery.value = query
        loadFoodList(if (query.isBlank()) null else query)
    }

    fun addFoodToTracker(foodId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isActionLoading.value = true
            try {
                val response = repository.addFoodToTracker(foodId)
                if (response.success) {
                    _message.value = response.message
                    // Refresh daily tracker setelah menambah makanan
                    loadDailyTracker()
                    onSuccess()
                } else {
                    _message.value = response.message ?: "Gagal menambahkan makanan"
                }
            } catch (e: Exception) {
                _message.value = e.message ?: "Terjadi kesalahan saat menambahkan makanan"
            } finally {
                _isActionLoading.value = false
            }
        }
    }

    fun updateFoodQuantity(intakeId: Int, quantity: Int) {
        viewModelScope.launch {
            _isActionLoading.value = true
            try {
                val response = repository.updateFoodQuantity(intakeId, quantity)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Quantity berhasil diperbarui"
                    // Refresh daily tracker setelah update quantity
                    loadDailyTracker()
                } else {
                    _message.value = response.body()?.message ?: "Gagal memperbarui quantity"
                }
            } catch (e: Exception) {
                _message.value = e.message ?: "Terjadi kesalahan saat memperbarui quantity"
            } finally {
                _isActionLoading.value = false
            }
        }
    }

    fun removeFoodFromTracker(intakeId: Int) {
        viewModelScope.launch {
            _isActionLoading.value = true
            try {
                val response = repository.removeFoodFromTracker(intakeId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Makanan berhasil dihapus"
                    // Refresh daily tracker setelah hapus makanan
                    loadDailyTracker()
                } else {
                    _message.value = response.body()?.message ?: "Gagal menghapus makanan"
                }
            } catch (e: Exception) {
                _message.value = e.message ?: "Terjadi kesalahan saat menghapus makanan"
            } finally {
                _isActionLoading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}