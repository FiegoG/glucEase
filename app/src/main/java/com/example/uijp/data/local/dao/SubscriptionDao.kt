// file: data/local/dao/SubscriptionDao.kt
package com.example.uijp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uijp.data.local.entity.PremiumPackageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionDao {

    // Mengambil semua paket sebagai Flow, sehingga UI akan update otomatis jika data di DB berubah
    @Query("SELECT * FROM premium_packages")
    fun getAllPackages(): Flow<List<PremiumPackageEntity>>

    // Menyimpan daftar paket. Jika ada paket dengan 'id' yang sama, akan diganti.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePackages(packages: List<PremiumPackageEntity>)

    // Menghapus semua paket (berguna sebelum menyisipkan data baru)
    @Query("DELETE FROM premium_packages")
    suspend fun deleteAllPackages()
}