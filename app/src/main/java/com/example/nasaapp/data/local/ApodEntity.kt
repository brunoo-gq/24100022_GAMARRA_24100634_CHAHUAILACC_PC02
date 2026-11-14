package com.example.nasaapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apod")
data class ApodEntity(
    @PrimaryKey val date: String,
    val title: String,
    val explanation: String,
    val url: String?,
    val hdUrl: String?,
    val mediaType: String,
    val copyright: String?,
    val isFavorite: Boolean = false
)