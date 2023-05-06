package ru.sfedu.zhalnin.oborona.ui.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun OutlinedButton(
    modifier: Modifier = Modifier,
    text: String = "",
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    var clickPerformed by remember { mutableStateOf(System.currentTimeMillis()) }

    val transition = updateTransition(clickPerformed, label = "checkedState")

    val aPadding by transition.animateDp(
        transitionSpec = {
            keyframes {
                durationMillis = 250
                0.dp at 0 with LinearOutSlowInEasing // for 0-15 ms
                1.dp at 15 with FastOutLinearInEasing // for 15-75 ms
                2.dp at 75 // ms
                1.dp at 150 // ms
            }
        },
        label = "Size"
    ) { 0.dp }

    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 37.dp + aPadding),
        shape = AppTheme.shapes.button,
        border = BorderStroke(1.dp, AppTheme.colors.primary),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = AppTheme.colors.transparent,
            contentColor = AppTheme.colors.primary,
            disabledBackgroundColor = AppTheme.colors.transparent,
            disabledContentColor = AppTheme.colors.primary
        ),
        enabled = enabled,
        onClick = {
            clickPerformed = System.currentTimeMillis()
            onClick()
        },
    ) {
        Text(
            text = text.uppercase(),
            textAlign = TextAlign.Center,
            style = AppTheme.typography.button1,
            fontSize = (AppTheme.typography.button1.fontSize.value + aPadding.value).sp
        )
    }
}

@Composable
@Preview
fun OutlinedButtonPreviewEnabled() {
    AppTheme {
        OutlinedButton(text = "Active")
    }
}

@Composable
@Preview
fun OutlinedButtonPreviewDisabled() {
    AppTheme {
        OutlinedButton(
            text = "Disabled",
            enabled = false
        )
    }
}