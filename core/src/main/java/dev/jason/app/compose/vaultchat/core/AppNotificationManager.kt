package dev.jason.app.compose.vaultchat.core

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.content.getSystemService

class AppNotificationManager(private val notificationChannelConfig: AppNotificationChannelConfig) {

    fun showNotification(context: Context, notification: Notification) {
        val notificationManager = context.getSystemService<NotificationManager>()!!
        val notificationChannel = notificationChannelConfig.notificationChannel(NotificationManager.IMPORTANCE_HIGH)

        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}