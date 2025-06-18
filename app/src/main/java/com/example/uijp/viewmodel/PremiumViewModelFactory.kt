package com.example.uijp.viewmodel

// file: (Lokasi ViewModelFactory Anda, misal: viewmodel/PremiumViewModelFactory.kt)

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uijp.data.auth.AuthTokenManager
import com.example.uijp.data.local.AppDatabase // <-- Import AppDatabase
import com.example.uijp.data.network.RetrofitClient
import com.example.uijp.data.repository.SubscriptionRepository // <-- Import Repository

class PremiumViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    /**
     * Saran (Best Practice):
     * Sebaiknya, inisialisasi singleton seperti RetrofitClient.initialize(context)
     * dilakukan sekali saja di kelas Application aplikasi Anda, bukan di dalam factory.
     * Ini memastikan semua komponen siap sebelum dibutuhkan.
     *
     * Contoh di kelas Application:
     * class MyApplication : Application() {
     * override fun onCreate() {
     * super.onCreate()
     * RetrofitClient.initialize(this)
     * }
     * }
     * Jangan lupa daftarkan di AndroidManifest.xml: <application android:name=".MyApplication" ...>
     */

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PremiumViewModel::class.java)) {
            // 1. Dapatkan semua dependensi yang dibutuhkan oleh Repository dan ViewModel
            val authTokenManager = AuthTokenManager.getInstance(context.applicationContext)
            val subscriptionApiService = RetrofitClient.subscriptionApiService
            val subscriptionDao = AppDatabase.getInstance(context.applicationContext).subscriptionDao()

            // 2. Buat instance Repository dengan dependensinya
            val repository = SubscriptionRepository.getInstance(subscriptionApiService, subscriptionDao)

            // 3. Buat PremiumViewModel dengan Repository (bukan ApiService lagi)
            return PremiumViewModel(
                subscriptionRepository = repository,
                authTokenManager = authTokenManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}