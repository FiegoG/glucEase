package com.example.uijp.data.model

//deklarasi class2 dan juga atribut yang dibutuhkan untuk viewmodel

data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null,
    val error: String? = null
)

data class DashboardResponse(
    val chartData: List<ChartDataItem>,
    val recentHistory: List<BloodSugarRecord>,
    val summary: SummaryData
)

data class ChartDataItem(
    val date: String,
    val readings: List<Reading>,
    val averageLevel: Double
)

data class Reading(
    val level: Int,
    val time: String
)

data class SummaryData(
    val totalRecords: Int,
    val weeklyAverage: Double
)

data class BloodSugarRecord(
    val id: Int,
    val blood_sugar_level: Int,
    val check_date: String,
    val check_time: String,
    val created_at: String
)

data class HistoryResponse(
    val records: List<BloodSugarRecord>,
    val pagination: PaginationData
)

data class PaginationData(
    val page: Int,
    val limit: Int,
    val hasMore: Boolean
)

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

