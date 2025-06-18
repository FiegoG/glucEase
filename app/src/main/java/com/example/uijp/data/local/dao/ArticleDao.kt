package com.example.uijp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uijp.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    // Mengambil semua artikel berdasarkan genre, dan mengembalikannya sebagai Flow.
    // Flow akan otomatis memancarkan data baru setiap kali data di tabel berubah.
    @Query("SELECT * FROM articles WHERE genre = :genre")
    fun getArticlesByGenre(genre: String): Flow<List<ArticleEntity>>

    // Mengambil artikel spesifik berdasarkan ID
    @Query("SELECT * FROM articles WHERE id = :id")
    fun getArticleById(id: Int): Flow<ArticleEntity?>

    // Memasukkan atau memperbarui list artikel.
    // OnConflictStrategy.REPLACE akan menimpa data lama jika ada artikel dengan ID yang sama.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(articles: List<ArticleEntity>)

    // (Opsional) Menghapus semua artikel berdasarkan genre
    @Query("DELETE FROM articles WHERE genre = :genre")
    suspend fun clearByGenre(genre: String)

    // (Opsional) Menghapus semua artikel
    @Query("DELETE FROM articles")
    suspend fun clearAll()
}