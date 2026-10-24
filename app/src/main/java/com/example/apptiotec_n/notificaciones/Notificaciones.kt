package com.example.apptiotec_n.notificaciones

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

const val CANAL_RECORDATORIOS = "recordatorios"

fun crearCanalRecordatorios(contexto: Context) {
    val canal = NotificationChannel(
        CANAL_RECORDATORIOS,
        "Recordatorios",
        NotificationManager.IMPORTANCE_DEFAULT
    )
    canal.description = "Avisos para volver a evaluar tus hábitos digitales"

    val administrador = contexto.getSystemService(NotificationManager::class.java)
    administrador.createNotificationChannel(canal)
}

fun tienePermisoDeNotificaciones(contexto: Context): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true

    val estado = ContextCompat.checkSelfPermission(
        contexto,
        Manifest.permission.POST_NOTIFICATIONS
    )
    return estado == PackageManager.PERMISSION_GRANTED
}

fun mostrarRecordatorio(contexto: Context, hora: String) {
    if (!tienePermisoDeNotificaciones(contexto)) return

    val notificacion = NotificationCompat.Builder(contexto, CANAL_RECORDATORIOS)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Es hora de revisar tus hábitos")
        .setContentText("Tu recordatorio está configurado para las $hora.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(contexto).notify(1, notificacion)
}
