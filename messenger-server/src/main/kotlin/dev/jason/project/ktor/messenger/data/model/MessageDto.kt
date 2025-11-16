package dev.jason.project.ktor.messenger.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: Long,
    val roomId: String,
    val sender: UserDto,
    val message: String,
    val timestamp: Long
)