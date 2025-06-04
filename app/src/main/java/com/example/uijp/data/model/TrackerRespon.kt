package com.example.uijp.data.model

data class AddBloodSugarRequest(
    val bloodSugarLevel: Int,
    val checkDate: String,
    val checkTime: String
)

data class UpdateBloodSugarRequest(
    val bloodSugarLevel: Int,
    val checkDate: String,
    val checkTime: String
)