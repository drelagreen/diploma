package ru.sfedu.zhalnin.oborona.ui.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.ui.common.InputField
import ru.sfedu.zhalnin.oborona.ui.common.PrimaryButton
import ru.sfedu.zhalnin.oborona.ui.user.UserViewModel
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onFieldFocused: () -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    email: String = "",
    password: String = "",
    emailValidationError: UserViewModel.State.ValidationError = UserViewModel.State.ValidationError.NONE,
    passwordValidationError: UserViewModel.State.ValidationError = UserViewModel.State.ValidationError.NONE,
    authError: UserViewModel.State.AuthError = UserViewModel.State.AuthError.NONE,
    onLogin: (String, String) -> Unit = { _, _ -> },
    toRegisterScreen: () -> Unit = {},
    onBackClicked: () -> Unit = {},
    toPasswordRecovery: () -> Unit = {},
    btnEnabled: Boolean = true,
) {
    BackHandler(true) {
        onBackClicked()
    }

    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(vertical = 24.dp),
            text = stringResource(id = R.string.ls_enter),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = AppTheme.typography.header2,
            color = AppTheme.colors.onBackground
        )
        var emailFocused by remember { mutableStateOf(false) }
        var passwordFocused by remember { mutableStateOf(false) }

        InputField(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) onFieldFocused()
                emailFocused = it.isFocused
            },
            label = stringResource(id = R.string.ls_email),
            onChange = {
                onEmailChanged(it)
            },
            value = email,
            error = if (authError == UserViewModel.State.AuthError.NONE && !emailFocused) {
                when (emailValidationError) {
                    UserViewModel.State.ValidationError.EMPTY -> stringResource(id = R.string.ls_empty_field)
                    UserViewModel.State.ValidationError.FORMAT_EMAIL -> stringResource(id = R.string.ls_incorrect_email)
                    UserViewModel.State.ValidationError.NONE -> null
                    else -> null
                }
            } else {
                when (authError) {
                    UserViewModel.State.AuthError.INCORRECT -> stringResource(id = R.string.ls_incorrect_email_or_pass)
                    UserViewModel.State.AuthError.SERVER_ERROR -> stringResource(id = R.string.ls_server_connection)
                    else -> null
                }
            },
            keyboardType = KeyboardType.Email
        )

        InputField(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) onFieldFocused()
                passwordFocused = it.isFocused
            },
            label = stringResource(id = R.string.ls_password),
            onChange = {
                onPasswordChanged(it)
            },
            value = password,
            isPassword = true,
            error = if (authError == UserViewModel.State.AuthError.NONE && !passwordFocused) {
                when (passwordValidationError) {
                    UserViewModel.State.ValidationError.EMPTY -> stringResource(id = R.string.ls_empty_field)
                    UserViewModel.State.ValidationError.FORMAT_PASSWORD -> stringResource(id = R.string.ls_incorrect_password)
                    UserViewModel.State.ValidationError.NONE -> null
                    else -> null
                }
            } else {
                null
            },
            keyboardType = KeyboardType.Password
        )

        Text(
            modifier = Modifier
                .padding(top = 4.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true)
                ) {
                    toPasswordRecovery()
                },
            text = stringResource(id = R.string.ls_rec_pass),
            style = AppTheme.typography.text3,
            color = AppTheme.colors.primary
        )

        PrimaryButton(
            modifier = Modifier.padding(top = 32.dp),
            text = stringResource(id = R.string.ls_auth),
            enabled = btnEnabled,
            onClick = {
                onLogin(email, password)
            }
        )

        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.ls_no_acc),
                style = AppTheme.typography.text3,
                color = AppTheme.colors.onBackground
            )
            Text(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true)
                    ) {
                        toRegisterScreen()
                    },
                text = stringResource(R.string.ls_register),
                style = AppTheme.typography.text3,
                color = AppTheme.colors.primary
            )
        }
    }
}



@Preview
@Composable
fun LoginScreenPreview() {
    AppTheme {
        LoginScreen()
    }
}