package dev.jason.app.compose.vaultchat.ui.main.abstractt.home

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExpandedDockedSearchBarWithGap
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarWithGapState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.ui.main.abstractt.R
import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.UserUi
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AbstractHomeScreen(
    isOffline: Boolean,
    selectedUserUid: String?,
    uiState: HomeUiState,
    currentUserProfilePictureUrl: String?,
    onAction: (HomeUiAction) -> Unit,
    onUserClick: (UserUi) -> Unit,
    onNonConnectedUserClick: (UserUi) -> Unit,
    onProfileClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

    val searchBarState = rememberSearchBarWithGapState(initialValue = SearchBarValue.Collapsed)
    val inputField = @Composable {
        SearchBarDefaults.InputField(
            query = uiState.searchQuery,
            onQueryChange = { string ->
                onAction(
                    HomeUiAction.OnUiStateChange { it.copy(searchQuery = string) }
                )
            },
            onSearch = { onAction(HomeUiAction.Search) },
            expanded = searchBarState.currentValue != SearchBarValue.Collapsed,
            onExpandedChange = { expanded ->
                coroutineScope.launch {
                    if (expanded) {
                        searchBarState.animateToExpanded()
                    } else {
                        searchBarState.animateToCollapsed()
                        onAction(
                            HomeUiAction.OnUiStateChange {
                                it.copy(
                                    searchQuery = "",
                                    searchResults = persistentListOf()
                                )
                            }
                        )
                    }
                }
            },
            placeholder = { Text(stringResource(R.string.search_for_users)) },
            leadingIcon = { Icon(Icons.Default.Search, null) }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Box {
                AppBarWithSearch(
                    scrollBehavior = scrollBehavior,
                    state = searchBarState,
                    inputField = inputField,
                    actions = {
                        val imageModifier = Modifier.size(50.dp)

                        if (isOffline) {
                            Icon(Icons.Default.WifiOff, null)
                        }

                        IconButton(onProfileClick) {
                            SubcomposeAsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(currentUserProfilePictureUrl)
                                    .crossfade(true)
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .memoryCachePolicy(CachePolicy.ENABLED)
                                    .build(),
                                contentDescription = null,
                                modifier = imageModifier,
                                error = {
                                    Log.e(
                                        "HomeScreen",
                                        "HomeScreen: error while loading image",
                                        it.result.throwable
                                    )

                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = null,
                                        modifier = imageModifier
                                    )
                                }
                            )
                        }
                    }
                )

                ExpandedDockedSearchBarWithGap(
                    state = searchBarState,
                    inputField = inputField,
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn {
                        if (uiState.hasRequestedSearchAtLeastOnce) {
                            if (uiState.searchResults.isNotEmpty()) {
                                itemsIndexed(uiState.searchResults) { index, user ->
                                    UserItem(
                                        selected = false,
                                        count = uiState.searchResults.count(),
                                        user = user,
                                        onUserClick = onNonConnectedUserClick,
                                        index = index,
                                    )
                                }
                            } else {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(stringResource(R.string.no_users_found))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding + PaddingValues(12.dp)
        ) {
            if (uiState.connections.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.no_recent_connections))
                    }
                }
            } else {
                itemsIndexed(uiState.connections) { index, user ->
                    UserItem(
                        selected = selectedUserUid == user.uid,
                        count = uiState.connections.count(),
                        user = user,
                        onUserClick = onUserClick,
                        index = index
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun UserItem(
    selected: Boolean,
    count: Int,
    user: UserUi,
    onUserClick: (UserUi) -> Unit,
    index: Int
) {
    val profilePictureModifier = Modifier
        .size(40.dp)
        .clip(CircleShape)

    SegmentedListItem(
        modifier = Modifier.fillMaxWidth(),
        selected = selected,
        onClick = { onUserClick(user) },
        shapes = ListItemDefaults.segmentedShapes(
            index = index,
            count = count
        ),
        leadingContent = {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.profilePictureUrl)
                    .crossfade(true)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = null,
                modifier = profilePictureModifier,
                error = {
                    Log.e(
                        "HomeScreen",
                        "HomeScreen: exception while loading image",
                        it.result.throwable
                    )

                    Icon(Icons.Default.AccountCircle, null, profilePictureModifier)
                }
            )
        }
    ) {
        Text(user.displayName)
    }
}

private val previewContent = @Composable {
    VaultChatTheme {
        AbstractHomeScreen(
            isOffline = true,
            selectedUserUid = null,
            uiState = HomeUiState.asPreview(),
            currentUserProfilePictureUrl = "",
            onAction = {},
            onUserClick = { },
            onNonConnectedUserClick = {},
            onProfileClick = {}
        )
    }
}

@Preview
@Composable
private fun HomeScreenLightModePreview() {
    previewContent.invoke()
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun HomeScreenDarkModePreview() {
    previewContent.invoke()
}