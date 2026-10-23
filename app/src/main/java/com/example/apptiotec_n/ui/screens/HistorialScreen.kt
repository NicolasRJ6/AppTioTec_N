package com.example.apptiotec_n.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.apptiotec_n.model.Accion
import com.example.apptiotec_n.model.Evaluacion
import com.example.apptiotec_n.navigation.Screen
import com.example.apptiotec_n.viewmodel.HistorialViewModel
import com.example.apptiotec_n.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    navController: NavController,
    viewModel: MainViewModel,
    historialViewModel: HistorialViewModel = viewModel()
) {
    val estado by historialViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial") },
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
            when {
                estado.cargando -> {
                    CircularProgressIndicator(modifier = Modifier.padding(32.dp))
                }

                estado.evaluaciones.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Todavía no hay evaluaciones guardadas",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Cuando termines tu primera evaluación, el resultado queda guardado acá.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        OutlinedButton(onClick = { viewModel.navigateTo(Screen.Evaluacion) }) {
                            Text("Hacer mi primera evaluación")
                        }
                    }
                }

                else -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Resumen(etiqueta = "Evaluaciones", valor = estado.total.toString())
                            Resumen(etiqueta = "Mejor", valor = estado.mejorPuntaje.toString())
                            Resumen(etiqueta = "Promedio", valor = estado.promedio.toString())
                        }
                    }

                    Text(
                        text = "Evaluaciones guardadas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    estado.evaluaciones.forEach { evaluacion ->
                        TarjetaEvaluacion(
                            evaluacion = evaluacion,
                            expandida = estado.idExpandido == evaluacion.id,
                            acciones = if (estado.idExpandido == evaluacion.id) {
                                estado.acciones
                            } else {
                                emptyList()
                            },
                            onAlternar = { historialViewModel.alternarDetalle(evaluacion.id) },
                            onMarcarAccion = { id, marcada ->
                                historialViewModel.marcarAccion(id, marcada)
                            }
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.navigateTo(Screen.Evaluacion) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Hacer una evaluación nueva")
                    }
                }
            }
        }
    }
}

@Composable
private fun Resumen(etiqueta: String, valor: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = valor,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun TarjetaEvaluacion(
    evaluacion: Evaluacion,
    expandida: Boolean,
    acciones: List<Accion>,
    onAlternar: () -> Unit,
    onMarcarAccion: (Long, Boolean) -> Unit
) {
    val formato = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    val color = when (evaluacion.riesgo) {
        "Bajo" -> Color(0xFF2E7D32)
        "Medio" -> Color(0xFFF9A825)
        else -> Color(0xFFC62828)
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAlternar() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(color, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${evaluacion.puntaje}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = formato.format(Date(evaluacion.fecha)),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Riesgo ${evaluacion.riesgo.lowercase()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = color
                    )
                }

                Icon(
                    imageVector = if (expandida) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expandida) "Ocultar detalle" else "Ver detalle"
                )
            }

            AnimatedVisibility(visible = expandida) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalDivider()

                    if (acciones.isEmpty()) {
                        Text(
                            text = "Esta evaluación no dejó acciones pendientes.",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        Text(
                            text = "Acciones de esta evaluación",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(start = 16.dp, top = 12.dp)
                        )
                        acciones.forEach { accion ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = accion.completada,
                                    onCheckedChange = { marcada ->
                                        onMarcarAccion(accion.id, marcada)
                                    }
                                )
                                Text(
                                    text = accion.texto,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
