package ru.sfedu.zhalnin.oborona.ui.common

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun IconToolbarButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconChecked: ImageVector,
    checked: Boolean = false,
    onClick: (Boolean) -> Unit = {},
) {
    val transition = updateTransition(checked, label = "checkedState")

    val aTint by transition.animateColor(label = "iconColor") { isChecked ->
        if (isChecked) AppTheme.colors.onSecondary else AppTheme.colors.onPrimary
    }

    val aSize by transition.animateDp(
        transitionSpec = {
            if (false isTransitioningTo true) {
                keyframes {
                    durationMillis = 250
                    24.dp at 0 with LinearOutSlowInEasing // for 0-15 ms
                    27.dp at 15 with FastOutLinearInEasing // for 15-75 ms
                    30.dp at 75 // ms
                    27.dp at 150 // ms
                }
            } else {
                spring(stiffness = Spring.StiffnessVeryLow)
            }
        },
        label = "Size"
    ) { 24.dp }

    IconToggleButton(
        modifier = modifier,
        checked = checked,
        onCheckedChange = { onClick(it) },
    ) {
        Icon(
            modifier = Modifier.padding(horizontal = 12.dp).size(aSize),
            imageVector = if (checked) iconChecked else icon,
            contentDescription = null,
            tint = aTint
        )
    }
}

@Composable
@Preview
fun ToolbarDrawingsButtonPreview() {
    AppTheme() {
        var checked by remember { mutableStateOf(false) }

        IconToolbarButton(
            icon = AppTheme.icons.HomeEmpty,
            iconChecked = AppTheme.icons.HomeFilled,
            checked = checked,
            onClick = { checked = !checked }
        )
    }
}


@Composable
@Preview
fun ToolbarDrawingsButtonCheckedPreview() {
    AppTheme() {
        IconToolbarButton(
            iconChecked = AppTheme.icons.HomeFilled,
            icon = AppTheme.icons.HomeEmpty,
            checked = true
        )
    }
}

