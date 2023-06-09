package ru.sfedu.zhalnin.oborona.ui.top_toolbar

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun TopToolbar(
    modifier: Modifier = Modifier,
    state: TopToolbarViewModel.State = TopToolbarViewModel.State(),
    onAction: (TopToolbarViewModel.Action) -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(
                if (state.mode == TopToolbarViewModel.Mode.COMMON) 48.dp else 64.dp
            ),
        color = AppTheme.colors.transparent
    ) {
        AnimatedVisibility(
            visible = state.mode == TopToolbarViewModel.Mode.COMMON,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            CommonToolbar(state = state, onAction = onAction)
        }

        AnimatedVisibility(
            visible = state.mode == TopToolbarViewModel.Mode.SEARCH,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            SearchToolbar(state, onAction)
        }

        AnimatedVisibility(
            visible = state.mode == TopToolbarViewModel.Mode.USER,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            UserToolbar(state, onAction)
        }
    }
}

@Composable
private fun SearchToolbar(
    state: TopToolbarViewModel.State = TopToolbarViewModel.State(),
    onAction: (TopToolbarViewModel.Action) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchViewTextField(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, top = 16.dp, bottom = 16.dp),
            text = state.text,
            onValueChanged = { onAction(TopToolbarViewModel.Action.TextChanged(it)) }
        )
        IconButton(
            modifier = Modifier.padding(horizontal = 8.dp),
            onClick = { onAction(TopToolbarViewModel.Action.OpenMenu) }
        ) {
            Icon(
                imageVector = AppTheme.icons.Menu,
                tint = AppTheme.colors.onPrimary,
                contentDescription = null
            )
        }
    }
}

@Composable
fun CommonToolbar(
    modifier: Modifier = Modifier,
    state: TopToolbarViewModel.State = TopToolbarViewModel.State(),
    onAction: (TopToolbarViewModel.Action) -> Unit = {}
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        val (backBtnRef, tittleRef) = createRefs()

        if (state.backButtonEnabled) {
            IconButton(
                modifier = Modifier.constrainAs(backBtnRef) {
                    start.linkTo(parent.start)
                    top.linkTo(tittleRef.top)
                    bottom.linkTo(tittleRef.bottom)
                },
                onClick = {
                    onAction(TopToolbarViewModel.Action.BackClicked)
                }
            ) {
                Icon(
                    imageVector = AppTheme.icons.Back,
                    tint = AppTheme.colors.onPrimary,
                    contentDescription = null
                )
            }
        }

        Text(
            modifier = Modifier
                .padding(all = 0.dp)
                .constrainAs(tittleRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            style = AppTheme.typography.header2,
            color = AppTheme.colors.onPrimary,
            text = state.tittle
        )
    }
}

@Composable
private fun UserToolbar(
    state: TopToolbarViewModel.State = TopToolbarViewModel.State(),
    onAction: (TopToolbarViewModel.Action) -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (state.userInfo != null) {
            Icon(
                modifier = Modifier.padding(horizontal = 8.dp),
                imageVector = if (state.userInfo.isSponsor) {
                    AppTheme.icons.SponsorUser
                } else if (state.userInfo.isVip) {
                    AppTheme.icons.VipUser
                } else {
                    AppTheme.icons.CommonUser
                },
                tint = AppTheme.colors.onPrimary,
                contentDescription = null
            )

            Text(
                modifier = Modifier.weight(1f),
                style = AppTheme.typography.header2,
                color = AppTheme.colors.onPrimary,
                text = stringResource(
                    id = if (state.userInfo.isSponsor) {
                        R.string.user_role2
                    } else if (state.userInfo.isVip) {
                        R.string.user_role1
                    } else {
                        R.string.user_role0
                    }
                )
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        IconButton(
            modifier = Modifier.padding(horizontal = 8.dp),
            onClick = { onAction(TopToolbarViewModel.Action.OpenMenu) }
        ) {
            Icon(
                imageVector = AppTheme.icons.Menu,
                tint = AppTheme.colors.onPrimary,
                contentDescription = null
            )
        }

    }
}

@Composable
@Preview
fun TopToolbarPreview() {
    AppTheme {
        Surface(color = Color.Black) {
            Column {
                TopToolbar(state = TopToolbarViewModel.State(mode = TopToolbarViewModel.Mode.COMMON))
                TopToolbar(
                    state = TopToolbarViewModel.State(
                        tittle = "Карта",
                        mode = TopToolbarViewModel.Mode.COMMON
                    )
                )
                TopToolbar(
                    state = TopToolbarViewModel.State(mode = TopToolbarViewModel.Mode.SEARCH)
                )

                var text by remember {
                    mutableStateOf("Тестовая строка")
                }

                TopToolbar(
                    state = TopToolbarViewModel.State(
                        text = text,
                        mode = TopToolbarViewModel.Mode.SEARCH,
                    ),
                    onAction = {
                        when (it) {
                            is TopToolbarViewModel.Action.TextChanged -> {
                                text = it.text
                            }
                            else -> {}
                        }
                    }
                )
                TopToolbar(state = TopToolbarViewModel.State(mode = TopToolbarViewModel.Mode.USER))
            }
        }
    }
}
