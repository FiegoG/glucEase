package com.example.uijp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uijp.data.auth.AuthTokenManager
import com.example.uijp.data.local.AppDatabase
import com.example.uijp.data.network.BloodSugarApiService
import com.example.uijp.data.network.MissionApiService
import com.example.uijp.data.network.RetrofitClient
import com.example.uijp.data.repository.SugarTrackerRepository

//class BloodSugarViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(BloodSugarViewModel::class.java)) {
//            // BloodSugarViewModel membutuhkan Context, jadi kita berikan applicationContext
//            return BloodSugarViewModel(context.applicationContext) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            // Dapatkan instance AuthTokenManager menggunakan Context aplikasi
            val authTokenManager = AuthTokenManager.getInstance(context.applicationContext)
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authTokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class SugarTrackerViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SugarTrackerViewModel::class.java)) {
            // Inisialisasi dependensi di sini
            val apiService = RetrofitClient.sugarTrackerApiService
            val dao = AppDatabase.getInstance(context).sugarTrackerDao()
            val repository = SugarTrackerRepository(apiService, dao)
            @Suppress("UNCHECKED_CAST")
            return SugarTrackerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class FoodTrackerViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodTrackerViewModel::class.java)) {
            // Initialize RetrofitClient dengan context jika belum
            RetrofitClient.initialize(context)
            @Suppress("UNCHECKED_CAST")
            return FoodTrackerViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MissionViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MissionViewModel::class.java)) {
            RetrofitClient.initialize(context.applicationContext)
            val missionApiService = RetrofitClient.missionApiService
            @Suppress("UNCHECKED_CAST")
            return MissionViewModel(missionApiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

class PremiumViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PremiumViewModel::class.java)) {
            // Pastikan RetrofitClient sudah diinisialisasi sebelum factory ini digunakan
            if (RetrofitClient.subscriptionApiService == null) {
                RetrofitClient.initialize(context.applicationContext)
            }
            return PremiumViewModel(
                RetrofitClient.subscriptionApiService,
                AuthTokenManager.getInstance(context.applicationContext)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

