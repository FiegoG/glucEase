package com.example.uijp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.uijp.model.Reminder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

class ReminderViewModel : ViewModel() {
    var selectedTime by mutableStateOf("")
        private set

    var selectedDay by mutableStateOf("")
        private set

    // StateFlow untuk menyimpan daftar reminder
    private val _reminder = MutableStateFlow<List<Reminder>>(emptyList())
    val reminder: StateFlow<List<Reminder>> = _reminder.asStateFlow()

    // Update hari menjadi Senin-Minggu
    val days = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")

    fun getCurrentHourMinute(): Pair<Int, Int> {
        val calendar = Calendar.getInstance()
        return Pair(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
    }

    fun updateTime(hour: Int, minute: Int) {
        selectedTime = String.format("%02d:%02d", hour, minute)
    }

    fun updateDay(day: String) {
        selectedDay = day
    }

    fun isReminderValid(): Boolean {
        return selectedTime.isNotEmpty() && selectedDay.isNotEmpty()
    }

    fun saveReminder() {
        if (isReminderValid()) {
            val reminder = Reminder(
                id = UUID.randomUUID().toString(), // Generate unique ID
                time = selectedTime,
                day = selectedDay
            )
            // Tambahkan reminder baru ke list
            _reminder.value = _reminder.value + reminder
            
            // Reset form after creating reminder
            selectedTime = ""
            selectedDay = ""
        }
    }

    fun deleteReminder(id: String) {
        _reminder.value = _reminder.value.filter { it.id != id }
    }
}
