package dev.jason.app.compose.vaultchat.feature.connections

import android.util.Log
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.feature.connections.api.ConnectionsApiRepository
import dev.jason.app.compose.vaultchat.feature.connections.db.ConnectionsDbRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ConnectionsService(
    private val apiRepository: ConnectionsApiRepository,
    private val dbRepository: ConnectionsDbRepository
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private var isRequestSent = false

    suspend fun getConnection(uid: String): User? {
        return dbRepository.getConnection(uid)
    }

    fun getConnections(): Flow<List<User>> = channelFlow {

        launch {
            dbRepository.getConnections().collect {
                send(it)
            }
        }

        if (!isRequestSent) { // prevents from unnecessary calls
            try {
                val connections = apiRepository.getConnections()
                dbRepository.updateConnections(connections)
                isRequestSent = true
            } catch (e: Exception) {
                Log.e("ConnectionsService", "getConnections: background refresh failed", e)
            }
        }
    }

    /**
     * updates all connections existing connections in database.
     */
    suspend fun updateConnections(list: List<User>) {
        dbRepository.updateConnections(list)
    }

    suspend fun updateStatus(uid: String, status: User.Status) {
        dbRepository.updateStatus(uid, status)
    }

    init {
        coroutineScope.launch {
            AppEvents.events.collect { event ->
                if (event is AppEvent.UpdateConnectionStatus) {
                    updateStatus(event.uid, event.status)
                }

                if (event is AppEvent.UpdateConnections) {
                    updateConnections(event.connections)
                }

                if (event is AppEvent.ReFetchConnections) {
                    isRequestSent = false
                    getConnections().first()
                }
            }
        }

        coroutineScope.launch {
            AppEvents.requests.collect { request ->
                if (request is AppEvent.Request.GetConnectionRequest) {
                    val connection = getConnection(request.uid)
                    AppEvents.sendResponse(AppEvent.Response.GetConnectionResponse(connection))
                }
            }
        }
    }
}
