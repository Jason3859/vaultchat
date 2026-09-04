package dev.jason.app.compose.vaultchat.core

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import dev.jason.app.compose.vaultchat.core.AppConstants.ACTION_START_MAIN_ACTIVITY
import dev.jason.app.compose.vaultchat.core.AppConstants.EXTRA_NAV_DESTINATION_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessagesNotificationChannelConfig(private val context: Context) :
    AppNotificationChannelConfig(
        context.getString(R.string.messages_channel_id),
        context.getString(R.string.messages_channel_name)
    ) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun showNotification(vararg args: Any?) {
        val title = "New message"
        val from = args[0] as? String ?: return
        val content = args[1] as? String ?: return

        val intent = Intent(ACTION_START_MAIN_ACTIVITY).apply {
            putExtra(EXTRA_NAV_DESTINATION_KEY, "messaging")
            putExtra("uid", from)

            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, flags)

        val notificationManager = AppNotificationManager(context)
        val notification = NotificationCompat.Builder(context, id)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // FIXME: to be replaced
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.showNotification(notification, this)
    }

    override fun channel(): android.app.NotificationChannel {
        return android.app.NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
    }

    init {
        coroutineScope.launch {
            AppEvents.events.collect { event ->
                if (event is AppEvent.ShowNotification) {
                    val (title, text, channel, extras) = event

                    if (channel == NotificationChannel.MESSAGES) {
                        // extras has `from` value
                        showNotification(extras, text)
                    }
                }
            }
        }
    }
}