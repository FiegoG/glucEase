package com.example.uijp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_summary")
data class DailySummaryEntity(
    @PrimaryKey
    val date: String, // "YYYY-MM-DD"
    val total_sugar: Double,
    val total_calories: Double,
    val total_carbohydrate: Double,
    val total_protein: Double,
    val recommended_daily_intake: Int,
    val percentage_of_recommendation: Double,
    val health_status_code: String
)