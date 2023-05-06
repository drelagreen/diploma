package ru.sfedu.zhalnin.oborona.ui.menu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.ui.dialog.ConfirmationDialog
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme
import ru.sfedu.zhalnin.oborona.ui.top_toolbar.CommonToolbar
import ru.sfedu.zhalnin.oborona.ui.top_toolbar.TopToolbarViewModel

@Composable
fun MainMenu(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onAction: (MenuViewModel.Action) -> Unit = {},
    userIsLoggedIn: Boolean = false
) {
    var comfirmationDialogVisible by remember { mutableStateOf(false) }

    BackHandler(true) {
        onBackClicked()
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = AppTheme.colors.transparent
    ) {
        if (comfirmationDialogVisible) {
            ConfirmationDialog(
                question = stringResource(R.string.mcExitDialogQuestion),
                onDismiss = {
                    comfirmationDialogVisible = false
                },
                onConfirm = {
                    comfirmationDialogVisible = false
                    onAction(MenuViewModel.Action.LogoutClicked)
                }
            )
        }

        Column(
            modifier = Modifier
        ) {
            CommonToolbar(
                state = TopToolbarViewModel.State(
                    tittle = stringResource(R.string.menu)
                ),
                onAction = {
                    when (it) {
                        TopToolbarViewModel.Action.BackClicked -> onBackClicked()
                        else -> {}
                    }
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(
                        AppTheme.colors.background,
                        shape = AppTheme.shapes.backgroundShape
                    )
                    .padding(top = 24.dp)
            ) {
                MenuItem(
                    topLineVisible = true,
                    tittle = stringResource(R.string.helpMenu),
                    icon = AppTheme.icons.HelpMenu,
                    onClick = { onAction(MenuViewModel.Action.HelpMenuClicked) }
                )
                if (userIsLoggedIn) {
                    MenuItem(
                        tittle = stringResource(R.string.profile),
                        icon = AppTheme.icons.Profile,
                        onClick = { onAction(MenuViewModel.Action.ProfileClicked) }
                    )
                }
                MenuItem(
                    tittle = stringResource(R.string.aboutFestival),
                    icon = AppTheme.icons.AboutFestival,
                    onClick = { onAction(MenuViewModel.Action.AboutFestivalClicked) }
                )
                MenuItem(
                    tittle = stringResource(R.string.contacts),
                    icon = AppTheme.icons.Telephone,
                    onClick = { onAction(MenuViewModel.Action.ContactsClicked) }
                )
                MenuItem(
                    tittle = stringResource(R.string.aboutApp),
                    icon = AppTheme.icons.AboutApp,
                    onClick = { onAction(MenuViewModel.Action.AboutAppClicked) }
                )
                if (userIsLoggedIn) {
                    MenuItem(
                        tittle = stringResource(R.string.exit),
                        icon = AppTheme.icons.Logout,
                        onClick = {
                            comfirmationDialogVisible = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HelpMenu(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onAction: (MenuViewModel.Action) -> Unit = {}
) {
    BackHandler(true) {
        onBackClicked()
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = AppTheme.colors.transparent
    ) {
        Column(
            modifier = Modifier
        ) {
            CommonToolbar(
                state = TopToolbarViewModel.State(
                    tittle = stringResource(R.string.helpMenuTittle)
                ),
                onAction = {
                    when (it) {
                        TopToolbarViewModel.Action.BackClicked -> onBackClicked()
                        else -> {}
                    }
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(
                        AppTheme.colors.background,
                        shape = AppTheme.shapes.backgroundShape
                    )
                    .padding(top = 24.dp)
            ) {
                MenuItem(
                    topLineVisible = true,
                    tittle = stringResource(R.string.hmVol),
                    icon = AppTheme.icons.Volunteer,
                    onClick = { onAction(MenuViewModel.Action.VolunteerClicked) }
                )
                MenuItem(
                    tittle = stringResource(R.string.hmTech),
                    icon = AppTheme.icons.TechSupport,
                    onClick = { onAction(MenuViewModel.Action.TechClicked) }
                )
                MenuItem(
                    tittle = stringResource(R.string.hmSupp),
                    icon = AppTheme.icons.Obslug,
                    onClick = { onAction(MenuViewModel.Action.ObslClicked) }
                )
                MenuItem(
                    tittle = stringResource(R.string.hmTK),
                    icon = AppTheme.icons.Tvorch,
                    onClick = { onAction(MenuViewModel.Action.TvorchClicked) }
                )
                MenuItem(
                    tittle = stringResource(R.string.hmSponsor),
                    icon = AppTheme.icons.Sponsor,
                    onClick = { onAction(MenuViewModel.Action.SponsorClicked) }
                )
                MenuItem(
                    tittle = stringResource(R.string.hmDon),
                    icon = AppTheme.icons.Donate,
                    onClick = { onAction(MenuViewModel.Action.DonateClicked) }
                )
            }
        }
    }
}

@Preview
@Composable
fun MainMenuPreview() {
    AppTheme {
        MainMenu()
    }
}