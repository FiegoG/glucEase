package com.example.uijp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.uijp.data.local.entity.ConsumedFoodEntity
import com.example.uijp.data.local.entity.DailySummaryEntity

data class DailyTrackerWithConsumedFoods(
    @Embedded
    val summary: DailySummaryEntity,

    @Relation(
        parentColumn = "date",
        entityColumn = "date"
    )
    val consumedFoods: List<ConsumedFoodEntity>
)