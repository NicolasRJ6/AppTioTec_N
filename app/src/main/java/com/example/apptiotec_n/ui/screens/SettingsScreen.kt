package com.example.apptiotec_n.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.apptiotec_n.notificaciones.crearCanalRecordatorios
import com.example.apptiotec_n.notificaciones.tienePermisoDeNotificaciones
import com.example.apptiotec_n.viewmodel.AjustesViewModel
import com.example.apptiotec_n.viewmodel.MainViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: MainViewModel,
    ajustesViewModel: AjustesViewModel = viewModel()
) {
    val estado by ajustesViewModel.uiState.collectAsState()
    val contexto = LocalContext.current

    var permisoNotificaciones by remember {
        mutableStateOf(tienePermisoDeNotificaciones(contexto))
    }

    val pedirPermiso = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        permisoNotificaciones = concedido
        if (concedido) {
            ajustesViewModel.probarRecordatorio()
        }
    }

    LaunchedEffect(Unit) {
        crearCanalRecordatorios(contexto)
    }

    LaunchedEffect(estado.guardado) {
        if (estado.guardado) {
            delay(1500)
            ajustesViewModel.avisoMostrado()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Recordatorios",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Configura cuándo quieres que la app te recuerde volver a revisar tus hábitos digitales.",
                style = MaterialTheme.typography.bodyMedium
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.padding(end = 12.dp)) {
                        Text(
                            text = "Activar recordatorio",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Te avisa una vez al día para volver a evaluar.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Switch(
                        checked = estado.recordatorioActivo,
                        onCheckedChange = { activo ->
                            ajustesViewModel.cambiarRecordatorio(activo)
                        }
                    )
                }
            }

            OutlinedTextField(
                value = estado.horaRecordatorio,
                onValueChange = { valor -> ajustesViewModel.cambiarHora(valor) },
                label = { Text("Hora del recordatorio") },
                placeholder = { Text("20:00") },
                leadingIcon = {
                    Icon(Icons.Default.Notifications, contentDescription = null)
                },
                isError = estado.errorHora != null,
                supportingText = {
                    Text(estado.errorHora ?: "Formato de 24 horas, por ejemplo 20:00")
                },
                enabled = estado.recordatorioActivo,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { ajustesViewModel.guardar() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Guardar ajustes")
            }

            OutlinedButton(
                onClick = {
                    if (permisoNotificaciones) {
                        ajustesViewModel.probarRecordatorio()
                    } else {
                        pedirPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Probar recordatorio")
            }

            AnimatedVisibility(visible = estado.mensajeError != null) {
                Text(
                    text = estado.mensajeError ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            AnimatedVisibility(visible = estado.guardado) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = "Ajustes guardados en el dispositivo",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Text(
                text = "Los datos de esta aplicación son ficticios. No se guarda información real, sensible ni de menores.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
