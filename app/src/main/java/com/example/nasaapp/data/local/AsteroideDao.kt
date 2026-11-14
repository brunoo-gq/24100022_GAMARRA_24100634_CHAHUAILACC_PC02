package com.example.nasaapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AsteroideDao {

    @Query("SELECT * FROM asteroides ORDER BY closeApproachDate DESC")
    abstract fun observeAll(): Flow<List<AsteroideEntity>>

    @Query("SELECT * FROM asteroides WHERE isHazardous = 1 ORDER BY closeApproachDate DESC")
    abstract fun observeHazardous(): Flow<List<AsteroideEntity>>

    @Query("SELECT * FROM asteroides WHERE isFavorite = 1 ORDER BY closeApproachDate DESC")
    abstract fun observeFavorites(): Flow<List<AsteroideEntity>>

    @Query("SELECT * FROM asteroides WHERE id = :id AND closeApproachDate = :date LIMIT 1")
    abstract fun observeById(id: String, date: String): Flow<AsteroideEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertIgnore(items: List<AsteroideEntity>): List<Long>

    @Query("UPDATE asteroides SET isFavorite = :fav WHERE id = :id AND closeApproachDate = :date")
    abstract suspend fun setFavorite(id: String, date: String, fav: Boolean)

    @Query(
        """UPDATE asteroides SET name = :name, diameterMinM = :dMin, diameterMaxM = :dMax,
           velocityKmh = :vel, missDistanceKm = :dist, isHazardous = :haz
           WHERE id = :id AND closeApproachDate = :date"""
    )
    abstract suspend fun updateApiFields(
        id: String,
        date: String,
        name: String,
        dMin: Double,
        dMax: Double,
        vel: Double,
        dist: Double,
        haz: Boolean
    )

    @Transaction
    open suspend fun upsertKeepingFavorites(items: List<AsteroideEntity>) {
        val results = insertIgnore(items)
        items.forEachIndexed { i, a ->
            if (results[i] == -1L) {
                updateApiFields(
                    a.id, a.closeApproachDate, a.name,
                    a.diameterMinM, a.diameterMaxM,
                    a.velocityKmh, a.missDistanceKm, a.isHazardous
                )
            }
        }
    }
}