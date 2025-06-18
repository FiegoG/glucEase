// file: data/local/entity/PremiumPackageEntity.kt
package com.example.uijp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "premium_packages")
data class PremiumPackageEntity(
    @PrimaryKey
    val id: Int,
    val packageName: String?,
    val durationMonths: Int,
    val price: String?,
    val description: String?
)