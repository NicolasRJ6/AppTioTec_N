package com.example.apptiotec_n.ui.screens

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.apptiotec_n.navigation.Screen
import com.example.apptiotec_n.ui.utils.copiarImagenAlPerfil
import com.example.apptiotec_n.viewmodel.MainViewModel
import com.example.apptiotec_n.viewmodel.PerfilViewModel
import kotlinx.coroutines.delay
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: MainViewModel,
    perfilViewModel: PerfilViewModel = viewModel()
) {
    var selectedItem by remember { mutableIntStateOf(1) }
    val estado by perfilViewModel.uiState.collectAsState()
    val niveles = listOf("Básico", "Intermedio", "Avanzado")
    val contexto = LocalContext.current

    val selectorImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val ruta = copiarImagenAlPerfil(contexto, uri)
            if (ruta != null) {
                perfilViewModel.cambiarImagen(ruta)
            }
        }
    }

    val fotoPerfil = remember(estado.imagenUri, estado.imagenVersion) {
        estado.imagenUri?.let { ruta ->
            if (File(ruta).exists()) {
                BitmapFactory.decodeFile(ruta)?.asImageBitmap()
            } else {
                null
            }
        }
    }

    LaunchedEffect(estado.perfilGuardado) {
        if (estado.perfilGuardado) {
            delay(1200)
            viewModel.navigateTo(Screen.Home)
            perfilViewModel.avisoMostrado()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi perfil") })
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = (selectedItem == 0),
                    onClick = {
                        selectedItem = 0
                        viewModel.navigateTo(Screen.Home)
                    },
                    label = { Text("Inicio") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") }
                )
                NavigationBarItem(
                    selected = (selectedItem == 1),
                    onClick = { selectedItem = 1 },
                    label = { Text("Perfil") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") }
                )
            }
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
                text = "Perfil del usuario",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Estos datos se guardan en tu teléfono y se usan para adaptar las recomendaciones.",
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = estado.alias,
                onValueChange = { perfilViewModel.cambiarAlias(it) },
                label = { Text("Alias") },
                placeholder = { Text("Ej: seba_dev") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                isError = estado.errores.alias != null,
                supportingText = {
                    Text(estado.errores.alias ?: "Nombre que verás dentro de la app")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Nivel de experiencia digital",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                niveles.forEach { nivel ->
                    FilterChip(
                        selected = estado.nivelExperiencia == nivel,
                        onClick = { perfilViewModel.cambiarNivelExperiencia(nivel) },
                        label = { Text(nivel) }
                    )
                }
            }
            estado.errores.nivelExperiencia?.let { mensaje ->
                Text(
                    text = mensaje,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                value = estado.dispositivo,
                onValueChange = { perfilViewModel.cambiarDispositivo(it) },
                label = { Text("Dispositivo") },
                placeholder = { Text("Ej: Galaxy A54") },
                leadingIcon = { Icon(Icons.Default.PhoneAndroid, contentDescription = null) },
                isError = estado.errores.dispositivo != null,
                supportingText = {
                    Text(estado.errores.dispositivo ?: "Modelo del teléfono que usas")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.sistemaOperativo,
                onValueChange = { perfilViewModel.cambiarSistemaOperativo(it) },
                label = { Text("Sistema operativo") },
                placeholder = { Text("Ej: Android 14") },
                leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null) },
                isError = estado.errores.sistemaOperativo != null,
                supportingText = {
                    Text(estado.errores.sistemaOperativo ?: "Versión del sistema")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Foto de perfil (opcional)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (fotoPerfil != null) {
                    Image(
                        bitmap = fotoPerfil,
                        contentDescription = "Foto de perfil elegida",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                    )
                }

                OutlinedButton(onClick = { selectorImagen.launch("image/*") }) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (fotoPerfil == null) "Elegir de la galería" else "Cambiar foto")
                }
            }

            Button(
                onClick = { perfilViewModel.guardarPerfil() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Guardar perfil")
            }

            AnimatedVisibility(visible = estado.mensajeError != null) {
                Text(
                    text = estado.mensajeError ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            AnimatedVisibility(visible = estado.perfilGuardado) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                        Text(
                            text = "Perfil guardado correctamente",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
