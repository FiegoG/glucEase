package com.example.uijp.data.model

data class Article(
    val id: Int,
    val title: String,
    val content: String,
    val genre: String, // "Kesehatan" atau "Lifestyle"
    val author: String?,
    val image_url: String?,
    val published_at: String,
    val created_at: String,
    val updated_at: String
)

data class ArticleResponse(
    val success: Boolean,
    val message: String,
    val data: List<Article>
)

data class ArticleDetailResponse(
    val success: Boolean,
    val message: String,
    val data: Article
)

data class ArticleHomepageResponse(
    val success: Boolean,
    val message: String,
    val data: ArticleHomepageData
)

data class ArticleHomepageData(
    val kesehatan: List<Article>,
    val lifestyle: List<Article>
)