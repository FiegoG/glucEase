package com.example.uijp.data.repository

import com.example.uijp.data.local.dao.SugarTrackerDao
import com.example.uijp.data.local.entity.ConsumedFoodEntity
import com.example.uijp.data.local.entity.DailySummaryEntity
import com.example.uijp.data.model.*
import com.example.uijp.data.network.SugarTrackerApiService
import com.example.uijp.viewmodel.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*

class SugarTrackerRepository(
    private val apiService: SugarTrackerApiService,
    private val dao: SugarTrackerDao
) {

    // Fungsi utama untuk mendapatkan data tracker
    fun getDailyTracker(date: String?): Flow<UiState<SugarTrackerData>> = flow {
        emit(UiState.Loading)

        val targetDate = date ?: getCurrentDate()

        // 1. Ambil data dari cache (Room) terlebih dahulu
        val cachedDataFlow = dao.getDailyTracker(targetDate)
        val cachedData = cachedDataFlow.first() // Ambil nilai pertama dari flow
        if (cachedData != null) {
            emit(UiState.Success(mapEntityToData(cachedData)))
        }

        // 2. Ambil data dari network (API)
        try {
            val response = apiService.getDailyTracker(date)
            if (response.isSuccessful && response.body()?.success == true) {
                val remoteData = response.body()!!.data
                // 3. Simpan data baru ke Room
                dao.syncDailyTracker(
                    targetDate,
                    mapSummaryToEntity(remoteData.summary, remoteData.date),
                    mapConsumedFoodsToEntities(remoteData.consumed_foods, remoteData.date)
                )

                // 4. Emit data baru dari database (sumber kebenaran)
                val freshData = dao.getDailyTracker(targetDate).first()
                if (freshData != null) {
                    emit(UiState.Success(mapEntityToData(freshData)))
                } else if (cachedData == null) {
                    // Jika data cache & data baru sama-sama null
                    emit(UiState.Error("Data tidak ditemukan."))
                }
            } else {
                if (cachedData == null) { // Hanya tampilkan error jika tidak ada cache sama sekali
                    emit(UiState.Error(response.body()?.message ?: "Gagal memuat data"))
                }
            }
        } catch (e: Exception) {
            if (cachedData == null) { // Hanya tampilkan error jika tidak ada cache sama sekali
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
                health_status = HealthStatusDetail(code = entity.summary.health_status_code)
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
            health_status_code = summary.health_status.code
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