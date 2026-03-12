package dev.jason.app.compose.vaultchat.auth.data

interface RemoteApi {

    suspend fun addUserToServer(body: User)
}