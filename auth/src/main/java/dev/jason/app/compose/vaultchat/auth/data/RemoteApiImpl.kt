package dev.jason.app.compose.vaultchat.auth.data

class RemoteApiImpl(private val api: RetrofitApi) : RemoteApi {
    override suspend fun addUserToServer(body: User) {
        api.addUser(body.toDto())
    }
}