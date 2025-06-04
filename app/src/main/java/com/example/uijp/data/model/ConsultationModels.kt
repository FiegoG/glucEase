package com.example.uijp.data.model

data class Doctor(
    val id: Int,
    val user_id: Int,
    val doctor_name: String,
    val expertise: String,
    val bio: String,
    val rating: Double,
    val consultation_fee: Int,
    val is_active: Int
)

data class DoctorsResponse(
    val success: Boolean,
    val message: String,
    val data: List<Doctor>
)

data class DoctorDetail(
    val doctor: Doctor,
    val schedules: List<DoctorSchedule>
)

data class DoctorSchedule(
    val id: Int,
    val doctor_id: Int,
    val available_date: String,
    val available_time: String,
    val is_booked: Int
)

data class DoctorDetailResponse(
    val success: Boolean,
    val message: String,
    val data: DoctorDetail
)