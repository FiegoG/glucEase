// file: viewmodel/PremiumViewModel.kt
package com.example.uijp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uijp.data.auth.AuthTokenManager
import com.example.uijp.data.model.ApiPremiumPackage
import com.example.uijp.data.model.SubscribePackageRequest
import com.example.uijp.data.model.SubscribePackageResponse
import com.example.uijp.data.network.SubscriptionApiService
import com.example.uijp.data.repository.SubscriptionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class PremiumUiState<out T> {
    object Loading : PremiumUiState<Nothing>()
    data class Success<T>(val data: T) : PremiumUiState<T>()
    data class Error(val message: String) : PremiumUiState<Nothing>()
    object Idle : PremiumUiState<Nothing>()
}

class PremiumViewModel(
    private val subscriptionRepository: SubscriptionRepository,
    private val authTokenManager: AuthTokenManager
) : ViewModel() {

    private val _premiumPackagesState = MutableStateFlow<PremiumUiState<List<ApiPremiumPackage>>>(PremiumUiState.Idle)
    val premiumPackagesState: StateFlow<PremiumUiState<List<ApiPremiumPackage>>> = _premiumPackagesState.asStateFlow()

    private val _selectedPackageState = MutableStateFlow<ApiPremiumPackage?>(null)
    val selectedPackageState: StateFlow<ApiPremiumPackage?> = _selectedPackageState.asStateFlow()

    private val _subscriptionResultState = MutableStateFlow<PremiumUiState<SubscribePackageResponse>>(PremiumUiState.Idle)
    val subscriptionResultState: StateFlow<PremiumUiState<SubscribePackageResponse>> = _subscriptionResultState.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        // Mulai mengamati data dari database
        observePremiumPackages()
        // Minta penyegaran data dari network
        fetchPremiumPackages()
    }

    private fun observePremiumPackages() {
        viewModelScope.launch {
            subscriptionRepository.getPremiumPackages()
                .catch { e ->
                    // Tangani error dari flow (misal: error database)
                    _premiumPackagesState.value = PremiumUiState.Error(e.message ?: "Failed to observe packages")
                    Log.e("PremiumVM", "Observe Packages Error: ${e.message}", e)
                }
                .collect { packages ->
                    // Setiap kali data di DB berubah, state akan di-update
                    _premiumPackagesState.value = PremiumUiState.Success(packages)
                    // Jika belum ada paket yg dipilih dan ada paket dari DB, pilih yg pertama
                    if (_selectedPackageState.value == null && packages.isNotEmpty()) {
                        // Anda bisa pertahankan logika untuk memilih paket default di UI
                        // atau melakukannya di sini.
                    }
                }
        }
    }

    fun fetchPremiumPackages() {
        viewModelScope.launch {
            _premiumPackagesState.value = PremiumUiState.Loading // Tampilkan loading saat menyegarkan
            val errorMessage = subscriptionRepository.refreshPremiumPackages()
            if (errorMessage != null) {
                // Jika refresh gagal, state akan tetap menampilkan data dari cache (dari collector di atas).
                // Kita bisa menunjukkan error non-blocking, misal via Toast atau Snackbar di UI.
                // Untuk kesederhanaan, kita bisa set state Error jika tidak ada data sama sekali.
                if ((_premiumPackagesState.value as? PremiumUiState.Success)?.data.isNullOrEmpty()) {
                    _premiumPackagesState.value = PremiumUiState.Error(errorMessage)
                }
                Log.e("PremiumVM", "Refresh failed: $errorMessage")
            }
        }
//        viewModelScope.launch {
//            _premiumPackagesState.value = PremiumUiState.Loading
//            try {
//                val response = subscriptionApiService.getPremiumPackages()
//                if (response.isSuccessful && response.body() != null) {
//                    _premiumPackagesState.value = PremiumUiState.Success(response.body()!!.data ?: emptyList())
//                    // Secara otomatis pilih paket pertama jika ada, atau biarkan user memilih
//                    // if (response.body()!!.data?.isNotEmpty() == true) {
//                    //     _selectedPackageState.value = response.body()!!.data!!.first()
//                    // }
//                } else {
//                    val errorMsg = response.errorBody()?.string() ?: response.message() ?: "Failed to fetch packages"
//                    Log.e("PremiumVM", "Fetch Packages Error: $errorMsg, Code: ${response.code()}")
//                    _premiumPackagesState.value = PremiumUiState.Error(errorMsg)
//                }
//            } catch (e: Exception) {
//                Log.e("PremiumVM", "Fetch Packages Exception: ${e.message}", e)
//                _premiumPackagesState.value = PremiumUiState.Error(e.message ?: "An unknown error occurred")
//            }
//        }
    }

    fun selectPackage(pkg: ApiPremiumPackage) {
        _selectedPackageState.value = pkg
    }
    fun selectPackageById(packageId: Int) {
        viewModelScope.launch {
            val currentPackages = (_premiumPackagesState.value as? PremiumUiState.Success)?.data
            val foundPackage = currentPackages?.find { it.id == packageId }
            if (foundPackage != null) {
                _selectedPackageState.value = foundPackage
            } else {
                Log.w("PremiumVM", "Package with ID $packageId not found in current list.")
            }
        }
//        viewModelScope.launch {
//            // Jika paket sudah ada di state, gunakan itu. Jika tidak, fetch ulang (atau cari dari list)
//            val currentPackages = (_premiumPackagesState.value as? PremiumUiState.Success)?.data
//            val foundPackage = currentPackages?.find { it.id == packageId }
//            if (foundPackage != null) {
//                _selectedPackageState.value = foundPackage
//            } else {
//                // Opsi: fetch ulang atau berikan error jika paket tidak ditemukan
//                // Untuk skenario ini, kita asumsikan paket sudah di-fetch sebelumnya
//                Log.w("PremiumVM", "Package with ID $packageId not found in current list.")
//            }
//        }
    }


    fun subscribeToSelectedPackage(paymentMethodName: String) {
        val currentPackage = _selectedPackageState.value
        val userId = authTokenManager.getUserId()

        if (currentPackage == null) {
            _subscriptionResultState.value = PremiumUiState.Error("No package selected.")
            return
        }
        if (userId == null) {
            _subscriptionResultState.value = PremiumUiState.Error("User not logged in.")
            return
        }

        viewModelScope.launch {
            _subscriptionResultState.value = PremiumUiState.Loading
            try {
                val request = SubscribePackageRequest(
                    userId = userId,
                    packageId = currentPackage.id,
                    paymentMethod = paymentMethodName
                )
                // Panggil metode repository
                val response = subscriptionRepository.subscribeToPackage(request)

                if (response.isSuccessful && response.body() != null) {
                    _subscriptionResultState.value = PremiumUiState.Success(response.body()!!)
                    Log.i("PremiumVM", "Subscription successful: ${response.body()!!.message}")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: response.message() ?: "Subscription failed"
                    Log.e("PremiumVM", "Subscription Error: $errorMsg, Code: ${response.code()}")
                    _subscriptionResultState.value = PremiumUiState.Error(errorMsg)
                }
            } catch (e: Exception) {
                Log.e("PremiumVM", "Subscription Exception: ${e.message}", e)
                _subscriptionResultState.value = PremiumUiState.Error(e.message ?: "An unknown error occurred during subscription")
            }
        }
//        val currentPackage = _selectedPackageState.value
//        val userId = authTokenManager.getUserId()
//
//        if (currentPackage == null) {
//            _subscriptionResultState.value = PremiumUiState.Error("No package selected.")
//            return
//        }
//        if (userId == null) {
//            _subscriptionResultState.value = PremiumUiState.Error("User not logged in.")
//            // Idealnya, alur ini tidak boleh tercapai jika user tidak login
//            return
//        }
//
//        viewModelScope.launch {
//            _subscriptionResultState.value = PremiumUiState.Loading
//            try {
//                val request = SubscribePackageRequest(
//                    userId = userId,
//                    packageId = currentPackage.id,
//                    paymentMethod = paymentMethodName // Kirim nama metode pembayaran
//                )
//                val response = subscriptionApiService.subscribeToPackage(request)
//                if (response.isSuccessful && response.body() != null) {
//                    _subscriptionResultState.value = PremiumUiState.Success(response.body()!!)
//                    Log.i("PremiumVM", "Subscription successful: ${response.body()!!.message}")
//                    // Di sini, backend API Anda sudah menganggap user premium.
//                    // Tidak perlu fetch misi di ViewModel ini, biarkan screen selanjutnya (misalnya Home)
//                    // yang menggunakan MissionViewModel untuk mengecek status premium dan fetch misi.
//                } else {
//                    val errorMsg = response.errorBody()?.string() ?: response.message() ?: "Subscription failed"
//                    Log.e("PremiumVM", "Subscription Error: $errorMsg, Code: ${response.code()}")
//                    _subscriptionResultState.value = PremiumUiState.Error(errorMsg)
//                }
//            } catch (e: Exception) {
//                Log.e("PremiumVM", "Subscription Exception: ${e.message}", e)
//                _subscriptionResultState.value = PremiumUiState.Error(e.message ?: "An unknown error occurred during subscription")
//            }
//        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    fun resetSubscriptionResultState() {
        _subscriptionResultState.value = PremiumUiState.Idle
    }
}