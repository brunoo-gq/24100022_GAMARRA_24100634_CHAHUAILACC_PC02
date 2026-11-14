package com.example.nasaapp.data.repository

import android.content.Context
import com.example.nasaapp.data.local.AppDatabase
import com.example.nasaapp.data.mapper.toEntity
import com.example.nasaapp.data.mapper.toEntities
import com.example.nasaapp.data.remote.nasa.RetrofitInstance
import java.time.LocalDate

class NasaRepository(context: Context) {

    private val db = AppDatabase.get(context)
    private val apodDao = db.apodDao()
    private val asteroideDao = db.asteroideDao()
    private val api = RetrofitInstance.api

    val imagenes = apodDao.observeAll()
    val imagenesFavoritas = apodDao.observeFavorites()
    val asteroides = asteroideDao.observeAll()
    val asteroidesFavoritos = asteroideDao.observeFavorites()
    val asteroidesPeligrosos = asteroideDao.observeHazardous()

    fun obtenerImagen(fecha: String) =
        apodDao.observeByDate(fecha)

    fun obtenerAsteroide(id: String, fecha: String) =
        asteroideDao.observeById(id, fecha)

    suspend fun actualizarImagenes() {

        val hoy = LocalDate.now()
        val inicio = hoy.minusDays(6)

        val formato = java.time.format.DateTimeFormatter
            .ofPattern("yyMMdd")

        val respuesta = api.getApod(
            inicio.format(formato),
            hoy.format(formato)
        )

        val registros = respuesta.mapNotNull {
            it.toEntity()
        }

        apodDao.upsertKeepingFavorites(registros)
    }

    suspend fun actualizarAsteroides() {
        val hoy = LocalDate.now()
        val inicio = hoy.minusDays(6)

        val respuesta = api.getAsteroids(
            inicio.toString(),
            hoy.toString()
        )

        asteroideDao.upsertKeepingFavorites(
            respuesta.toEntities()
        )
    }

    suspend fun favoritoImagen(fecha: String, favorito: Boolean) {
        apodDao.setFavorite(fecha, favorito)
    }

    suspend fun favoritoAsteroide(
        id: String,
        fecha: String,
        favorito: Boolean
    ) {
        asteroideDao.setFavorite(id, fecha, favorito)
    }
}
