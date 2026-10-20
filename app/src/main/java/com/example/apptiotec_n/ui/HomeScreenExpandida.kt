package com.example.apptiotec_n.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apptiotec_n.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenExpandida() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TíoTec (Expandida - Tablet horizontal / Desktop)") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            )
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo TíoTec",
                modifier = Modifier
                    .weight(1.2f)
                    .height(260.dp),
                contentScale = ContentScale.Fit
            )
            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Panel TíoTec Expandido",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Monitoreo integral y evaluación preventiva de dispositivos.")
                    Button(onClick = { }, modifier = Modifier.fillMaxWidth()) {
                        Text("Iniciar Evaluación")
                    }
                    OutlinedButton(onClick = { }, modifier = Modifier.fillMaxWidth()) {
                        Text("Registrar Dispositivo")
                    }
                }
            }
        }
    }
}

@Preview(name = "Expanded", widthDp = 840, heightDp = 600, showBackground = true)
@Composable
fun PreviewExpanded() {
    HomeScreenExpandida()
}