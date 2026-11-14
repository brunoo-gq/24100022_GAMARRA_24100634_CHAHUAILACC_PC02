
package com.example.nasaapp.presentation.nasa

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.nasaapp.data.local.ApodEntity
import com.example.nasaapp.data.local.AsteroideEntity
import java.time.LocalDate
import java.util.Locale

private fun numero(valor: Double): String {
    return String.format(Locale.US, "%.2f", valor)
}

// ============================================
// MOSTRAR FOTOGRAFIAS NASA
// ============================================

@Composable
fun FotoNasa(
    imagen: ApodEntity,
    grande: Boolean = false
) {
    val context = LocalContext.current

    var usarHd by remember(imagen.date, grande) {
        mutableStateOf(grande)
    }

    var cargando by remember(imagen.date, grande) {
        mutableStateOf(true)
    }

    var errorImagen by remember(imagen.date, grande) {
        mutableStateOf(false)
    }

    if (!imagen.mediaType.equals("image", ignoreCase = true)) {
        Text(
            if (imagen.mediaType.equals("video", ignoreCase = true))
                "Contenido en video"
            else
                "Imagen no disponible"
        )
        return
    }

    val urlNormal = imagen.url?.takeIf { it.isNotBlank() }
    val urlHd = imagen.hdUrl?.takeIf { it.isNotBlank() }

    val urlImagen = if (usarHd) {
        urlHd ?: urlNormal
    } else {
        urlNormal ?: urlHd
    }

    if (urlImagen == null) {
        Text("La NASA no proporcionó una imagen")
        return
    }

    Column {
        if (cargando && !errorImagen) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(urlImagen)
                .crossfade(true)
                .build(),
            contentDescription = imagen.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(if (grande) 300.dp else 180.dp),
            contentScale = if (grande)
                ContentScale.Fit
            else
                ContentScale.Crop,
            onLoading = {
                cargando = true
                errorImagen = false
            },
            onSuccess = {
                cargando = false
                errorImagen = false
            },
            onError = { resultado ->
                Log.e(
                    "NASA_IMAGEN",
                    "Error al cargar: $urlImagen",
                    resultado.result.throwable
                )

                if (usarHd && urlNormal != null &&
                    urlNormal != urlHd
                ) {
                    usarHd = false
                } else {
                    cargando = false
                    errorImagen = true
                }
            }
        )

        if (errorImagen) {
            Text(
                "No se pudo cargar la fotografía",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

// ============================================
// LISTA DE IMAGENES APOD
// ============================================

@Composable
fun ApodScreen(
    vm: NasaViewModel,
    favoritos: Boolean,
    onVolver: () -> Unit,
    onFavoritos: () -> Unit,
    onDetalle: (String) -> Unit
) {
    val imagenes by vm.imagenes.collectAsState()
    val favoritas by vm.imagenesFavoritas.collectAsState()
    val cargando by vm.cargandoImagenes.collectAsState()
    val error by vm.errorImagenes.collectAsState()

    LaunchedEffect(favoritos) {
        if (!favoritos) {
            vm.actualizarImagenes()
        }
    }

    val lista = if (favoritos) favoritas else imagenes

    val imagenDia = imagenes.firstOrNull {
        it.date == LocalDate.now().toString()
    }

    val imagenReciente = imagenDia ?: imagenes.firstOrNull()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    if (favoritos)
                        "Mis imágenes favoritas"
                    else
                        "Imágenes NASA",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f)
                )

                TextButton(
                    onClick = if (favoritos) onVolver else onFavoritos
                ) {
                    Text(if (favoritos) "Volver" else "Favoritos")
                }
            }
        }

        if (!favoritos) {

            item {
                if (cargando) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (error != null) {
                    Text(
                        error.orEmpty(),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            if (imagenReciente != null) {
                item {
                    Text(
                        if (imagenDia != null)
                            "Imagen astronómica del día"
                        else
                            "Imagen astronómica más reciente",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(Modifier.height(8.dp))

                    ApodTarjeta(
                        imagen = imagenReciente,
                        vm = vm,
                        grande = true,
                        onDetalle = {
                            onDetalle(imagenReciente.date)
                        }
                    )
                }
            }
        }

        item {
            Text(
                if (favoritos)
                    "Imágenes guardadas"
                else
                    "Últimos 7 días",
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (lista.isEmpty() && !cargando) {
            item {
                Text(
                    if (favoritos)
                        "Todavía no tienes imágenes favoritas"
                    else
                        "No hay imágenes para mostrar"
                )
            }
        }

        items(lista, key = { it.date }) { imagen ->
            ApodTarjeta(
                imagen = imagen,
                vm = vm,
                onDetalle = {
                    onDetalle(imagen.date)
                }
            )
        }
    }
}

// ============================================
// TARJETA DE IMAGEN
// ============================================

@Composable
fun ApodTarjeta(
    imagen: ApodEntity,
    vm: NasaViewModel,
    grande: Boolean = false,
    onDetalle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDetalle() }
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            FotoNasa(
                imagen = imagen,
                grande = grande
            )

            Spacer(Modifier.height(10.dp))

            Text(
                imagen.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                imagen.date,
                style = MaterialTheme.typography.bodySmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDetalle) {
                    Text("Ver detalle")
                }

                TextButton(onClick = {
                    vm.cambiarFavoritoImagen(
                        imagen.date,
                        !imagen.isFavorite
                    )
                }) {
                    Text(
                        if (imagen.isFavorite)
                            "♥ Favorito"
                        else
                            "♡ Guardar"
                    )
                }
            }
        }
    }
}

