package com.example.uijp.viewmodel

import android.util.Log // Import Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uijp.data.model.ErrorResponse
import com.example.uijp.data.model.LoginRequest
import com.example.uijp.data.model.LoginUiState
import com.example.uijp.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.google.gson.Gson

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                val request = LoginRequest(email, password)
                val response = RetrofitClient.apiService.login(request)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true,
                            user = loginResponse.user,
                            accessToken = loginResponse.accessToken
                        )
                    } else {
                        // Log ini jika respons sukses tapi body kosong/tidak terduga
                        val rawResponseBody = response.raw().body()?.string()
                        Log.e(
                            "LoginViewModel",
                            "Successful API call but body is null or unexpected: $rawResponseBody"
                        )
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Respon sukses tapi body kosong atau cacat. Cek logcat."
                        )
                    }
                } else {
                    // --- FOKUS UTAMA: LOG INI ---
                    val errorBodyString = response.errorBody()?.string()
                    Log.e(
                        "LoginViewModel",
                        "API Error - Raw Error Body (HTTP ${response.code()}): $errorBodyString"
                    )
                    // --- END LOG ---

                    val errorResponse = try {
                        Gson().fromJson(errorBodyString, ErrorResponse::class.java)
                    } catch (e: Exception) {
                        Log.e(
                            "LoginViewModel",
                            "Failed to parse error body as JSON: ${e.message}",
                            e
                        )
                        ErrorResponse("Terjadi kesalahan parsing error: ${e.message}. Cek logcat untuk raw body.")
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = errorResponse.message
                    )
                }
            } catch (e: Exception) {
                // Ini menangani error jaringan atau error lain sebelum respons diterima
                Log.e("LoginViewModel", "Network/General Exception: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Koneksi bermasalah: ${e.message}"
                )
            }
        }
    }

        fun clearError() {
            _uiState.value = _uiState.value.copy(errorMessage = null)
        }

        fun resetState() {
            _uiState.value = LoginUiState()
        }
    }
