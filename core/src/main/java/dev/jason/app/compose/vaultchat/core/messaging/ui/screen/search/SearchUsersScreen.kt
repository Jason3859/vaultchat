package dev.jason.app.compose.vaultchat.core.messaging.ui.screen.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUsersScreen(
    uiState: SearchUsersViewModel.UiState,
    updateState: (SearchUsersViewModel.UiState) -> Unit,
    onSearch: (String) -> Unit,
    onUserClick: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            windowInsets = WindowInsets(0, 0, 0, 0),
            inputField = {
                SearchBarDefaults.InputField(
                    query = uiState.searchQuery,
                    onQueryChange = { updateState(uiState.copy(searchQuery = it)) },
                    onSearch = onSearch,
                    expanded = uiState.expanded,
                    onExpandedChange = { updateState(uiState.copy(expanded = it)) },
                    placeholder = { Text("Search") }
                )
            },
            expanded = uiState.expanded,
            onExpandedChange = { updateState(uiState.copy(expanded = it)) },
        ) {
            if (!uiState.isLoading) {
                LazyColumn {
                    items(uiState.searchResults) { user ->
                        ListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onUserClick(user)
                                },
                            headlineContent = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    SubcomposeAsyncImage(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape),
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(user.profilePictureUrl)
                                            .crossfade(true)
                                            .build(),
                                        loading = {
                                            CircularProgressIndicator()
                                        },
                                        error = {
                                            Image(Icons.Default.AccountCircle, null)
                                            Log.w(
                                                "SearchUsersScreen",
                                                "SearchUsersScreen: error while loading image",
                                                it.result.throwable
                                            )
                                        },
                                        contentDescription = null,
                                    )

                                    Spacer(Modifier.width(16.dp))

                                    Text(
                                        text = user.displayName
                                    )
                                }
                            }
                        )
                    }
                }
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}