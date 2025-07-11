package com.example.uijp.data.network

import com.example.uijp.data.model.AddBloodSugarRequest
import com.example.uijp.data.model.AddFoodRequest
import com.example.uijp.data.model.AddFoodResponse
import com.example.uijp.data.model.ApiResponse
import com.example.uijp.data.model.ArticleDetailResponse
import com.example.uijp.data.model.ArticleHomepageResponse
import com.example.uijp.data.model.ArticleResponse
import com.example.uijp.data.model.BloodSugarRecord
import com.example.uijp.data.model.DashboardResponse
import com.example.uijp.data.model.DoctorDetailResponse
import com.example.uijp.data.model.DoctorsResponse
import com.example.uijp.data.model.FoodListResponse
import com.example.uijp.data.model.GetPremiumPackagesApiResponse
import com.example.uijp.data.model.HistoryResponse
import com.example.uijp.data.model.LoginRequest
import com.example.uijp.data.model.LoginResponse
import com.example.uijp.data.model.MissionDetailResponse
import com.example.uijp.data.model.MissionsResponse
import com.example.uijp.data.model.SubscribePackageRequest
import com.example.uijp.data.model.SubscribePackageResponse
import com.example.uijp.data.model.SugarTrackerResponse
import com.example.uijp.data.model.UpdateBloodSugarRequest
import com.example.uijp.data.model.UpdateQuantityRequest
import com.example.uijp.data.model.WeeklyReportApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}

interface BloodSugarApiService {

    @GET("api/blood-sugar/dashboard")
    suspend fun getDashboardData(): Response<ApiResponse<DashboardResponse>>

    @GET("api/blood-sugar/history")
    suspend fun getHistory(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<ApiResponse<HistoryResponse>>

    @POST("api/blood-sugar/add")
    suspend fun addRecord(
        @Body request: AddBloodSugarRequest
    ): Response<ApiResponse<BloodSugarRecord>>

    @PUT("api/blood-sugar/{id}")
    suspend fun updateRecord(
        @Path("id") id: Int,
        @Body request: UpdateBloodSugarRequest
    ): Response<ApiResponse<BloodSugarRecord>>

    @DELETE("api/blood-sugar/{id}")
    suspend fun deleteRecord(
        @Path("id") id: Int
    ): Response<ApiResponse<String>>
}

interface SugarTrackerApiService {

    @GET("api/sugar-tracker/daily-tracker")
    suspend fun getDailyTracker(
        @Query("date") date: String? = null
    ): Response<SugarTrackerResponse>

    @GET("api/sugar-tracker/foods")
    suspend fun getFoodList(
        @Query("search") search: String? = null
    ): Response<FoodListResponse>

    @POST("api/sugar-tracker/add-food")
    suspend fun addFoodToTracker(
        @Body request: AddFoodRequest
    ): Response<AddFoodResponse>

    @PUT("api/sugar-tracker/intake/{intake_id}/quantity")
    suspend fun updateFoodQuantity(
        @Path("intake_id") intakeId: Int,
        @Body request: UpdateQuantityRequest
    ): Response<ApiResponse<String>>

    @DELETE("api/sugar-tracker/intake/{intake_id}")
    suspend fun removeFoodFromTracker(
        @Path("intake_id") intakeId: Int
    ): Response<ApiResponse<String>>
}

interface ArticleApiService {

    @GET("articles")
    suspend fun getArticlesHomepage(): Response<ArticleHomepageResponse>

    @GET("articles/genre/{genre}")
    suspend fun getArticlesByGenre(
        @Path("genre") genre: String
    ): Response<ArticleResponse>

    @GET("articles/{id}")
    suspend fun getArticleDetail(
        @Path("id") id: Int
    ): Response<ArticleDetailResponse>
}

interface ConsultationApiService {
    @GET("api/consultation/doctors")
    suspend fun getAllDoctors(): Response<DoctorsResponse>

    @GET("api/consultation/doctor/{id}")
    suspend fun getDoctorDetail(
        @Path("id") doctorId: Int
    ): Response<DoctorDetailResponse>
}

interface MissionApiService {
    @GET("api/missions/") // Endpoint from your Node.js routes
    suspend fun getUserMissions(): Response<MissionsResponse>

    @GET("api/missions/{id}") // Endpoint dari Node.js: /api/missions/:id
    suspend fun getMissionDetail(@Path("id") missionId: String): Response<MissionDetailResponse>
}

interface SubscriptionApiService {

    @GET("api/subscriptions/packages") // Matches your Node.js route
    suspend fun getPremiumPackages(): Response<GetPremiumPackagesApiResponse>

    @POST("api/subscriptions/subscribe")
    suspend fun subscribeToPackage(
        @Body subscriptionRequest: SubscribePackageRequest
    ): Response<SubscribePackageResponse>

    /*
    // Example for getting status (you'll need to define Response model)
    @GET("api/subscriptions/status/{userId}")
    suspend fun getSubscriptionStatus(
        @Path("userId") userId: String
    ): Response<UserSubscriptionStatusResponse>
    */
}

interface WeeklyReportApiService {
    @GET("api/weekly-report/latest/{userId}")
    suspend fun getLatestWeeklyReport(
        @Path("userId") userId: String
    ): Response<WeeklyReportApiResponse>
}