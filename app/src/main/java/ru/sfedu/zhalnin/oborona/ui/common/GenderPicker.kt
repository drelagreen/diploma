package ru.sfedu.zhalnin.oborona.ui.common

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun GenderPicker(
    modifier: Modifier = Modifier,
    colorSelected: Color = AppTheme.colors.onPrimary,
    colorUnselected: Color = AppTheme.colors.onPrimary,
    backgroundColorSelected: Color = AppTheme.colors.primary,
    backgroundColorUnselected: Color = AppTheme.colors.disabled,
    onClick: (Boolean) -> Unit,
    isMaleChecked: Boolean = true,
) {
    val transition2 = updateTransition(isMaleChecked, label = "checkedState")

    val aMaleBackColor by transition2.animateColor(label = "iconColor") { isChecked ->
        if (isChecked) backgroundColorSelected else backgroundColorUnselected
    }

    val aFemaleBackColor by transition2.animateColor(label = "iconColor") { isChecked ->
        if (isChecked) backgroundColorUnselected else backgroundColorSelected
    }

    val aMaleContentColor by transition2.animateColor(label = "iconColor") { isChecked ->
        if (isChecked) colorSelected else colorUnselected
    }

    val aFemaleContentColor by transition2.animateColor(label = "iconColor") { isChecked ->
        if (isChecked) colorUnselected else colorSelected
    }

    Row(modifier = modifier) {
        PrimaryButton(
            modifier = modifier.weight(1f),
            text = stringResource(R.string.genderMale),
            backgroundColor = aMaleBackColor,
            contentColor = aMaleContentColor,
            onClick = { onClick(true) }
        )
        Spacer(modifier = Modifier.width(32.dp))
        PrimaryButton(
            modifier = modifier.weight(1f),
            text = stringResource(R.string.genderFemale),
            backgroundColor = aFemaleBackColor,
            contentColor = aFemaleContentColor,
            onClick = { onClick(false) }
        )
    }
}

@Preview
@Composable
fun GenderPickerPreview() {
    AppTheme {
        var isMaleChecked by remember { mutableStateOf(true) }

        LaunchedEffect(true) {
            while(true) {
                isMaleChecked = !isMaleChecked
                delay(1000)
            }
        }

        GenderPicker(
            modifier = Modifier.padding(16.dp),
            isMaleChecked = isMaleChecked,
            onClick = {}
        )
    }
}