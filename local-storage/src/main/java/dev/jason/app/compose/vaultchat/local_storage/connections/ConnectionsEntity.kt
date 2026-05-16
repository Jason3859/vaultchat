package dev.jason.app.compose.vaultchat.local_storage.connections

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.jason.app.compose.vaultchat.core.domain.User

@Entity(tableName = "connections")
data class ConnectionsEntity(
    @PrimaryKey
    val id: String,
    val displayName: String,
    val profilePictureUrl: String,
    val status: User.Status
)

fun User.toEntity() = ConnectionsEntity(uid, displayName, profilePictureUrl, status)
fun ConnectionsEntity.toUser() = User(id, displayName, profilePictureUrl, emptyList(), status)