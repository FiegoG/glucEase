package com.example.uijp.viewmodel

import android.util.Log // Import Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uijp.data.auth.AuthTokenManager
import com.example.uijp.data.model.ErrorResponse
import com.example.uijp.data.model.LoginRequest
import com.example.uijp.data.model.LoginUiState
import com.example.uijp.viewmodel.LoginViewModelFactory
import com.example.uijp.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.google.gson.Gson

class LoginViewModel(private val authTokenManager: AuthTokenManager) : ViewModel() {

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
                        // Save auth data to SharedPreferences
                        authTokenManager.saveAuthData(
                            accessToken = loginResponse.accessToken,
                            refreshToken = loginResponse.refreshToken,
                            user = loginResponse.user
                        )

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true,
                            user = loginResponse.user,
                            accessToken = loginResponse.accessToken
                        )

                        Log.d("LoginViewModel", "Login successful for user: ${loginResponse.user.email}")
                    } else {
                        val rawResponseBody = response.raw().body?.string()
                        Log.e("LoginViewModel", "Successful API call but body is null: $rawResponseBody")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Response body is null"
                        )
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    Log.e("LoginViewModel", "API Error (HTTP ${response.code()}): $errorBodyString")

                    val errorResponse = try {
                        Gson().fromJson(errorBodyString, ErrorResponse::class.java)
                    } catch (e: Exception) {
                        Log.e("LoginViewModel", "Failed to parse error body: ${e.message}", e)
                        ErrorResponse("Login failed: ${response.code()}")
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = errorResponse.message
                    )
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Network Exception: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Network error: ${e.message}"
                )
            }
        }
    }

        fun logout() {
            authTokenManager.clearAuthData()
            _uiState.value = LoginUiState()
        }

        fun clearError() {
            _uiState.value = _uiState.value.copy(errorMessage = null)
        }

        fun resetState() {
            _uiState.value = LoginUiState()
        }
    }
