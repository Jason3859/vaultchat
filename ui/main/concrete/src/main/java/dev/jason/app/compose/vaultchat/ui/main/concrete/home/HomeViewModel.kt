package dev.jason.app.compose.vaultchat.ui.main.concrete.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.NotificationChannel
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.model.user.toUi
import dev.jason.app.compose.vaultchat.feature.connections.ConnectionsService
import dev.jason.app.compose.vaultchat.feature.messages.MessageDatabaseService
import dev.jason.app.compose.vaultchat.feature.messaging.MessagingApiService
import dev.jason.app.compose.vaultchat.feature.user.UserApiService
import dev.jason.app.compose.vaultchat.ui.main.abstractt.home.HomeUiAction
import dev.jason.app.compose.vaultchat.ui.main.abstractt.home.HomeUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userApiService: UserApiService,
    private val connectionsService: ConnectionsService,
    private val messageDatabaseService: MessageDatabaseService,
    private val messagingApiService: MessagingApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()
        .onStart { updateConnections() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState()
        )

    fun onAction(action: HomeUiAction) {
        when (action) {
            is HomeUiAction.OnUiStateChange -> {
                _uiState.update { action.state(it) }
            }

            HomeUiAction.Search -> search()
        }
    }

    private fun search() {
        val searchQuery = _uiState.value.searchQuery
        if (searchQuery.isBlank()) return

        viewModelScope.launch {
            val searchResults = userApiService.searchUsers(searchQuery)
            _uiState.update {
                it.copy(
                    searchResults = searchResults.toUi(),
                    hasRequestedSearchAtLeastOnce = true
                )
            }
        }
    }

    private fun List<User>.toUi(): ImmutableList<UserUi> = map(User::toUi).toImmutableList()

    private fun updateConnections() {
        viewModelScope.launch {
            connectionsService.getConnections().collect { connections ->
                _uiState.update {
                    it.copy(
                        connections = connections.toUi(),
                        isLoading = true
                    )
                }
            }
        }
    }

    private suspend fun fetchAndAddMessagesToDatabase() {
        val localMessages = messageDatabaseService.getAllMessages()
        val fetchedMessages = messagingApiService.fetchMessages()
        val newMessages = fetchedMessages.filter { it !in localMessages }

        messageDatabaseService.addMessages(fetchedMessages)

        newMessages.forEach { message ->
            AppEvents.sendEvent(
                AppEvent.ShowNotification(
                    title = "New Message",
                    content = message.text,
                    channel = NotificationChannel.MESSAGES,
                    extras = listOf(message.from, message.fromDisplayName)
                )
            )
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            fetchAndAddMessagesToDatabase()
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}