// ============================================
// DETALLE DE IMAGEN
// ============================================

@Composable
fun ApodDetailScreen(
    vm: NasaViewModel,
    fecha: String,
    onVolver: () -> Unit
) {
    val flujo = remember(fecha) {
        vm.obtenerImagen(fecha)
    }

    val imagen by flujo.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        TextButton(onClick = onVolver) {
            Text("← Volver")
        }

        val dato = imagen

        if (dato == null) {
            Text("Cargando imagen...")
        } else {

            Text(
                dato.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Text("Fecha: ${dato.date}")

            Spacer(Modifier.height(16.dp))

            FotoNasa(
                imagen = dato,
                grande = true
            )

            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                vm.cambiarFavoritoImagen(
                    dato.date,
                    !dato.isFavorite
                )
            }) {
                Text(
                    if (dato.isFavorite)
                        "♥ Quitar de favoritos"
                    else
                        "♡ Agregar a favoritos"
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "Explicación",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(8.dp))

            Text(dato.explanation)

            if (!dato.copyright.isNullOrBlank()) {
                Spacer(Modifier.height(12.dp))
                Text("Autor: ${dato.copyright}")
            }
        }
    }
}

// ============================================
// LISTA DE ASTEROIDES
// ============================================

@Composable
fun AsteroideScreen(
    vm: NasaViewModel,
    favoritos: Boolean,
    onVolver: () -> Unit,
    onFavoritos: () -> Unit,
    onDetalle: (String, String) -> Unit
) {
    val asteroides by vm.asteroides.collectAsState()
    val peligrosos by vm.asteroidesPeligrosos.collectAsState()
    val guardados by vm.asteroidesFavoritos.collectAsState()

    val cargando by vm.cargandoAsteroides.collectAsState()
    val error by vm.errorAsteroides.collectAsState()

    var soloPeligrosos by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(favoritos) {
        if (!favoritos) {
            vm.actualizarAsteroides()
        }
    }

    val lista = when {
        favoritos -> guardados
        soloPeligrosos -> peligrosos
        else -> asteroides
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    if (favoritos)
                        "Mis asteroides"
                    else
                        "Asteroides NASA",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f)
                )

                TextButton(
                    onClick = if (favoritos) onVolver else onFavoritos
                ) {
                    Text(if (favoritos) "Volver" else "Favoritos")
                }
            }
        }

        if (!favoritos) {

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Solo potencialmente peligrosos",
                        modifier = Modifier.weight(1f)
                    )

                    Switch(
                        checked = soloPeligrosos,
                        onCheckedChange = {
                            soloPeligrosos = it
                        }
                    )
                }

                if (cargando) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (error != null) {
                    Text(
                        error.orEmpty(),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        if (lista.isEmpty() && !cargando) {
            item {
                Text("No hay asteroides para mostrar")
            }
        }

        items(
            lista,
            key = { "${it.id}_${it.closeApproachDate}" }
        ) { asteroide ->
            AsteroideTarjeta(
                asteroide = asteroide,
                vm = vm,
                onDetalle = {
                    onDetalle(
                        asteroide.id,
                        asteroide.closeApproachDate
                    )
                }
            )
        }
    }
}

