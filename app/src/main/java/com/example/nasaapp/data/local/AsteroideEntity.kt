package com.example.nasaapp.data.local

import androidx.room.Entity

@Entity(tableName = "asteroides", primaryKeys = ["id", "closeApproachDate"])
data class AsteroideEntity(
    val id: String,
    val closeApproachDate: String,
    val name: String,
    val diameterMinM: Double,
    val diameterMaxM: Double,
    val velocityKmh: Double,
    val missDistanceKm: Double,
    val isHazardous: Boolean,
    val isFavorite: Boolean = false
)