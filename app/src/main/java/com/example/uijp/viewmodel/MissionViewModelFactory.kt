package com.example.uijp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uijp.data.local.AppDatabase
import com.example.uijp.data.network.RetrofitClient
import com.example.uijp.data.repository.MissionRepository

class MissionViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MissionViewModel::class.java)) {
            // 1. Dapatkan instance dari DAO dan ApiService
            val missionDao = AppDatabase.getInstance(context).missionDao()
            val missionApiService = RetrofitClient.missionApiService

            // 2. Buat Repository
            val repository = MissionRepository(missionApiService, missionDao)

            // 3. Buat ViewModel dengan Repository
            @Suppress("UNCHECKED_CAST")
            return MissionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}