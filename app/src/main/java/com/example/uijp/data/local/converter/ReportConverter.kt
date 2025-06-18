// file: com/example/uijp/data/local/converter/ReportConverters.kt
package com.example.uijp.data.local.converter

import androidx.room.TypeConverter
import com.example.uijp.data.model.HealthReportDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ReportConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromHealthReportDetail(detail: HealthReportDetail?): String? {
        if (detail == null) return null
        return gson.toJson(detail)
    }

    @TypeConverter
    fun toHealthReportDetail(json: String?): HealthReportDetail? {
        if (json.isNullOrEmpty()) return null
        val type = object : TypeToken<HealthReportDetail>() {}.type
        return gson.fromJson(json, type)
    }
}