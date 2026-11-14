
package com.example.nasaapp.presentation.nasa

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasaapp.data.repository.NasaRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class NasaViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repository = NasaRepository(application)

    private fun fechaInicio() =
        LocalDate.now().minusDays(6).toString()

    val imagenes = repository.imagenes
        .map { lista ->
            lista.filter { it.date >= fechaInicio() }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val asteroides = repository.asteroides
        .map { lista ->
            lista.filter { it.closeApproachDate >= fechaInicio() }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val imagenesFavoritas = repository.imagenesFavoritas
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val asteroidesFavoritos = repository.asteroidesFavoritos
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val asteroidesPeligrosos = repository.asteroidesPeligrosos
        .map { lista ->
            lista.filter { it.closeApproachDate >= fechaInicio() }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val _cargandoImagenes = MutableStateFlow(false)
    val cargandoImagenes = _cargandoImagenes.asStateFlow()

    private val _cargandoAsteroides = MutableStateFlow(false)
    val cargandoAsteroides = _cargandoAsteroides.asStateFlow()

    private val _errorImagenes = MutableStateFlow<String?>(null)
    val errorImagenes = _errorImagenes.asStateFlow()

    private val _errorAsteroides = MutableStateFlow<String?>(null)
    val errorAsteroides = _errorAsteroides.asStateFlow()

    fun actualizarImagenes() {
        if (_cargandoImagenes.value) return

        viewModelScope.launch {
            _cargandoImagenes.value = true
            _errorImagenes.value = null

            try {
                repository.actualizarImagenes()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _errorImagenes.value =
                    e.message ?: "Error al consultar NASA"
            } finally {
                _cargandoImagenes.value = false
            }
        }
    }

    fun actualizarAsteroides() {
        if (_cargandoAsteroides.value) return

        viewModelScope.launch {
            _cargandoAsteroides.value = true
            _errorAsteroides.value = null

            try {
                repository.actualizarAsteroides()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _errorAsteroides.value =
                    e.message ?: "Error al consultar asteroides"
            } finally {
                _cargandoAsteroides.value = false
            }
        }
    }

    fun obtenerImagen(fecha: String) =
        repository.obtenerImagen(fecha)

    fun obtenerAsteroide(id: String, fecha: String) =
        repository.obtenerAsteroide(id, fecha)

    fun cambiarFavoritoImagen(fecha: String, favorito: Boolean) {
        viewModelScope.launch {
            repository.favoritoImagen(fecha, favorito)
        }
    }

    fun cambiarFavoritoAsteroide(
        id: String,
        fecha: String,
        favorito: Boolean
    ) {
        viewModelScope.launch {
            repository.favoritoAsteroide(id, fecha, favorito)
        }
    }
}
