package com.example.uijp.data.repository

import android.util.Log
import com.example.uijp.data.local.dao.SugarTrackerDao
import com.example.uijp.data.local.entity.ConsumedFoodEntity
import com.example.uijp.data.local.entity.DailySummaryEntity
import com.example.uijp.data.model.*
import com.example.uijp.data.network.SugarTrackerApiService
import com.example.uijp.viewmodel.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SugarTrackerRepository(
    private val apiService: SugarTrackerApiService,
    private val dao: SugarTrackerDao
) {

    // Fungsi utama untuk mendapatkan data tracker
    fun getDailyTracker(date: String?): Flow<UiState<SugarTrackerData>> = flow {
        val tag = "RepoDebug"
        emit(UiState.Loading)

        val targetDate = date ?: getCurrentDate()
        Log.d(tag, "1. getDailyTracker DIMULAI untuk tanggal: $targetDate")

        // 1. Ambil data dari cache
        val cachedData = dao.getDailyTracker(targetDate).first()
        if (cachedData != null) {
            Log.d(tag, "2. Data ditemukan di cache. Mengirimkan data cache ke UI.")
            emit(UiState.Success(mapEntityToData(cachedData)))
        } else {
            Log.d(tag, "2. Cache kosong untuk tanggal $targetDate.")
        }

        // 2. Ambil data dari network
        try {
            Log.d(tag, "3. Mencoba mengambil data dari API...")
            val response = apiService.getDailyTracker(date)

            if (response.isSuccessful && response.body()?.success == true) {
                val remoteData = response.body()!!.data
                Log.d(tag, "4. API SUKSES. Menerima data untuk tanggal: ${remoteData.date}")

                // 3. Simpan data baru ke Room
                Log.d(tag, "5. Memanggil dao.syncDailyTracker...")
                dao.syncDailyTracker(
                    targetDate,
                    mapSummaryToEntity(remoteData.summary, remoteData.date),
                    mapConsumedFoodsToEntities(remoteData.consumed_foods, remoteData.date)
                )
                Log.d(tag, "6. dao.syncDailyTracker SELESAI.")

                // 4. Emit data baru dari database
                Log.d(tag, "7. Membaca ulang data dari DB setelah sync...")
                val freshData = dao.getDailyTracker(targetDate).first()
                if (freshData != null) {
                    Log.d(tag, "8. SUKSES membaca ulang data. Mengirimkan data baru ke UI.")
                    emit(UiState.Success(mapEntityToData(freshData)))
                } else {
                    Log.d(tag, "8. FATAL: Gagal membaca ulang data setelah sync. freshData adalah NULL.")
                    if (cachedData == null) {
                        emit(UiState.Error("Data tidak ditemukan."))
                    }
                }
            } else {
                Log.e(tag, "4. API GAGAL. Pesan: ${response.body()?.message}")
                if (cachedData == null) {
                    emit(UiState.Error(response.body()?.message ?: "Gagal memuat data dari server"))
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "3. KESALAHAN JARINGAN: ${e.message}")
            if (cachedData == null) {
                emit(UiState.Error(e.message ?: "Terjadi kesalahan jaringan"))
            }
        }
    }

    // Fungsi untuk menambah makanan
    suspend fun addFoodToTracker(foodId: Int): AddFoodResponse {
        val response = apiService.addFoodToTracker(AddFoodRequest(foodId))
        // Jika berhasil, pemicu refresh tidak diperlukan di sini karena
        // ViewModel akan memanggil loadDailyTracker lagi, yang akan menyinkronkan data.
        return response.body()!!
    }

    // Fungsi lainnya (update, remove) bisa mengikuti pola yang sama
    // ...
    suspend fun updateFoodQuantity(intakeId: Int, quantity: Int): Response<ApiResponse<String>> {
        return apiService.updateFoodQuantity(intakeId, UpdateQuantityRequest(quantity))
    }

    suspend fun removeFoodFromTracker(intakeId: Int): Response<ApiResponse<String>> {
        return apiService.removeFoodFromTracker(intakeId)
    }


    // --- Helper & Mapper Functions ---

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun mapEntityToData(entity: com.example.uijp.data.local.relation.DailyTrackerWithConsumedFoods): SugarTrackerData {
        return SugarTrackerData(
            date = entity.summary.date,
            summary = DailySummary(
                total_sugar = entity.summary.total_sugar,
                total_calories = entity.summary.total_calories,
                total_carbohydrate = entity.summary.total_carbohydrate,
                total_protein = entity.summary.total_protein,
                total_food_types = entity.consumedFoods.distinctBy { it.food_id }.size,
                total_records = entity.consumedFoods.size,
                recommended_daily_intake = entity.summary.recommended_daily_intake,
                percentage_of_recommendation = entity.summary.percentage_of_recommendation,
                health_status = HealthStatusDetail(status = entity.summary.health_status_code)
            ),
            consumed_foods = entity.consumedFoods.map {
                ConsumedFood(
                    intake_id = it.intake_id,
                    food_id = it.food_id,
                    food_name = it.food_name,
                    quantity = it.quantity,
                    portion_detail = it.portion_detail,
                    sugar_per_portion = 0.0, // Data ini tidak disimpan di entity, bisa di-nol-kan
                    total_sugar = it.total_sugar,
                    total_calories = it.total_calories,
                    consumed_at = it.consumed_at
                )
            }
        )
    }

    private fun mapSummaryToEntity(summary: DailySummary, date: String): DailySummaryEntity {
        return DailySummaryEntity(
            date = date,
            total_sugar = summary.total_sugar,
            total_calories = summary.total_calories,
            total_carbohydrate = summary.total_carbohydrate,
            total_protein = summary.total_protein,
            recommended_daily_intake = summary.recommended_daily_intake,
            percentage_of_recommendation = summary.percentage_of_recommendation,
            health_status_code = summary.health_status.status
        )
    }

    private fun mapConsumedFoodsToEntities(foods: List<ConsumedFood>, date: String): List<ConsumedFoodEntity> {
        return foods.map {
            ConsumedFoodEntity(
                intake_id = it.intake_id,
                food_id = it.food_id,
                date = date,
                food_name = it.food_name,
                quantity = it.quantity,
                portion_detail = it.portion_detail,
                total_sugar = it.total_sugar,
                total_calories = it.total_calories,
                consumed_at = it.consumed_at
            )
        }
    }
}