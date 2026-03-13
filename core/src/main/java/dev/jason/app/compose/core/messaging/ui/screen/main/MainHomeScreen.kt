package dev.jason.app.compose.core.messaging.ui.screen.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainHomeScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(10) { index ->
            ListItem(
                headlineContent = {
                    Text("User $index")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            )
        }
    }
}
