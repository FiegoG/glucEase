package com.example.uijp.data.model

data class SugarTrackerResponse(
    val success: Boolean,
    val message: String,
    val data: SugarTrackerData
)

data class SugarTrackerData(
    val date: String,
    val summary: DailySummary,
    val consumed_foods: List<ConsumedFood>
)

data class DailySummary(
    val total_sugar: Double,
    val total_calories: Double,
    val total_carbohydrate: Double,
    val total_protein: Double,
    val total_food_types: Int,
    val total_records: Int,
    val recommended_daily_intake: Int,
    val percentage_of_recommendation: Double,
    val health_status: HealthStatusDetail
)

data class ConsumedFood(
    val intake_id: Int,
    val food_id: Int,
    val food_name: String,
    val quantity: Int,
    val portion_detail: String,
    val sugar_per_portion: Double,
    val total_sugar: Double,
    val total_calories: Double,
    val consumed_at: String
)

data class FoodListResponse(
    val success: Boolean,
    val message: String,
    val data: FoodListData
)

data class FoodListData(
    val total_foods: Int,
    val search_term: String?,
    val foods: List<Food>
)

data class Food(
    val id: Int,
    val name: String,
    val portion_detail: String,
    val sugar: Double,
    val carbohydrate: Double,
    val protein: Double,
    val calories: Double
)

data class AddFoodRequest(
    val food_id: Int
)

data class AddFoodResponse(
    val success: Boolean,
    val message: String,
    val data: AddFoodData
)

data class AddFoodData(
    val intake_id: Int,
    val food_name: String,
    val portion_detail: String,
    val added_sugar: Double,
    val added_calories: Double,
    val updated_daily_total: UpdatedDailyTotal
)

data class UpdatedDailyTotal(
    val total_sugar: Double,
    val total_calories: Double
)

data class UpdateQuantityRequest(
    val quantity: Int
)

data class HealthStatusDetail(
    val code: String
)