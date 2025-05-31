package com.example.uijp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uijp.data.model.*
import com.example.uijp.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FoodTrackerViewModel : ViewModel() {

    private val apiService = RetrofitClient.sugarTrackerApiService

    // State untuk food list di halaman tambah makanan
    private val _foodListState = MutableStateFlow<UiState<List<Food>>>(UiState.Loading)
    val foodListState: StateFlow<UiState<List<Food>>> = _foodListState

    // State untuk loading actions
    private val _isActionLoading = MutableStateFlow(false)
    val isActionLoading: StateFlow<Boolean> = _isActionLoading

    // State untuk message
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // State untuk search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        loadFoodList()
    }

    // Fungsi untuk load daftar makanan
    fun loadFoodList(search: String? = null) {
        viewModelScope.launch {
            _foodListState.value = UiState.Loading
            try {
                val response = apiService.getFoodList(search)
                if (response.isSuccessful && response.body()?.success == true) {
                    val foods = response.body()!!.data.foods
                    _foodListState.value = UiState.Success(foods)
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

    // Fungsi untuk search makanan
    fun searchFoods(query: String) {
        _searchQuery.value = query
        loadFoodList(if (query.isBlank()) null else query)
    }

    // Fungsi untuk menambahkan makanan ke tracker
    fun addFoodToTracker(foodId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isActionLoading.value = true
            try {
                val response = apiService.addFoodToTracker(AddFoodRequest(foodId))
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = response.body()!!.message
                    onSuccess()
                } else {
                    _message.value = response.body()?.message ?: "Gagal menambahkan makanan"
                }
            } catch (e: Exception) {
                _message.value = e.message ?: "Terjadi kesalahan saat menambahkan makanan"
            } finally {
                _isActionLoading.value = false
            }
        }
    }

    // Fungsi untuk clear message
    fun clearMessage() {
        _message.value = null
    }

    // Update search query tanpa memanggil API
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}