package com.example.uijp.data.network

import android.content.Context
import com.example.uijp.data.auth.AuthTokenManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000" // Ganti dengan URL API Anda

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private var authTokenManager: AuthTokenManager? = null

    fun initialize(context: Context) {
        authTokenManager = AuthTokenManager.getInstance(context)
    }

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request()
        val token = getAuthToken()

        val newRequest = if (token != null) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }

        chain.proceed(newRequest)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val bloodSugarApiService: BloodSugarApiService by lazy {
        retrofit.create(BloodSugarApiService::class.java)
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val sugarTrackerApiService: SugarTrackerApiService by lazy {
        retrofit.create(SugarTrackerApiService::class.java)
    }

    val articleApiService: ArticleApiService by lazy {
        retrofit.create(ArticleApiService::class.java)
    }

    val consultationApiService: ConsultationApiService by lazy {
        retrofit.create(ConsultationApiService::class.java)
    }

    val missionApiService: MissionApiService by lazy {
        retrofit.create(MissionApiService::class.java)
    }

    private fun getAuthToken(): String? {
        return authTokenManager?.getAccessToken()
    }
}