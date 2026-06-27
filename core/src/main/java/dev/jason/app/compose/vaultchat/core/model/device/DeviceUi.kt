package dev.jason.app.compose.vaultchat.core.model.device

import androidx.compose.runtime.Immutable

@Immutable
data class DeviceUi(
    val ownerUid: String,
    val name: String,
    val type: Device.Type,
    val os: Device.Os,
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

fun DeviceUi.toDevice() = Device(
    ownerUid = ownerUid,
    name = name,
    type = type,
    os = os,
    version = version,
    token = token
)