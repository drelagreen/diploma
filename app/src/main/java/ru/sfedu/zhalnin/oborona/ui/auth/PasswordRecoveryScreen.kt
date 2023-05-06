package ru.sfedu.zhalnin.oborona.ui.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.ui.common.InputField
import ru.sfedu.zhalnin.oborona.ui.common.PrimaryButton
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme
import ru.sfedu.zhalnin.oborona.ui.user.UserViewModel

@Composable
fun PasswordRecoveryScreen(
    modifier: Modifier = Modifier,
    state: UserViewModel.State.PasswordRecoveryState,
    changeEmail: (String) -> Unit,
    changeCode: (String) -> Unit,
    changePassword: (String) -> Unit,
    onBackClicked: () -> Unit,
    removeErrors: () -> Unit,
    sendCode: (String) -> Unit,
    sendNewPassword: (String, String) -> Unit,
    toLoginScreen: () -> Unit,
    timerTicks: Int = 0
) {
    val focusManager = LocalFocusManager.current

    var emailFocused by remember { mutableStateOf(false) }

    BackHandler(true) {
        onBackClicked()
    }

    Column(
        modifier = modifier.verticalScroll(
            state = rememberScrollState()
        )
    ) {
        Text(
            modifier = Modifier.padding(vertical = 24.dp),
            text = stringResource(R.string.pr_tittle),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = AppTheme.typography.header2,
            color = AppTheme.colors.onBackground
        )

        InputField(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) removeErrors()
                emailFocused = it.isFocused
            },
            label = "E-mail*",
            onChange = changeEmail,
            value = state.emailField,
            error = if (!emailFocused) {
                if (state.isEmailValidationError) {
                    stringResource(R.string.pr_email_format)
                } else if (state.isEmailError) {
                    stringResource(R.string.pr_no_email)
                } else if (state.isServerError) {
                    stringResource(R.string.pr_send_error)
                } else null
            } else null,
            keyboardType = KeyboardType.Email
        )

        Row(
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PrimaryButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                enabled = !state.isEmailValidationError && state.emailField.isNotEmpty() && timerTicks == 0,
                text = stringResource(R.string.pr_send_code),
                onClick = {
                    sendCode(state.emailField)
                    focusManager.clearFocus(true)
                },
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                if (timerTicks != 0) {
                    Text(
                        text = stringResource(id = R.string.pr_timer, timerTicks),
                        style = AppTheme.typography.text3
                    )
                }
            }
        }

        InputField(
            Modifier.onFocusChanged {
                if (it.isFocused) removeErrors()
            },
            keyboardType = KeyboardType.Text,
            label = stringResource(R.string.pr_code),
            value = state.codeField,
            onChange = changeCode,
            error = if (state.isCodeError) {
                stringResource(R.string.pr_bad_code)
            } else {
                null
            }
        )

        InputField(
            Modifier.onFocusChanged {
                if (it.isFocused) removeErrors()
            },
            isPassword = true,
            keyboardType = KeyboardType.Password,
            value = state.passwordField,
            onChange = changePassword,
            error = if (state.isPasswordValidationError) {
                stringResource(R.string.pr_bad_pass)
            } else {
                null
            },
            label = stringResource(R.string.pr_new_pass)
        )

        PrimaryButton(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.ok),
            onClick = {
                sendNewPassword(state.codeField, state.passwordField)
                focusManager.clearFocus(true)
            },
            enabled = state.codeField.isNotEmpty() && state.passwordField.isNotEmpty() && !state.isPasswordValidationError,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true)
                ) {
                    toLoginScreen()
                },
            textAlign = TextAlign.Center,
            text = stringResource(R.string.pr_auth),
            style = AppTheme.typography.text3,
            color = AppTheme.colors.primary
        )
    }
}

@Preview
@Composable
fun PasswordRecoveryScreenPreview() {
    AppTheme {
        PasswordRecoveryScreen(
            onBackClicked = { },
            sendCode = {},
            sendNewPassword = { _, _ -> },
            toLoginScreen = {},
            state = UserViewModel.State.PasswordRecoveryState(),
            changeEmail = {},
            removeErrors = {},
            changeCode = {},
            changePassword = {},
        )
    }
}