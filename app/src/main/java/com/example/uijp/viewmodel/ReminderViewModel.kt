package com.example.uijp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.*

class ReminderViewModel : ViewModel() {
    var selectedTime by mutableStateOf("")
        private set

    var selectedDay by mutableStateOf("")
        private set

    val days = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")

    fun updateTime(hour: Int, minute: Int) {
        selectedTime = String.format("%02d:%02d", hour, minute)
    }

    fun updateDay(day: String) {
        selectedDay = day
    }

    fun isReminderValid(): Boolean {
        return selectedTime.isNotEmpty() && selectedDay.isNotEmpty()
    }

    fun getCurrentHourMinute(): Pair<Int, Int> {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.HOUR_OF_DAY) to calendar.get(Calendar.MINUTE)
    }
}
