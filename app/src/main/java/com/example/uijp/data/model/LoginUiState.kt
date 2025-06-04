package com.example.uijp.data.model

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val user: User? = null,
    val accessToken: String? = null
)