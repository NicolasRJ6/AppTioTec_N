package com.example.apptiotec_n.ui.utils

import android.content.Context
import android.net.Uri
import java.io.File

fun copiarImagenAlPerfil(contexto: Context, origen: Uri): String? {
    val destino = File(contexto.filesDir, "foto_perfil.jpg")

    val entrada = contexto.contentResolver.openInputStream(origen) ?: return null
    entrada.use { flujoEntrada ->
        destino.outputStream().use { flujoSalida ->
            flujoEntrada.copyTo(flujoSalida)
        }
    }

    return destino.absolutePath
}
