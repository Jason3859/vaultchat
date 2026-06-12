package dev.jason.app.compose.vaultchat.core.model

import android.content.Context
import android.content.res.Configuration
import android.os.Build

data class Device(
    val ownerUid: String,
    val name: String,
    val type: Type,
    val os: Os,
    val version: String,
    val token: String,
) {

    override fun equals(other: Any?): Boolean {
        if (other !is Device) return false
        return other.name == name &&
                other.type == type &&
                other.os == os &&
                other.version == version
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + os.hashCode()
        result = 31 * result + version.hashCode()
        result = 31 * result + token.hashCode()
        return result
    }

    enum class Os {
        Android // currently only android is supported
    }

    enum class Type {
        Mobile, Tablet
    }

    companion object {
        fun getCurrentDevice(context: Context, ownerUid: String, token: String): Device {
            return Device(
                ownerUid = ownerUid,
                name = "${Build.MANUFACTURER} ${Build.MODEL}",
                os = Os.Android,
                version = "${Build.VERSION.RELEASE}",
                token = token,
                type = getDeviceType(context)
            )
        }

        fun getDeviceType(context: Context): Type {
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