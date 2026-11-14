package com.example.nasaapp.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val rutaActual = backStack?.destination?.route

    Scaffold(
        bottomBar = {
            if (rutaActual == "imagenes" ||
                rutaActual == "asteroides") {

                NavigationBar {

                    NavigationBarItem(
                        selected = rutaActual == "imagenes",
                        onClick = {
                            navController.navigate("imagenes") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Text("📷") },
                        label = { Text("Imágenes") }
                    )

                    NavigationBarItem(
                        selected = rutaActual == "asteroides",
                        onClick = {
                            navController.navigate("asteroides") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Text("☄️") },
                        label = { Text("Asteroides") }
                    )
                }
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "imagenes",
            modifier = Modifier.padding(padding)
        ) {

            composable("imagenes") {
                PantallaInicial(
                    titulo = "Imágenes NASA",
                    onFavoritos = {
                        navController.navigate("imagenes/favoritos")
                    },
                    onDetalle = {
                        navController.navigate("imagenes/detalle")
                    }
                )
            }

            composable("asteroides") {
                PantallaInicial(
                    titulo = "Asteroides NASA",
                    onFavoritos = {
                        navController.navigate("asteroides/favoritos")
                    },
                    onDetalle = {
                        navController.navigate("asteroides/detalle")
                    }
                )
            }

            composable("imagenes/favoritos") {
                PantallaSecundaria("Mis imágenes favoritas")
            }

            composable("imagenes/detalle") {
                PantallaSecundaria("Detalle de imagen")
            }

            composable("asteroides/favoritos") {
                PantallaSecundaria("Mis asteroides")
            }

            composable("asteroides/detalle") {
                PantallaSecundaria("Detalle de asteroide")
            }
        }
    }
}

@Composable
fun PantallaInicial(
    titulo: String,
    onFavoritos: () -> Unit,
    onDetalle: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onFavoritos) {
            Text("Mis favoritos")
        }

        Button(onClick = onDetalle) {
            Text("Ver detalle")
        }
    }
}

@Composable
fun PantallaSecundaria(titulo: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
