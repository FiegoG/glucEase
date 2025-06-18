// file: com/example/uijp/data/local/entity/WeeklyReportEntity.kt
package com.example.uijp.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.uijp.data.local.converter.ReportConverters // Kita akan buat ini nanti
import com.example.uijp.data.model.HealthReportDetail
import com.example.uijp.data.model.ReportInfo

@Entity(tableName = "weekly_report")
@TypeConverters(ReportConverters::class)
data class WeeklyReportEntity(
    // Kita gunakan ID dari reportInfo sebagai Primary Key.
    // Atau bisa juga hardcode satu ID jika hanya ingin menyimpan 1 laporan terbaru.
    @PrimaryKey val id: Int,

    @Embedded(prefix = "report_")
    val reportInfo: ReportInfo?,

    // Objek kompleks ini akan dikonversi menjadi JSON String oleh TypeConverter
    val sugarIntake: HealthReportDetail?,
    val bloodSugar: HealthReportDetail?
)