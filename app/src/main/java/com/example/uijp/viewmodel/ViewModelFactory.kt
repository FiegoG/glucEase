package com.example.uijp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uijp.data.auth.AuthTokenManager
import com.example.uijp.data.network.BloodSugarApiService

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