package dev.jason.app.compose.vaultchat.core.domain

import android.content.Context
import android.content.res.Configuration
import android.os.Build

data class Device(
    val name: String,
    val type: Type,
    val os: Os,
    val version: String,
    val token: String,
) {
    enum class Os {
        Android // currently only android is supported
    }

    enum class Type {
        Mobile, Tablet
    }

    companion object {
        fun getCurrentDevice(context: Context, token: String): Device {
            return Device(
                name = "${Build.MANUFACTURER} ${Build.MODEL}",
                os = Os.Android,
                version = "${Build.VERSION.RELEASE}",
                token = token,
                type = getDeviceType(context)
            )
        }

        private fun getDeviceType(context: Context): Type {
            val screenLayout = context.resources.configuration.screenLayout
            val mask = Configuration.SCREENLAYOUT_SIZE_MASK
            val isTablet = (screenLayout and mask) >= Configuration.SCREENLAYOUT_SIZE_LARGE
            return if (isTablet) {
                Type.Tablet
            } else {
                Type.Mobile
            }
        }
    }
}