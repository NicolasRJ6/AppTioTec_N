package com.example.apptiotec_n.viewmodel

data class AjustesUiState(
    val recordatorioActivo: Boolean = false,
    val horaRecordatorio: String = "20:00",
    val errorHora: String? = null,
    val guardado: Boolean = false
)
