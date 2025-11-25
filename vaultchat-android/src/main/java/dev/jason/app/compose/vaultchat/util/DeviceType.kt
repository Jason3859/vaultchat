package dev.jason.app.compose.vaultchat.util

import androidx.compose.material3.adaptive.HingeInfo
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.window.core.layout.WindowSizeClass

sealed class DeviceType : Comparable<DeviceType> {
    abstract val minWidth: Int
    abstract val minHeight: Int
    abstract val rank: Int

    override fun compareTo(other: DeviceType): Int = this.rank - other.rank

    data class Compact(
        override val minWidth: Int,
        override val minHeight: Int,
        override val rank: Int = 0,
    ) : DeviceType()

    data class Medium(
        override val minWidth: Int,
        override val minHeight: Int,
        override val rank: Int = 1,
    ) : DeviceType()

    data class Foldable(
        override val minWidth: Int,
        override val minHeight: Int,
        val isTabletop: Boolean,
        val hingeList: List<HingeInfo>,
        override val rank: Int = 2,
    ) : DeviceType()

    data class Expanded(
        override val minWidth: Int,
        override val minHeight: Int,
        override val rank: Int = 3,
    ) : DeviceType()

    data class Large(
        override val minWidth: Int,
        override val minHeight: Int,
        override val rank: Int = 4,
    ) : DeviceType()

    data class ExtraLarge(
        override val minWidth: Int,
        override val minHeight: Int,
        override val rank: Int = 5,
    ) : DeviceType()

    val isLandscapePhone: Boolean
        get() = minWidth > minHeight
                && minHeight < 500
                && (minWidth.toFloat() / minHeight.toFloat()) >= 1.8f

    companion object {
        fun fromWindowAdaptiveInfo(
            windowAdaptiveInfo: WindowAdaptiveInfo,
        ): DeviceType {
            val width = windowAdaptiveInfo.windowSizeClass.minWidthDp
            val height = windowAdaptiveInfo.windowSizeClass.minHeightDp

            val aspectRatio = width.toFloat() / height.toFloat()
            // Foldable check (remains first)
            if (windowAdaptiveInfo.windowPosture.hingeList.isNotEmpty())
                return Foldable(
                    minWidth = width,
                    minHeight = height,
                    isTabletop = windowAdaptiveInfo.windowPosture.isTabletop,
                    hingeList = windowAdaptiveInfo.windowPosture.hingeList
                )

            // Device classification based on width and aspect ratio
            return when {
                width >= WindowSizeClass.WIDTH_DP_EXTRA_LARGE_LOWER_BOUND -> ExtraLarge(width, height)
                width >= WindowSizeClass.WIDTH_DP_LARGE_LOWER_BOUND -> Large(width, height)
                width >= WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND -> Expanded(width, height)
                width >= WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND -> {
                    // For tablets: medium width (and not a very wide landscape phone)
                    if (aspectRatio < 1.6f) Medium(width, height)
                    else Compact(width, height) // Fallback to compact if extreme landscape
                }
                else -> Compact(width, height)
            }
        }
    }
}