// ============================================
// TARJETA DE ASTEROIDE
// ============================================

@Composable
fun AsteroideTarjeta(
    asteroide: AsteroideEntity,
    vm: NasaViewModel,
    onDetalle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDetalle() }
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Text(
                asteroide.name,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            Text("Fecha: ${asteroide.closeApproachDate}")

            Text(
                "Diámetro máximo: " +
                        "${numero(asteroide.diameterMaxM)} m"
            )

            Text(
                "Velocidad: " +
                        "${numero(asteroide.velocityKmh)} km/h"
            )

            Text(
                "Distancia: " +
                        "${numero(asteroide.missDistanceKm)} km"
            )

            if (asteroide.isHazardous) {
                Spacer(Modifier.height(8.dp))

                Text(
                    "⚠ Potencialmente peligroso",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Row {
                TextButton(onClick = onDetalle) {
                    Text("Ver detalle")
                }

                TextButton(onClick = {
                    vm.cambiarFavoritoAsteroide(
                        asteroide.id,
                        asteroide.closeApproachDate,
                        !asteroide.isFavorite
                    )
                }) {
                    Text(
                        if (asteroide.isFavorite)
                            "♥ Favorito"
                        else
                            "♡ Guardar"
                    )
                }
            }
        }
    }
}

// ============================================
// DETALLE DE ASTEROIDE
// ============================================

@Composable
fun AsteroideDetailScreen(
    vm: NasaViewModel,
    id: String,
    fecha: String,
    onVolver: () -> Unit
) {
    val flujo = remember(id, fecha) {
        vm.obtenerAsteroide(id, fecha)
    }

    val asteroide by flujo.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        TextButton(onClick = onVolver) {
            Text("← Volver")
        }

        val dato = asteroide

        if (dato == null) {
            Text("Cargando asteroide...")
        } else {

            Text(
                dato.name,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(16.dp))

            Text("ID: ${dato.id}")
            Text("Fecha: ${dato.closeApproachDate}")

            Spacer(Modifier.height(8.dp))

            Text(
                "Diámetro mínimo: " +
                        "${numero(dato.diameterMinM)} m"
            )

            Text(
                "Diámetro máximo: " +
                        "${numero(dato.diameterMaxM)} m"
            )

            Text(
                "Velocidad: " +
                        "${numero(dato.velocityKmh)} km/h"
            )

            Text(
                "Distancia: " +
                        "${numero(dato.missDistanceKm)} km"
            )

            Spacer(Modifier.height(12.dp))

            Text(
                if (dato.isHazardous)
                    "⚠ Potencialmente peligroso"
                else
                    "No potencialmente peligroso",
                color = if (dato.isHazardous)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                vm.cambiarFavoritoAsteroide(
                    dato.id,
                    dato.closeApproachDate,
                    !dato.isFavorite
                )
            }) {
                Text(
                    if (dato.isFavorite)
                        "♥ Quitar de favoritos"
                    else
                        "♡ Agregar a favoritos"
                )
            }
        }
    }
}
