package ru.sfedu.zhalnin.oborona.ui.common

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GlobalModalBottomSheet(
    modifier: Modifier = Modifier,
    state: GlobalModalBottomSheetState = rememberGlobalModalBottomSheetState(ModalBottomSheetValue.Hidden),
    shape: Shape = AppTheme.shapes.backgroundShape,
    elevation: Dp = 8.dp,
    backgroundColor: Color = AppTheme.colors.background,
    scrimColor: Color = AppTheme.colors.onBackground.copy(alpha = 0.6f),
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = state.sheetState,
        sheetShape = shape,
        sheetElevation = elevation,
        sheetBackgroundColor = backgroundColor,
        scrimColor = scrimColor,
        content = {
            CompositionLocalProvider(LocalGlobalModalBottomSheet provides state) {
                content()
            }
        },
        sheetContent = {
            state.sheetContent(this, state.sheetState)
        }
    )
}

@Composable
@ExperimentalMaterialApi
fun rememberGlobalModalBottomSheetState(
    initialValue: ModalBottomSheetValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
): GlobalModalBottomSheetState {
    return GlobalModalBottomSheetState(
        rememberModalBottomSheetState(
            initialValue,
            animationSpec,
        )
    )
}

val LocalGlobalModalBottomSheet =
    compositionLocalOf<GlobalModalBottomSheetState> { error("No GlobalModalBottomSheetState provided yet") }

@OptIn(ExperimentalMaterialApi::class)
class GlobalModalBottomSheetState(val sheetState: ModalBottomSheetState) {
    var sheetContent: @Composable ColumnScope.(ModalBottomSheetState) -> Unit by mutableStateOf({ EmptySheetContent() })

    suspend fun show(content: @Composable () -> Unit) {
        show(onDismiss = {}, content = content)
    }

    suspend fun show(
        onDismiss: (isDismissByHardwareButton: Boolean) -> Unit = {},
        content: @Composable () -> Unit,
    ) {
        var isShown by mutableStateOf(false)
        var isHardButtonPressed by mutableStateOf(false)
        sheetContent = {
            LaunchedEffect(sheetState.currentValue) {
                if (sheetState.isVisible) isShown = true
                if (!sheetState.isVisible && isShown) onDismiss(isHardButtonPressed)
            }
            BackPressHandler {
                isHardButtonPressed = true
                sheetState.hide()
            }

            content()
        }
        sheetState.show()
    }

    suspend fun hide() {
        sheetContent = { EmptySheetContent() }
        sheetState.hide()
    }

    @Composable
    private fun EmptySheetContent() {
        Column(modifier = Modifier.defaultMinSize(minHeight = 1.dp)) {}
    }
}