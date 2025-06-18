package com.example.uijp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val content: String,
    val genre: String,
    val author: String?,
    val image_url: String?,
    val published_at: String
    // created_at dan updated_at bisa dihilangkan jika tidak perlu disimpan lokal
)