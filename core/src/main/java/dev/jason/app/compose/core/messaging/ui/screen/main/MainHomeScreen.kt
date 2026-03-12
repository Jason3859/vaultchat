package dev.jason.app.compose.core.messaging.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainHomeScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(10) { index ->
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("User: $index")
            }
        }
    }
}
