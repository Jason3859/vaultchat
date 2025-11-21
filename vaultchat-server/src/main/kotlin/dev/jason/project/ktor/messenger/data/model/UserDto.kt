package dev.jason.project.ktor.messenger.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val name: String,
    val profilePictureUrl: String?
)
