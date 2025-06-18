package com.example.uijp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "missions")
data class MissionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val rewardType: String?,
    val rewardValue: Int?,
    val targetValue: Int?,
    val missionLogicType: String?,
    val triggerEventKey: String?,
    val pointReward: Int?,
    val createdAt: String?,
    val progress: Int,
    val status: String?
)