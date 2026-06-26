package dev.jason.app.compose.vaultchat.feature.open_links

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

class OpenLinksService(private val context: Context) {

    fun open(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        context.startActivity(intent)
    }
}