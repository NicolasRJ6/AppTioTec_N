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
fun HomeScreenMediana() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TíoTec (Mediana - Plegable / Tablet)") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    ) { innerPadding ->
        // En pantallas medianas mostramos 2 columnas en una fila (Row)
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo TíoTec",
                modifier = Modifier
                    .weight(1f)
                    .height(200.dp),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "TíoTec Preventivo",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Vista optimizada para pantallas medianas.")
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

@Preview(name = "Medium", widthDp = 600, heightDp = 900, showBackground = true)
@Composable
fun PreviewMedium() {
    HomeScreenMediana()
}