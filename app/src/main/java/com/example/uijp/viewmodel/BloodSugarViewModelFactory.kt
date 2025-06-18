package com.example.uijp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uijp.data.auth.AuthTokenManager
import com.example.uijp.data.local.AppDatabase
import com.example.uijp.data.network.RetrofitClient
import com.example.uijp.data.repository.BloodSugarRepository

class BloodSugarViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BloodSugarViewModel::class.java)) {
            // 1. Dapatkan instance dari sumber data (API & Room)
            val apiService = RetrofitClient.bloodSugarApiService
            val dao = AppDatabase.getInstance(context.applicationContext).bloodSugarDao()

            // 2. Buat instance Repository
            val repository = BloodSugarRepository(apiService, dao)

            // 3. Dapatkan instance AuthTokenManager
            val authTokenManager = AuthTokenManager.getInstance(context.applicationContext)

            // 4. Buat ViewModel dengan dependensi yang diperlukan
            return BloodSugarViewModel(repository, authTokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}