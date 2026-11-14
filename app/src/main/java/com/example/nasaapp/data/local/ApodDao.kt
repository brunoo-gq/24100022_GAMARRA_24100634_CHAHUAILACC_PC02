package com.example.nasaapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ApodDao {

    @Query("SELECT * FROM apod ORDER BY date DESC")
    abstract fun observeAll(): Flow<List<ApodEntity>>

    @Query("SELECT * FROM apod WHERE isFavorite = 1 ORDER BY date DESC")
    abstract fun observeFavorites(): Flow<List<ApodEntity>>

    @Query("SELECT * FROM apod WHERE date = :date")
    abstract fun observeByDate(date: String): Flow<ApodEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertIgnore(items: List<ApodEntity>): List<Long>

    @Query("UPDATE apod SET isFavorite = :fav WHERE date = :date")
    abstract suspend fun setFavorite(date: String, fav: Boolean)

    @Query(
        """UPDATE apod SET title = :title, explanation = :explanation, url = :url,
           hdUrl = :hdUrl, mediaType = :mediaType, copyright = :copyright
           WHERE date = :date"""
    )
    abstract suspend fun updateApiFields(
        date: String,
        title: String,
        explanation: String,
        url: String?,
        hdUrl: String?,
        mediaType: String,
        copyright: String?
    )

    @Transaction
    open suspend fun upsertKeepingFavorites(items: List<ApodEntity>) {
        val results = insertIgnore(items)
        items.forEachIndexed { i, item ->
            if (results[i] == -1L) {
                updateApiFields(
                    item.date, item.title, item.explanation,
                    item.url, item.hdUrl, item.mediaType, item.copyright
                )
            }
        }
    }
}