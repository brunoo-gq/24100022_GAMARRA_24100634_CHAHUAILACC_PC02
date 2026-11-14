
package com.example.nasaapp.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.nasaapp.presentation.nasa.*

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()
    val vm: NasaViewModel = viewModel()

    val entrada by navController.currentBackStackEntryAsState()
    val ruta = entrada?.destination?.route

    Scaffold(
        bottomBar = {
            if (ruta == "imagenes" || ruta == "asteroides") {
                NavigationBar {
                    NavigationBarItem(
                        selected = ruta == "imagenes",
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
                        selected = ruta == "asteroides",
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
                ApodScreen(
                    vm = vm,
                    favoritos = false,
                    onVolver = {},
                    onFavoritos = {
                        navController.navigate("imagenes/favoritos")
                    },
                    onDetalle = { fecha ->
                        navController.navigate("imagenes/detalle/$fecha")
                    }
                )
            }

            composable("imagenes/favoritos") {
                ApodScreen(
                    vm = vm,
                    favoritos = true,
                    onVolver = { navController.popBackStack() },
                    onFavoritos = {},
                    onDetalle = { fecha ->
                        navController.navigate("imagenes/detalle/$fecha")
                    }
                )
            }

            composable("imagenes/detalle/{fecha}") { entry ->
                ApodDetailScreen(
                    vm = vm,
                    fecha = entry.arguments?.getString("fecha").orEmpty(),
                    onVolver = { navController.popBackStack() }
                )
            }

            composable("asteroides") {
                AsteroideScreen(
                    vm = vm,
                    favoritos = false,
                    onVolver = {},
                    onFavoritos = {
                        navController.navigate("asteroides/favoritos")
                    },
                    onDetalle = { id, fecha ->
                        navController.navigate("asteroides/detalle/$id/$fecha")
                    }
                )
            }

            composable("asteroides/favoritos") {
                AsteroideScreen(
                    vm = vm,
                    favoritos = true,
                    onVolver = { navController.popBackStack() },
                    onFavoritos = {},
                    onDetalle = { id, fecha ->
                        navController.navigate("asteroides/detalle/$id/$fecha")
                    }
                )
            }

            composable("asteroides/detalle/{id}/{fecha}") { entry ->
                AsteroideDetailScreen(
                    vm = vm,
                    id = entry.arguments?.getString("id").orEmpty(),
                    fecha = entry.arguments?.getString("fecha").orEmpty(),
                    onVolver = { navController.popBackStack() }
                )
            }
        }
    }
}
