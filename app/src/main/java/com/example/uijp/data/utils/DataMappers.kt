// file: com/example/uijp/data/DataMappers.kt
package com.example.uijp.data.utils

// Import kedua kelas BloodSugarRecord
import com.example.uijp.data.local.entity.BloodSugarRecord as BloodSugarEntity
import com.example.uijp.data.model.BloodSugarRecord as BloodSugarModel

/**
 * Mengubah objek Model API (BloodSugarModel) menjadi Entitas Database (BloodSugarEntity)
 * yang siap dimasukkan ke Room.
 */
fun BloodSugarModel.toEntity(): BloodSugarEntity {
    return BloodSugarEntity(
        id = this.id, // Pastikan model Anda punya ID
        blood_sugar_level = this.blood_sugar_level,
        check_date = this.check_date,
        check_time = this.check_time,
        created_at = this.created_at
    )
}

/**
 * Mengubah objek Entitas Database (BloodSugarEntity) menjadi Model API (BloodSugarModel)
 * yang siap ditampilkan di UI.
 */
fun BloodSugarEntity.toModel(): BloodSugarModel {
    return BloodSugarModel(
        id = this.id,
        blood_sugar_level = this.blood_sugar_level,
        check_date = this.check_date,
        check_time = this.check_time,
        created_at = this.created_at
    )
}

// Opsional: Buat juga mapper untuk List agar lebih ringkas
fun List<BloodSugarModel>.toEntityList(): List<BloodSugarEntity> {
    return this.map { it.toEntity() }
}

fun List<BloodSugarEntity>.toModelList(): List<BloodSugarModel> {
    return this.map { it.toModel() }
}