package com.example.uijp.data.network

import com.example.uijp.data.model.AddBloodSugarRequest
import com.example.uijp.data.model.ApiResponse
import com.example.uijp.data.model.BloodSugarRecord
import com.example.uijp.data.model.DashboardResponse
import com.example.uijp.data.model.HistoryResponse
import com.example.uijp.data.model.LoginRequest
import com.example.uijp.data.model.LoginResponse
import com.example.uijp.data.model.UpdateBloodSugarRequest
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