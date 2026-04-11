package dev.jason.app.compose.vaultchat.core.domain

import android.content.Context
import android.content.res.Configuration
import android.os.Build

data class User(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val devices: List<Device>,
    val status: Status
) {
    enum class Status {
        Online, Offline, Away
    }

    data class Device(
        val name: String,
        val type: Type,
        val version: String,
        val fcmToken: String,
    ) {
        enum class Type {
            Mobile, Tablet
        }

        companion object {
            fun getCurrentDevice(context: Context, token: String): Device {
                return Device(
                    "${Build.MANUFACTURER} ${Build.MODEL}",
                    version = "${Build.VERSION.RELEASE}",
                    fcmToken = token,
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
}