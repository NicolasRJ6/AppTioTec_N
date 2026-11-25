package com.example.apptiotec_n.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apptiotec_n.data.model.ConsejoServidor
import com.example.apptiotec_n.viewmodel.ConsejosServidorViewModel
import com.example.apptiotec_n.viewmodel.MainViewModel

/**
 * Lista los consejos guardados en el microservicio Spring Boot y permite
 * crearlos, editarlos y eliminarlos. Cada acción llama a la API REST
 * (GET, POST, PUT y DELETE sobre `/api/consejos`).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsejosServidorScreen(
    viewModel: MainViewModel,
    consejosViewModel: ConsejosServidorViewModel = viewModel()
) {
    val estado by consejosViewModel.uiState.collectAsState()

    var mostrarFormulario by remember { mutableStateOf(false) }
    var consejoEnEdicion by remember { mutableStateOf<ConsejoServidor?>(null) }
    var consejoAEliminar by remember { mutableStateOf<ConsejoServidor?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Consejos del microservicio") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    if (estado.cargando) {
                        Box(modifier = Modifier.padding(end = 16.dp)) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    } else {
                        IconButton(onClick = { consejosViewModel.cargar() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Actualizar")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    consejoEnEdicion = null
                    mostrarFormulario = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Agregar") }
            )
        }
    ) { innerPadding ->
        when {
            estado.cargando && estado.consejos.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            estado.error != null && estado.consejos.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.CloudOff,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = estado.error.orEmpty(),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { consejosViewModel.cargar() }) {
                        Text("Reintentar")
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Surface(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Cloud,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(
                                    text = "Estos consejos se guardan en el microservicio " +
                                        "Spring Boot mediante Retrofit (GET, POST, PUT y DELETE " +
                                        "sobre /api/consejos).",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }

                    if (estado.guardando) {
                        item {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    if (estado.error != null) {
                        item {
                            Surface(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = estado.error.orEmpty(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                    TextButton(onClick = { consejosViewModel.cargar() }) {
                                        Text("Reintentar")
                                    }
                                }
                            }
                        }
                    }

                    if (estado.consejos.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.Cloud,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "El servidor no tiene consejos todavía. " +
                                        "Usa el botón Agregar para crear el primero.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    items(
                        items = estado.consejos,
                        key = { consejo -> consejo.id }
                    ) { consejo ->
                        TarjetaConsejoServidor(
                            consejo = consejo,
                            habilitado = !estado.guardando,
                            onEditar = {
                                consejoEnEdicion = consejo
                                mostrarFormulario = true
                            },
                            onEliminar = { consejoAEliminar = consejo }
                        )
                    }
                }
            }
        }
    }

    if (mostrarFormulario) {
        FormularioConsejoDialog(
            consejo = consejoEnEdicion,
            onCancelar = { mostrarFormulario = false },
            onGuardar = { consejo ->
                consejosViewModel.guardar(consejo)
                mostrarFormulario = false
            }
        )
    }

    consejoAEliminar?.let { consejo ->
        ConfirmarEliminarDialog(
            consejo = consejo,
            onCancelar = { consejoAEliminar = null },
            onConfirmar = {
                consejosViewModel.eliminar(consejo.id)
                consejoAEliminar = null
            }
        )
    }
}

@Composable
private fun TarjetaConsejoServidor(
    consejo: ConsejoServidor,
    habilitado: Boolean,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "#${consejo.id} · ${consejo.titulo}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = consejo.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onEditar, enabled = habilitado) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onEliminar, enabled = habilitado) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun FormularioConsejoDialog(
    consejo: ConsejoServidor?,
    onCancelar: () -> Unit,
    onGuardar: (ConsejoServidor) -> Unit
) {
    var titulo by remember(consejo) { mutableStateOf(consejo?.titulo.orEmpty()) }
    var descripcion by remember(consejo) {
        mutableStateOf(consejo?.descripcion.orEmpty())
    }
    val valido = titulo.isNotBlank() && descripcion.isNotBlank()

    AlertDialog(
        onDismissRequest = onCancelar,
        title = {
            Text(if (consejo == null) "Nuevo consejo" else "Editar consejo")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onGuardar(
                        ConsejoServidor(
                            id = consejo?.id ?: 0L,
                            titulo = titulo.trim(),
                            descripcion = descripcion.trim()
                        )
                    )
                },
                enabled = valido
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun ConfirmarEliminarDialog(
    consejo: ConsejoServidor,
    onCancelar: () -> Unit,
    onConfirmar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Eliminar consejo") },
        text = {
            Text(
                "¿Quieres eliminar \"${consejo.titulo}\" del servidor? " +
                    "Esta acción no se puede deshacer."
            )
        },
        confirmButton = {
            Button(onClick = onConfirmar) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    )
}
