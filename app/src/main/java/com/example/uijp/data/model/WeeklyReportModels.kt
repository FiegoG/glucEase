// file: com/example/uijp/data/model/WeeklyReportModels.kt
package com.example.uijp.data.model

import com.google.gson.annotations.SerializedName

// Wrapper utama sesuai dengan formatReportResponse dari backend Anda
data class WeeklyReportApiResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: WeeklyReportData?
)

data class WeeklyReportData(
    @SerializedName("report_info") val reportInfo: ReportInfo?,
    @SerializedName("sugar_intake") val sugarIntake: HealthReportDetail?,
    @SerializedName("blood_sugar") val bloodSugar: HealthReportDetail?
    // Jika ada data misi di sini, tambahkan. Jika tidak, misi diambil terpisah.
)

data class ReportInfo(
    @SerializedName("id") val id: Int?,
    @SerializedName("period") val period: String?,
    @SerializedName("generated_at") val generatedAt: String?
)

data class HealthReportDetail(
    @SerializedName("daily_data") val dailyData: List<DailyDataPoint>?,
    @SerializedName("ai_analysis") val aiAnalysis: AiAnalysis?
)

// Digunakan untuk sugar_intake.daily_data dan blood_sugar.daily_data
data class DailyDataPoint(
    @SerializedName("day") val day: String?,
    @SerializedName("total_sugar") val totalSugar: Float?, // Untuk asupan gula
    @SerializedName("avg_blood_sugar") val avgBloodSugar: Float?, // Untuk gula darah
    @SerializedName("status") val status: String?
)

data class AiAnalysis(
    @SerializedName("kesimpulan") val kesimpulan: String?,
    @SerializedName("saran") val saran: List<String>?,
    @SerializedName("peringatan") val peringatan: String?
)

// Data class untuk Misi (jika akan digabungkan atau sebagai referensi)
// Jika MissionViewModel sudah memiliki model ini, gunakan yang sudah ada.
data class MissionItem(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("status") val status: String, // e.g., "in_progress", "completed"
    @SerializedName("target_value") val targetValue: Int?,
    @SerializedName("current_progress") val currentProgress: Int? // Anda perlu menambahkan ini di backend jika belum ada
)

data class MissionsListResponse( // Wrapper untuk daftar misi
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<MissionItem>?
    // Atau sesuaikan dengan struktur respons API misi Anda yang sebenarnya
)