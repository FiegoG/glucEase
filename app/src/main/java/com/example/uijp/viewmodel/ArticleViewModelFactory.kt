package com.example.uijp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uijp.data.local.AppDatabase
import com.example.uijp.data.network.RetrofitClient
import com.example.uijp.data.repository.ArticleRepository

class ArticleViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val database = AppDatabase.getInstance(context)
            val repository = ArticleRepository(
                RetrofitClient.articleApiService,
                database.articleDao()
            )
            return ArticleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}