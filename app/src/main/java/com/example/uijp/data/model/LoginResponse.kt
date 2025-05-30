package com.example.uijp.data.model

data class LoginResponse(
    val message: String,
    val user: User,
    val accessToken: String,
    val refreshToken: String
)