// viewmodel/ArticleViewModel.kt
package com.example.uijp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uijp.data.model.Article
import com.example.uijp.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleViewModel : ViewModel() {

    private val apiService = RetrofitClient.articleApiService

    // UI State untuk halaman utama artikel
    private val _uiState = MutableStateFlow(ArticleUiState())
    val uiState: StateFlow<ArticleUiState> = _uiState.asStateFlow()

    // UI State untuk halaman kategori artikel
    private val _categoryUiState = MutableStateFlow(ArticleCategoryUiState())
    val categoryUiState: StateFlow<ArticleCategoryUiState> = _categoryUiState.asStateFlow()

    // UI State untuk detail artikel
    private val _detailUiState = MutableStateFlow(ArticleDetailUiState())
    val detailUiState: StateFlow<ArticleDetailUiState> = _detailUiState.asStateFlow()

    init {
        loadHomepageArticles()
    }

    fun loadHomepageArticles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            Log.d("ArticleViewModel", "Attempting to load homepage articles...")

            try {
                val response = apiService.getArticlesHomepage()
                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data
                    Log.d("ArticleViewModel", "Homepage articles loaded successfully: $data")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        kesehatanArticles = data?.kesehatan ?: emptyList(),
                        lifestyleArticles = data?.lifestyle ?: emptyList(),
                        highlightedArticle = data?.kesehatan?.firstOrNull()
                    )
                } else {
                    val errorMessage = response.body()?.message ?: response.errorBody()?.string() ?: "Unknown error"
                    Log.e("ArticleViewModel", "Failed to load homepage articles: HTTP ${response.code()} - $errorMessage")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
            } catch (e: Exception) {
                Log.e("ArticleViewModel", "Exception loading homepage articles: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Terjadi kesalahan: ${e.message}"
                )
            }
        }
    }

    fun loadArticlesByCategory(category: String) {
        viewModelScope.launch {
            _categoryUiState.value = _categoryUiState.value.copy(
                isLoading = true,
                error = null,
                categoryName = category
            )

            try {
                val response = apiService.getArticlesByGenre(category)
                if (response.isSuccessful && response.body()?.success == true) {
                    _categoryUiState.value = _categoryUiState.value.copy(
                        isLoading = false,
                        articles = response.body()?.data ?: emptyList()
                    )
                } else {
                    _categoryUiState.value = _categoryUiState.value.copy(
                        isLoading = false,
                        error = response.body()?.message ?: "Gagal memuat artikel kategori"
                    )
                }
            } catch (e: Exception) {
                _categoryUiState.value = _categoryUiState.value.copy(
                    isLoading = false,
                    error = "Terjadi kesalahan: ${e.message}"
                )
            }
        }
    }

    fun loadArticleDetail(articleId: Int) {
        viewModelScope.launch {
            _detailUiState.value = _detailUiState.value.copy(isLoading = true, error = null)

            try {
                val response = apiService.getArticleDetail(articleId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _detailUiState.value = _detailUiState.value.copy(
                        isLoading = false,
                        article = response.body()?.data
                    )
                } else {
                    _detailUiState.value = _detailUiState.value.copy(
                        isLoading = false,
                        error = response.body()?.message ?: "Gagal memuat detail artikel"
                    )
                }
            } catch (e: Exception) {
                _detailUiState.value = _detailUiState.value.copy(
                    isLoading = false,
                    error = "Terjadi kesalahan: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
        _categoryUiState.value = _categoryUiState.value.copy(error = null)
        _detailUiState.value = _detailUiState.value.copy(error = null)
    }
}

// Data classes untuk UI State
data class ArticleUiState(
    val isLoading: Boolean = false,
    val kesehatanArticles: List<Article> = emptyList(),
    val lifestyleArticles: List<Article> = emptyList(),
    val highlightedArticle: Article? = null,
    val error: String? = null
)

data class ArticleCategoryUiState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val categoryName: String = "",
    val error: String? = null
)

data class ArticleDetailUiState(
    val isLoading: Boolean = false,
    val article: Article? = null,
    val error: String? = null
)