package com.example.uijp.data.repository

import android.util.Log
import com.example.uijp.data.local.dao.ArticleDao
import com.example.uijp.data.model.Article
import com.example.uijp.data.model.ArticleHomepageData
import com.example.uijp.data.network.ArticleApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import com.example.uijp.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class ArticleRepository(
    private val apiService: ArticleApiService,
    private val articleDao: ArticleDao
) {

    // Mapper dari model Network (Article) ke model Database (ArticleEntity)
    private fun Article.toEntity(): ArticleEntity = ArticleEntity(
        id = this.id,
        title = this.title,
        content = this.content,
        genre = this.genre,
        author = this.author,
        image_url = this.image_url,
        published_at = this.published_at
    )

    // Mapper dari model Database (ArticleEntity) ke model Domain (Article)
    private fun ArticleEntity.toDomain(): Article = Article(
        id = this.id,
        title = this.title,
        content = this.content,
        genre = this.genre,
        author = this.author,
        image_url = this.image_url,
        published_at = this.published_at,
        created_at = "", // Atau sesuaikan jika perlu
        updated_at = ""  // Atau sesuaikan jika perlu
    )

    // Fungsi untuk mendapatkan data homepage
    fun getHomepageArticles(): Flow<ArticleHomepageData> {
        // 1. Kombinasikan flow dari DAO. Ini akan menjadi sumber data utama.
        val kesehatanFlow = articleDao.getArticlesByGenre("kesehatan")
        val lifestyleFlow = articleDao.getArticlesByGenre("lifestyle")

        return combine(kesehatanFlow, lifestyleFlow) { kesehatan, lifestyle ->
            ArticleHomepageData(
                kesehatan = kesehatan.map { it.toDomain() },
                lifestyle = lifestyle.map { it.toDomain() }
            )
        }.onStart {
            // 2. Saat flow ini mulai dikoleksi (onStart), trigger refresh dari network.
            // Ini tidak akan memblokir flow utama dari DAO.
            try {
                val response = apiService.getArticlesHomepage()
                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()!!.data
                    // 3. Simpan data baru ke database.
                    // Room akan secara otomatis memberi tahu flow di atas untuk emit data baru.
                    articleDao.upsertAll(data.kesehatan.map { it.toEntity() })
                    articleDao.upsertAll(data.lifestyle.map { it.toEntity() })
                }
            } catch (e: Exception) {
                // Tangani error network di sini, misalnya dengan logging.
                // Flow dari database akan tetap berjalan dengan data cache yang ada.
                Log.e("ArticleRepository", "Failed to refresh homepage articles: ${e.message}")
            }
        }
    }

    // Fungsi untuk mendapatkan artikel berdasarkan kategori
    fun getArticlesByGenre(genre: String): Flow<List<Article>> {
        return articleDao.getArticlesByGenre(genre)
            .map { entities -> entities.map { it.toDomain() } } // Map Entity ke Domain
            .onStart {
                try {
                    val response = apiService.getArticlesByGenre(genre)
                    if (response.isSuccessful && response.body()?.success == true) {
                        val articles = response.body()!!.data
                        articleDao.upsertAll(articles.map { it.toEntity() })
                    }
                } catch (e: Exception) {
                    Log.e("ArticleRepository", "Failed to refresh articles for genre $genre: ${e.message}")
                }
            }
    }

    // Fungsi untuk mendapatkan detail artikel
    fun getArticleDetail(articleId: Int): Flow<Article?> {
        return articleDao.getArticleById(articleId)
            .map { entity -> entity?.toDomain() } // Map Entity ke Domain
            .onStart {
                try {
                    val response = apiService.getArticleDetail(articleId)
                    if (response.isSuccessful && response.body()?.success == true) {
                        val article = response.body()!!.data
                        articleDao.upsertAll(listOf(article.toEntity()))
                    }
                } catch (e: Exception) {
                    Log.e("ArticleRepository", "Failed to refresh article detail $articleId: ${e.message}")
                }
            }
    }
}