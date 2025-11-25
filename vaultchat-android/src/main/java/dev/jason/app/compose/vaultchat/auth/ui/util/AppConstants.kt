package dev.jason.app.compose.vaultchat.auth.ui.util

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal object AppConstants {

    val COMPACT_DP_VALUES = DynamicAppDpValues(50.dp, 18.sp, 27.dp, 8.dp, 16.dp)
    val LARGE_DP_VALUES = DynamicAppDpValues(70.dp, 25.sp, 30.dp, 16.dp, 24.dp)

    val STATIC_APP_DP_VALUES = StaticAppDpValues()

    data class DynamicAppDpValues(
        val height: Dp,
        val fontSize: TextUnit,
        val iconSize: Dp,
        val arrangementSpace: Dp,
        val emailFormContentPadding: Dp
    )

    data class StaticAppDpValues(
        val roundedCornerShape: Dp = 40.dp,
        val textFieldCornerShape: Dp = 15.dp
    )

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    val compactTextStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.headlineLargeEmphasized

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    val largeTextStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.displayMediumEmphasized
}