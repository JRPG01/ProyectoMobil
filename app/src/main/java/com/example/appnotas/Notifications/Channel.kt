package com.example.appnotas.Notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

fun createChannelNotification(idCanal: String, context: Context) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val nombre = "Tareas"
        val descripcion = "Canal de notificaciones de tareas"
        val importancia = NotificationManager.IMPORTANCE_DEFAULT
        val canal = NotificationChannel(idCanal, nombre, importancia)
            .apply {
                description = descripcion
            }

        val notificationManager: NotificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(canal)
    }
}