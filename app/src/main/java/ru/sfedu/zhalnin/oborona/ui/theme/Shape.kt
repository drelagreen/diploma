package ru.sfedu.zhalnin.oborona.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

internal val LocalShapes = staticCompositionLocalOf { AppShapes() }

data class AppShapes(
    val small: RoundedCornerShape = RoundedCornerShape(4.dp),
    val medium: RoundedCornerShape = RoundedCornerShape(12.dp),
    val large: RoundedCornerShape = RoundedCornerShape(16.dp),

    val button: RoundedCornerShape = RoundedCornerShape(12.dp),

    val backgroundShape: RoundedCornerShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
)