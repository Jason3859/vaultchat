package dev.jason.app.compose.vaultchat.core

abstract class AppNotificationChannelConfig(
    val id: String,
    val name: String
) {

    abstract fun channel(): android.app.NotificationChannel
    abstract fun showNotification(vararg args: Any?)
}