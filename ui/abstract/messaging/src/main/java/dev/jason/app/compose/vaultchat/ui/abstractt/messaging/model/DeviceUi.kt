package dev.jason.app.compose.vaultchat.ui.abstractt.messaging.model

import dev.jason.app.compose.vaultchat.core.model.Device
import dev.jason.app.compose.vaultchat.core.model.Device.Os
import dev.jason.app.compose.vaultchat.core.model.Device.Type

data class DeviceUi(
    val ownerUid: String,
    val name: String,
    val type: Type,
    val os: Os,
    val version: String,
    val token: String,
)

fun Device.toUi() = DeviceUi(
    ownerUid = ownerUid,
    name = name,
    type = type,
    os = os,
    version = version,
    token = token
)