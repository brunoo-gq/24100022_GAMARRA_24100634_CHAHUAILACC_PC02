package com.example.nasaapp.data.mapper

import com.example.nasaapp.data.local.ApodEntity
import com.example.nasaapp.data.local.AsteroideEntity
import com.example.nasaapp.data.remote.nasa.ApodDto
import com.example.nasaapp.data.remote.nasa.NeoFeedDto

fun ApodDto.toEntity(): ApodEntity? {
    val fecha = date?.takeIf { it.isNotBlank() } ?: return null

    return ApodEntity(
        date = fecha,
        title = title ?: "Sin título",
        explanation = explanation.orEmpty(),
        url = url,
        hdUrl = hdurl,
        mediaType = mediaType ?: "unknown",
        copyright = copyright,
        isFavorite = false
    )
}

fun NeoFeedDto.toEntities(): List<AsteroideEntity> {
    return nearEarthObjects.orEmpty().flatMap { (fecha, lista) ->
        lista.mapNotNull { asteroide ->

            val id = asteroide.id
                ?.takeIf { it.isNotBlank() }
                ?: return@mapNotNull null

            val aproximacion = asteroide.closeApproachData
                .orEmpty()
                .firstOrNull { it.closeApproachDate == fecha }

            AsteroideEntity(
                id = id,
                closeApproachDate = fecha,
                name = asteroide.name ?: "Sin nombre",
                diameterMinM =
                    asteroide.estimatedDiameter?.meters?.min ?: 0.0,
                diameterMaxM =
                    asteroide.estimatedDiameter?.meters?.max ?: 0.0,
                velocityKmh = aproximacion?.relativeVelocity
                    ?.kilometersPerHour?.toDoubleOrNull() ?: 0.0,
                missDistanceKm = aproximacion?.missDistance
                    ?.kilometers?.toDoubleOrNull() ?: 0.0,
                isHazardous =
                    asteroide.isPotentiallyHazardousAsteroid ?: false,
                isFavorite = false
            )
        }
    }
}
