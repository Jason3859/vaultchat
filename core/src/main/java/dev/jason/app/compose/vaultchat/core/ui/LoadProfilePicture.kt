package dev.jason.app.compose.vaultchat.core.ui

import android.util.Log
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun LoadProfilePicture(
    url: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val internalModifier = Modifier
        .clip(CircleShape)
        .then(modifier)

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(context)
            .data(url)
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build(),
        contentDescription = null,
        modifier = internalModifier,
        error = {
            Log.e("LoadProfilePicture", "LoadProfilePicture: error while loading image", it.result.throwable)

            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = internalModifier
            )
        }
    )
}