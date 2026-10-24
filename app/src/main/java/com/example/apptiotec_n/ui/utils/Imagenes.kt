package com.example.apptiotec_n.ui.utils

import android.content.Context
import android.net.Uri
import java.io.File

fun copiarImagenAlPerfil(contexto: Context, origen: Uri): String? {
    val destino = File(contexto.filesDir, "foto_perfil.jpg")

    return try {
        contexto.contentResolver.openInputStream(origen)?.use { flujoEntrada ->
            destino.outputStream().use { flujoSalida ->
                flujoEntrada.copyTo(flujoSalida)
            }
            destino.absolutePath
        }
    } catch (e: Exception) {
        null
    }
}
