package dev.jason.app.compose.vaultchat.ui.abstractt.messaging.model

import dev.jason.app.compose.vaultchat.core.model.User
import dev.jason.app.compose.vaultchat.core.model.User.Status

data class UserUi(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val status: Status
)

fun User.toUi() = UserUi(
    uid = uid,
    displayName = displayName,
    profilePictureUrl = profilePictureUrl,
    status = status
)