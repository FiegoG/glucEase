// file: data/repository/SubscriptionRepository.kt
package com.example.uijp.data.repository

import android.util.Log
import com.example.uijp.data.local.dao.SubscriptionDao
import com.example.uijp.data.local.entity.PremiumPackageEntity
import com.example.uijp.data.model.ApiPremiumPackage
import com.example.uijp.data.model.SubscribePackageRequest
import com.example.uijp.data.model.SubscribePackageResponse
import com.example.uijp.data.network.SubscriptionApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

// Helper untuk konversi data
fun ApiPremiumPackage.toEntity(): PremiumPackageEntity {
    return PremiumPackageEntity(
        id = this.id,
        packageName = this.packageName,
        durationMonths = this.durationMonths,
        price = this.price,
        description = this.description
    )
}

fun PremiumPackageEntity.toApiModel(): ApiPremiumPackage {
    return ApiPremiumPackage(
        id = this.id,
        packageName = this.packageName,
        durationMonths = this.durationMonths,
        price = this.price,
        description = this.description
    )
}


class SubscriptionRepository(
    private val apiService: SubscriptionApiService,
    private val localDataSource: SubscriptionDao
) {

    /**
     * Mengambil daftar paket premium.
     * Fungsi ini akan selalu mencoba mengambil data dari API.
     * Jika berhasil, data akan disimpan ke database lokal (cache).
     * Data dari database lokal akan langsung di-emit ke pengamat (ViewModel) melalui Flow.
     * Jika API gagal, Flow akan tetap menyediakan data lama dari cache jika ada.
     */
    fun getPremiumPackages(): Flow<List<ApiPremiumPackage>> {
        // 1. Langsung return Flow dari database. UI akan langsung menampilkan data cache.
        return localDataSource.getAllPackages().map { entities ->
            entities.map { it.toApiModel() }
        }
    }

    /**
     * Fungsi terpisah untuk menyegarkan data dari jaringan.
     * ViewModel akan memanggil ini saat pertama kali dibuat atau saat pull-to-refresh.
     */
    suspend fun refreshPremiumPackages(): String? {
        try {
            val response = apiService.getPremiumPackages()
            if (response.isSuccessful && response.body() != null) {
                val packagesFromApi = response.body()!!.data
                if (!packagesFromApi.isNullOrEmpty()) {
                    // 2. Konversi data API ke Entity dan simpan ke database
                    val packageEntities = packagesFromApi.map { it.toEntity() }
                    localDataSource.insertOrUpdatePackages(packageEntities)
                }
                return null // Sukses
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Failed to refresh packages"
                Log.e("SubscriptionRepo", "API Error: $errorMsg")
                return errorMsg // Gagal, kembalikan pesan error
            }
        } catch (e: Exception) {
            Log.e("SubscriptionRepo", "Network Exception: ${e.message}", e)
            return e.message ?: "An unknown network error occurred" // Gagal, kembalikan pesan error
        }
    }


    /**
     * Fungsi untuk berlangganan. Operasi ini hanya berinteraksi dengan API.
     * Tidak ada caching yang diperlukan untuk request ini.
     */
    suspend fun subscribeToPackage(request: SubscribePackageRequest): Response<SubscribePackageResponse> {
        return apiService.subscribeToPackage(request)
    }

    companion object {
        @Volatile
        private var INSTANCE: SubscriptionRepository? = null

        fun getInstance(apiService: SubscriptionApiService, localDataSource: SubscriptionDao): SubscriptionRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = SubscriptionRepository(apiService, localDataSource)
                INSTANCE = instance
                instance
            }
        }
    }
}