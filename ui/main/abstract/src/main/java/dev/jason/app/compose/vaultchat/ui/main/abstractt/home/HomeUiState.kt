package dev.jason.app.compose.vaultchat.ui.main.abstractt.home

import dev.jason.app.compose.vaultchat.core.model.device.Device
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.core.model.device.DeviceUi
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class HomeUiState(
    val connections: ImmutableList<UserUi> = persistentListOf(),
    val areConnectionsFetched: Boolean = false,
    val searchResults: ImmutableList<UserUi> = persistentListOf(),
    val searchQuery: String = "",
    val isSearchSuccessful: Boolean = false,
    val hasRequestedSearchAtLeastOnce: Boolean = false,
) {
    companion object {
        val defaultDevices = List(10) { index ->
            DeviceUi(
                ownerUid = "owner",
                name = "device @$index",
                type = if (index % 2 == 0) Device.Type.Mobile else Device.Type.Tablet,
                os = Device.Os.Android,
                version = "10",
                token = "token"
            )
        }.toImmutableList()

        val defaultUsers = List(20) { index ->
            UserUi(
                uid = "uid-$index",
                displayName = "User @$index",
                profilePictureUrl = "url @$index",
                status = User.Status.Online
            )
        }.toImmutableList()

        fun asPreview(
            connections: ImmutableList<UserUi> = defaultUsers,
            hasRequestedSearchAtLeastOnce: Boolean = false,
            searchResults: ImmutableList<UserUi> = defaultUsers.subList(0, 10),
            searchQuery: String = ""
        ) = HomeUiState(
            connections = connections,
            hasRequestedSearchAtLeastOnce = hasRequestedSearchAtLeastOnce,
            searchResults = searchResults,
            searchQuery = searchQuery
        )
    }
}