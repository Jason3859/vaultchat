package dev.jason.app.compose.vaultchat.messaging.ui.home

import dev.jason.app.compose.vaultchat.core.model.Device
import dev.jason.app.compose.vaultchat.core.model.User

data class HomeUiState(
    val connections: List<User> = emptyList(),
    val areConnectionsFetched: Boolean = false,
    val searchResults: List<User> = emptyList(),
    val searchQuery: String = "",
    val isSearchSuccessful: Boolean = false,
    val hasRequestedSearchAtLeastOnce: Boolean = false,
) {
    companion object {
        val defaultDevices = List(10) { index ->
            Device(
                ownerUid = "owner",
                name = "device @$index",
                type = if (index % 2 == 0) Device.Type.Mobile else Device.Type.Tablet,
                os = Device.Os.Android,
                version = "10",
                token = "token"
            )
        }

        val defaultUsers = List(20) { index ->
            User(
                uid = "uid-$index",
                displayName = "User @$index",
                profilePictureUrl = "url @$index",
                status = User.Status.Online
            )
        }

        fun asPreview(
            connections: List<User> = defaultUsers,
            hasRequestedSearchAtLeastOnce: Boolean = false,
            searchResults: List<User> = defaultUsers.subList(0, 10),
            searchQuery: String = ""
        ) = HomeUiState(
            connections = connections,
            hasRequestedSearchAtLeastOnce = hasRequestedSearchAtLeastOnce,
            searchResults = searchResults,
            searchQuery = searchQuery
        )
    }
}