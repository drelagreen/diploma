package ru.sfedu.zhalnin.oborona.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotApplyResult
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val White = Color(0xFFFFFFFF)
val Black = Color(0xFF1C1C1C)
val DarkGray = Color(0xFF747474)
val LightGray = Color(0xFFB8B8B8)
val LightestGray = Color(0xFFF1F1F1)
val Blue = Color(0xFF2660A7)
val AcidBlue = Color(0xFF2E2ECE)
val Red = Color(0xFFE10D3F)
val ErrorRed = Color(0xFFD53032)
val Transparent = Color(0x00000000)
val Success = Color(0xFF00C853)

fun lightPalette(): AppColors = AppColors(
    primary = Blue,
    onPrimary = White,
    primaryVariant = AcidBlue,
    secondary = Red,
    onSecondary = White,
    background = White,
    onBackground = Black,
    onBackgroundVariant = DarkGray,
    error = ErrorRed,
    onError = White,
    disabled = LightGray,
    onDisabled = White,
    transparent = Transparent,
    success = Success,
    card = LightestGray,
    isLight = true
)


internal val LocalColors = staticCompositionLocalOf { lightPalette() }

class AppColors(
    primary: Color,
    onPrimary: Color,
    primaryVariant: Color,
    secondary: Color,
    onSecondary: Color,
    background: Color,
    onBackground: Color,
    onBackgroundVariant: Color,
    error: Color,
    onError: Color,
    disabled: Color,
    onDisabled: Color,
    transparent: Color,
    success: Color,
    card: Color,
    isLight: Boolean
) {
    var primary by mutableStateOf(primary)
        private set

    var secondary by mutableStateOf(secondary)
        private set

    var background by mutableStateOf(background)
        private set

    var onPrimary by mutableStateOf(onPrimary)
        private set

    var onSecondary by mutableStateOf(onSecondary)
        private set

    var onBackground by mutableStateOf(onBackground)
        private set

    var onBackgroundVariant by mutableStateOf(onBackgroundVariant)
        private set

    var error by mutableStateOf(error)
        private set

    var onError by mutableStateOf(onError)
        private set

    var disabled by mutableStateOf(disabled)
        private set

    var onDisabled by mutableStateOf(onDisabled)
        private set

    var transparent by mutableStateOf(transparent)
        private set

    var isLight by mutableStateOf(isLight)
        private set

    var primaryVariant by mutableStateOf(primaryVariant)
        private set

    var success by mutableStateOf(success)
        private set

    var card by mutableStateOf(card)
        private set

    fun copy(
        primary: Color = this.primary,
        onPrimary: Color = this.onPrimary,
        primaryVariant: Color = this.primaryVariant,
        secondary: Color = this.secondary,
        onSecondary: Color = this.onSecondary,
        background: Color = this.background,
        onBackground: Color = this.onBackground,
        onBackgroundVariant: Color = this.onBackgroundVariant,
        error: Color = this.error,
        onError: Color = this.onError,
        disabled: Color = this.disabled,
        onDisabled: Color = this.onDisabled,
        transparent: Color = this.transparent,
        success: Color = this.success,
        card: Color = this.card,
        isLight: Boolean = this.isLight
    ) = AppColors(
        primary = primary,
        onPrimary = onPrimary,
        primaryVariant = primaryVariant,
        secondary = secondary,
        onSecondary = onSecondary,
        background = background,
        onBackground = onBackground,
        onBackgroundVariant = onBackgroundVariant,
        error = error,
        onError = onError,
        disabled = disabled,
        onDisabled = onDisabled,
        transparent = transparent,
        success = success,
        card = card,
        isLight = isLight
    )

    fun update(other: AppColors) {
        primary = other.primary
        onPrimary = other.onPrimary
        primaryVariant = other.primaryVariant
        secondary = other.secondary
        onSecondary = other.onSecondary
        background = other.background
        onBackground = other.onBackground
        onBackgroundVariant = other.onBackgroundVariant
        error = other.error
        onError = other.onError
        disabled = other.disabled
        onDisabled = other.onDisabled
        transparent = other.transparent
        success = other.success
        card = other.card
        isLight = other.isLight
    }
}

