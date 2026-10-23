package com.example.apptiotec_n.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.apptiotec_n.navigation.Screen
import com.example.apptiotec_n.viewmodel.MainViewModel
import com.example.apptiotec_n.viewmodel.ResultadoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultadoScreen(
    navController: NavController,
    viewModel: MainViewModel,
    resultadoViewModel: ResultadoViewModel = viewModel()
) {
    val estado by resultadoViewModel.uiState.collectAsState()

    val colorRiesgo = when (estado.riesgo) {
        "Bajo" -> Color(0xFF2E7D32)
        "Medio" -> Color(0xFFF9A825)
        else -> Color(0xFFC62828)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resultado") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (estado.cargando) {
                CircularProgressIndicator(modifier = Modifier.padding(32.dp))
                return@Column
            }

            Text(
                text = "Tu nivel de riesgo",
                style = MaterialTheme.typography.titleMedium
            )

            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(colorRiesgo, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${estado.puntaje}",
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = estado.riesgo,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }

            LinearProgressIndicator(
                progress = { estado.puntaje / 100f },
                color = colorRiesgo,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )

            Text(
                text = "Puntaje sobre 100. Sobre 80 el riesgo es bajo, entre 50 y 79 es medio, bajo 50 es alto.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Text(
                text = "Evaluación del " + SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(Date(estado.fecha)),
                style = MaterialTheme.typography.bodySmall
            )

            HorizontalDivider()

            Text(
                text = "Acciones para mejorar",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedVisibility(visible = !estado.cargando && estado.acciones.isEmpty()) {
                Text(
                    text = "No quedaron acciones pendientes. Buen trabajo.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            AnimatedVisibility(visible = estado.acciones.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    estado.acciones.forEach { accion ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = accion.completada,
                                    onCheckedChange = { marcada ->
                                        resultadoViewModel.marcarAccion(accion.id, marcada)
                                    }
                                )
                                Text(
                                    text = accion.texto,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { viewModel.navigateTo(Screen.Home) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Volver al inicio")
            }
        }
    }
}
