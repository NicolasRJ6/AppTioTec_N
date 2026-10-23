package com.example.apptiotec_n.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.apptiotec_n.model.Pregunta
import com.example.apptiotec_n.navigation.Screen
import com.example.apptiotec_n.viewmodel.EvaluacionViewModel
import com.example.apptiotec_n.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluacionScreen(
    navController: NavController,
    viewModel: MainViewModel,
    evaluacionViewModel: EvaluacionViewModel = viewModel()
) {
    val estado by evaluacionViewModel.uiState.collectAsState()

    LaunchedEffect(estado.terminada) {
        if (estado.terminada) {
            viewModel.navigateTo(Screen.Resultado)
            evaluacionViewModel.reiniciar()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Evaluación") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Revisemos tus hábitos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Responde todas las preguntas. No hay respuestas incorrectas, solo sirve para saber qué mejorar.",
                style = MaterialTheme.typography.bodyMedium
            )

            LinearProgressIndicator(
                progress = { estado.progreso },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Text(
                text = "${estado.respondidas} de ${estado.total} respondidas",
                style = MaterialTheme.typography.bodySmall
            )

            Pregunta.lista.forEach { pregunta ->
                val seleccionada = estado.respuestas[pregunta.id]
                val tieneError = estado.errores.contains(pregunta.id)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (tieneError) {
                            MaterialTheme.colorScheme.errorContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = pregunta.categoria,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = pregunta.texto,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Pregunta.opciones.forEach { (etiqueta, valor) ->
                                FilterChip(
                                    selected = seleccionada == valor,
                                    onClick = { evaluacionViewModel.responder(pregunta.id, valor) },
                                    label = { Text(etiqueta) }
                                )
                            }
                        }
                        AnimatedVisibility(visible = tieneError) {
                            Text(
                                text = "Falta responder esta pregunta",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(visible = estado.mensajeError != null) {
                Text(
                    text = estado.mensajeError ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = { evaluacionViewModel.finalizar() },
                enabled = !estado.guardando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(if (estado.guardando) "Guardando..." else "Ver mi resultado")
            }
        }
    }
}
