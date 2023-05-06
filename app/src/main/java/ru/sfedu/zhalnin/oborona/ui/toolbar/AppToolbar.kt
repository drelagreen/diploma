package ru.sfedu.zhalnin.oborona.ui.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sfedu.zhalnin.oborona.ui.common.IconToolbarButton
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun AppToolbar(
    modifier: Modifier = Modifier,
    state: AppToolbarViewModel.ToolbarState = AppToolbarViewModel.ToolbarState(),
    onAction: (AppToolbarViewModel.Action) -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .background(AppTheme.colors.primary)
            .fillMaxWidth()
            .padding(horizontal = 38.dp, vertical = 2.dp)
    ) {

        IconToolbarButton(
            icon = AppTheme.icons.HomeEmpty,
            iconChecked = AppTheme.icons.HomeFilled,
            onClick = { onAction(AppToolbarViewModel.Action.HomeButtonClicked(it)) },
            checked = state.homeChecked
        )

        IconToolbarButton(
            icon = AppTheme.icons.MapEmpty,
            iconChecked = AppTheme.icons.MapFilled,
            onClick = { onAction(AppToolbarViewModel.Action.MapButtonClicked(it)) },
            checked = state.mapChecked
        )

        IconToolbarButton(
            icon = AppTheme.icons.CalendarEmtry,
            iconChecked = AppTheme.icons.CalendarFilled,
            onClick = { onAction(AppToolbarViewModel.Action.CalendarButtonClicked(it)) },
            checked = state.calendarChecked
        )

        IconToolbarButton(
            icon = AppTheme.icons.UserEmpty,
            iconChecked = AppTheme.icons.UserFilled,
            onClick = { onAction(AppToolbarViewModel.Action.UserButtonClicked(it)) },
            checked = state.userChecked
        )
    }
}

@Composable
@Preview
fun AppToolbarPreview() {
    AppTheme {
        val vm: AppToolbarViewModel = viewModel()
        val state by vm.state.collectAsState()

        AppToolbar(
            state = state,
            onAction = vm::onAction
        )
    }
}