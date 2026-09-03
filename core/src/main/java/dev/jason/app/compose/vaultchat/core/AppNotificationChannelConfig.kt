package dev.jason.app.compose.vaultchat.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

data class AppNotificationChannelConfig(
    val id: String,
    val name: String,
    val description: String? = null
) {
    companion object {
        fun messagesNotificationChannel(context: Context) =
            AppNotificationChannelConfig(
                id = context.getString(R.string.messages_channel_id),
                name = context.getString(R.string.messages_channel_name)
            )
    }

    fun notificationChannel(
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
        enableVibration: Boolean = true
    ): NotificationChannel {
        return NotificationChannel(
            /* id = */ id,
            /* name = */ name,
            /* importance = */ importance
        ).apply {
            description = this@AppNotificationChannelConfig.description
            this.enableVibration(enableVibration)
        }
    }
}