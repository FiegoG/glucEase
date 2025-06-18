package com.example.uijp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consumed_foods")
data class ConsumedFoodEntity(
    @PrimaryKey
    val intake_id: Int,
    val food_id: Int,
    val date: String, // Foreign key to link with DailySummaryEntity
    val food_name: String,
    val quantity: Int,
    val portion_detail: String,
    val total_sugar: Double,
    val total_calories: Double,
    val consumed_at: String
)