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

        if (!isRequestSent) { // prevents from unnecessary calls
            try {
                Log.d("ConnectionsService", "getConnections: fetching connections")
                val connections = apiRepository.getConnections()
                Log.d("ConnectionsService", "getConnections: completed fetching connections")
                Log.d("ConnectionsService", "getConnections: connections: $connections")
                dbRepository.updateConnections(connections)
                isRequestSent = true
            } catch (e: Exception) {
                Log.e("ConnectionsService", "getConnections: background refresh failed", e)
            }
        }
        
        launch {
            dbRepository.getConnections().collect {
                send(it)
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
                    Log.d("ConnectionsService", "init: collected app event to refetch connections")
                    isRequestSent = false
                    // Just collecting the flow once is enough to trigger the internal refresh logic
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
