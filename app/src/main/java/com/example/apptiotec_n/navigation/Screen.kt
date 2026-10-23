package com.example.apptiotec_n.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home_screen")
    data object Profile : Screen("profile_screen")
    data object Settings : Screen("settings_screen")
    data object Evaluacion : Screen("evaluacion_screen")
    data object Resultado : Screen("resultado_screen")
}