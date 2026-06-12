package dev.jason.app.compose.vaultchat.core

import android.content.Context
import android.widget.Toast
import org.koin.java.KoinJavaComponent.inject

object ToastController {

    private val context: Context by inject(Context::class.java)

    fun showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, text, duration).show()
    }

    fun showErrorOccurredToast() {
        showToast("An error occurred")
    }
}