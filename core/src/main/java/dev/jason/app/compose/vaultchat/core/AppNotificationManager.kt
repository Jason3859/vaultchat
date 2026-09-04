package dev.jason.app.compose.vaultchat.core

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.content.getSystemService

class AppNotificationManager(
    private val context: Context
) {

    fun showNotification(notification: Notification, notificationChannel: AppNotificationChannelConfig) {
        val notificationManager = context.getSystemService<NotificationManager>()!!

        notificationManager.createNotificationChannel(notificationChannel.channel())
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}