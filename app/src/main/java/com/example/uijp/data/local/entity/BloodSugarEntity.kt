package com.example.uijp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blood_sugar_records") // Menentukan nama tabel di database
data class BloodSugarRecord(
    @PrimaryKey val id: Int, // id dari API akan menjadi Primary Key
    val blood_sugar_level: Int,
    val check_date: String,
    val check_time: String,
    val created_at: String
)
