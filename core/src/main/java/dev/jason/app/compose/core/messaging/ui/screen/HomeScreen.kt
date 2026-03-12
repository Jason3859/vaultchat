package dev.jason.app.compose.core.messaging.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jason.app.compose.core.messaging.ui.screen.main.MainHomeScreen
import dev.jason.app.compose.core.messaging.ui.screen.search.SearchUsersScreen
import dev.jason.app.compose.core.messaging.ui.screen.search.SearchUsersViewModel
import dev.jason.app.compose.core.ui.theme.VaultChatTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen() {

    val topAppBarTexts = listOf("Home", "Search", "You")
    var bottomBarIndex by rememberSaveable { mutableIntStateOf(0) }

    val searchUsersViewModel: SearchUsersViewModel = koinViewModel()
    val searchUsersUiState by searchUsersViewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomBar(bottomBarIndex, { bottomBarIndex = it }) },
        topBar = { TopBar(topAppBarTexts[bottomBarIndex]) }
    ) { innerPadding ->
        if (bottomBarIndex == 0) {
            MainHomeScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            SearchUsersScreen(
                uiState = searchUsersUiState,
                updateState = searchUsersViewModel::updateState,
                onSearch = { searchUsersViewModel.getAndUpdateUsers() },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(text: String) {
    TopAppBar(
        title = { Text(text) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    )
}

data class BottomBarIcon(
    val filled: ImageVector,
    val outlined: ImageVector,
)

@Composable
fun BottomBar(currentScreenIndex: Int, onScreenIndexChange: (Int) -> Unit, modifier: Modifier = Modifier) {
    BottomAppBar(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf(
                BottomBarIcon(
                    filled = Icons.Default.Home,
                    outlined = Icons.Outlined.Home
                ),
                BottomBarIcon(
                    filled = Icons.Default.Search,
                    outlined = Icons.Outlined.Search
                ),
                BottomBarIcon(
                    filled = Icons.Default.AccountCircle,
                    outlined = Icons.Outlined.AccountCircle
                ),
            ).forEachIndexed { index, icon ->
                IconButton(onClick = { onScreenIndexChange(index) }) {
                    Icon(
                        imageVector = if (currentScreenIndex == index) icon.filled else icon.outlined,
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    VaultChatTheme {
        HomeScreen()
    }
}