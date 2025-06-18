package com.example.uijp.data.repository

import com.example.uijp.data.local.dao.ArticleDao
import com.example.uijp.data.model.Article
import com.example.uijp.data.model.ArticleHomepageData
import com.example.uijp.data.network.ArticleApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import com.example.uijp.data.local.entity.ArticleEntity

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
    fun getHomepageArticles(): Flow<Result<ArticleHomepageData>> = flow {
        try {
            // 1. Ambil data dari API
            val response = apiService.getArticlesHomepage()
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()!!.data
                // 2. Simpan data ke database Room
                articleDao.upsertAll(data.kesehatan.map { it.toEntity() })
                articleDao.upsertAll(data.lifestyle.map { it.toEntity() })
            }

            // 3. Ambil data dari database dan pancarkan (emit)
            // Menggabungkan dua flow (kesehatan dan lifestyle) dari database menjadi satu
            val kesehatanFlow = articleDao.getArticlesByGenre("Kesehatan")
            val lifestyleFlow = articleDao.getArticlesByGenre("Lifestyle")

            combine(kesehatanFlow, lifestyleFlow) { kesehatan, lifestyle ->
                Result.success(
                    ArticleHomepageData(
                        kesehatan = kesehatan.map { it.toDomain() },
                        lifestyle = lifestyle.map { it.toDomain() }
                    )
                )
            }.collect {
                emit(it) // emit hasil kombinasi
            }

        } catch (e: Exception) {
            // Jika network gagal, coba ambil dari cache dan emit sebagai error
            emit(Result.failure(e))
        }
    }

    // Fungsi untuk mendapatkan artikel berdasarkan kategori
    fun getArticlesByGenre(genre: String): Flow<Result<List<Article>>> = flow {
        try {
            // 1. Ambil data dari API
            val response = apiService.getArticlesByGenre(genre)
            if (response.isSuccessful && response.body()?.success == true) {
                val articles = response.body()!!.data
                // 2. Simpan ke database
                articleDao.upsertAll(articles.map { it.toEntity() })
            }
        } catch (e: Exception) {
            // Biarkan saja, data lama dari cache akan tetap dipancarkan
        }

        // 3. Selalu ambil dan pancarkan data dari database
        articleDao.getArticlesByGenre(genre).collect { entities ->
            emit(Result.success(entities.map { it.toDomain() }))
        }
    }

    // Fungsi untuk mendapatkan detail artikel
    fun getArticleDetail(articleId: Int): Flow<Result<Article?>> = flow {
        try {
            // 1. Ambil dari API untuk memastikan data paling update
            val response = apiService.getArticleDetail(articleId)
            if (response.isSuccessful && response.body()?.success == true) {
                val article = response.body()!!.data
                // 2. Simpan ke database
                articleDao.upsertAll(listOf(article.toEntity()))
            }
        } catch (e: Exception) {
            // Abaikan error, biarkan cache yang bekerja
        }

        // 3. Ambil dari database dan pancarkan
        articleDao.getArticleById(articleId).collect{ entity ->
            emit(Result.success(entity?.toDomain()))
        }
    }
}