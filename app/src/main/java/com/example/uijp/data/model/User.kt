package com.example.uijp.data.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val province: String,
    val city: String,
    val is_verified: Boolean,
    val created_at: String
)