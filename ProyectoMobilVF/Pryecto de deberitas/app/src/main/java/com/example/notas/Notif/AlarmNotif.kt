package com.example.notas.Notif

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.notas.R
import com.example.notas.ui.theme.notificacionProgramada.Companion.NOTIFICACION_ID
import kotlin.random.Random

class AlarmNotif(): BroadcastReceiver() {
    companion object{
        var NOTIFICATION_ID = Random.nextInt(1,10000)
    }


    override fun onReceive(context: Context, intent: Intent) {
        val titulo = intent.getStringExtra("title")
        val time = intent.getStringExtra("fecha")


        val notificacion = NotificationCompat.Builder(context, "canalTarea")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Tarea pendiente")
            .setContentText("La tarea $titulo caducara el dia $time ")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("La tarea $titulo caduca el dia $time")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.notify(NOTIFICACION_ID++, notificacion)
    }
}