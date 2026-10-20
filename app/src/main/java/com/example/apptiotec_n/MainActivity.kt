package com.example.apptiotec_n

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apptiotec_n.navigation.NavigationEvent
import com.example.apptiotec_n.navigation.Screen
import com.example.apptiotec_n.ui.screens.HomeScreen
import com.example.apptiotec_n.ui.screens.ProfileScreen
import com.example.apptiotec_n.ui.screens.SettingsScreen
import com.example.apptiotec_n.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: MainViewModel = viewModel()
                    val navController = rememberNavController()

                    // Escuchar eventos de navegación emitidos por el ViewModel
                    LaunchedEffect(key1 = Unit) {
                        viewModel.navigationEvents.collectLatest { event ->
                            when (event) {
                                is NavigationEvent.NavigateTo -> {
                                    navController.navigate(event.route.route) {
                                        event.popUpToRoute?.let {
                                            popUpTo(it.route) {
                                                inclusive = event.inclusive
                                            }
                                        }
                                        launchSingleTop = event.singleTop
                                        restoreState = true
                                    }
                                }
                                is NavigationEvent.PopBackStack -> navController.popBackStack()
                                is NavigationEvent.NavigateUp -> navController.navigateUp()
                            }
                        }
                    }

                    // Contenedor principal de rutas con Scaffold
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Home.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(route = Screen.Home.route) {
                                HomeScreen(navController = navController, viewModel = viewModel)
                            }
                            composable(route = Screen.Profile.route) {
                                ProfileScreen(navController = navController, viewModel = viewModel)
                            }
                            composable(route = Screen.Settings.route) {
                                SettingsScreen(navController = navController, viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}