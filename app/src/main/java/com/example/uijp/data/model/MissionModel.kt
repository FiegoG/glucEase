package com.example.uijp.data.model

import com.google.gson.annotations.SerializedName

// Matches the structure of individual mission objects in the backend response
data class MissionDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("reward_type") val rewardType: String?,
    @SerializedName("reward_value") val rewardValue: Int?,
    @SerializedName("target_value") val targetValue: Int?,
    @SerializedName("mission_logic_type") val missionLogicType: String?,
    @SerializedName("trigger_event_key") val triggerEventKey: String?,
    @SerializedName("point_reward") val pointReward: Int?,
    @SerializedName("created_at") val createdAt: String?, // Assuming String, adjust if it's a Date
    @SerializedName("progress") val progress: Int, // Progress from 0 to 100
    @SerializedName("status") val status: String? // e.g., "in_progress", "completed", null
)

// Matches the "data" part of the backend response
data class MissionData(
    @SerializedName("missions") val missions: List<MissionDto>
)

// Matches the top-level backend response for missions
data class MissionsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: MissionData?,
    @SerializedName("message") val message: String? // For error cases
)

// UI State for a single mission item (simplified for the GamifikasiUI)
data class MissionItemUiState(
    val id: String,
    val title: String,
    val progress: Int, // Progress from 0 to 100
    val isDone: Boolean // Derived from progress for the existing TaskList
)

// Response untuk getMissionDetail
data class MissionDetailResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: MissionDto?, // Menggunakan MissionDto yang sudah ada
    @SerializedName("message") val message: String?
)