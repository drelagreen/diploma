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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.ui.common.GenderPicker
import ru.sfedu.zhalnin.oborona.ui.common.InputField
import ru.sfedu.zhalnin.oborona.ui.common.PrimaryButton
import ru.sfedu.zhalnin.oborona.ui.user.UserViewModel
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onAction: (UserViewModel.Action) -> Unit,
    state: UserViewModel.State,
    toLoginScreen: () -> Unit,
    onBackClicked: () -> Unit,
) {
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
            text = stringResource(R.string.rs_reg),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = AppTheme.typography.header2,
            color = AppTheme.colors.onBackground
        )
        var firstnameFocused by remember { mutableStateOf(false) }
        var lastnameFocused by remember { mutableStateOf(false) }
        var patronymicFocused by remember { mutableStateOf(false) }
        var phoneFocused by remember { mutableStateOf(false) }
        var emailFocused by remember { mutableStateOf(false) }
        var passwordFocused by remember { mutableStateOf(false) }

        InputField(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) onAction(UserViewModel.Action.ChangeFieldsRegister.RemoveErrors)
                lastnameFocused = it.isFocused
            },
            label = stringResource(R.string.rs_lastname),
            onChange = {
                onAction(UserViewModel.Action.ChangeFieldsRegister.Lastname(it))
            },
            value = state.registerScreenState.lastnameValue,
            error = if (state.registerScreenState.registerError == UserViewModel.State.AuthError.NONE && !lastnameFocused) {
                when (state.registerScreenState.lastnameFieldValidation) {
                    UserViewModel.State.ValidationError.EMPTY -> stringResource(R.string.rs_empty_field)
                    UserViewModel.State.ValidationError.FORMAT_LASTNAME -> stringResource(R.string.rs_incorrect_lastname_format)
                    UserViewModel.State.ValidationError.NONE -> null
                    else -> null
                }
            } else {
                null
            },
            keyboardType = KeyboardType.Text
        )

        InputField(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) onAction(UserViewModel.Action.ChangeFieldsRegister.RemoveErrors)
                firstnameFocused = it.isFocused
            },
            label = stringResource(R.string.rsFirstname),
            onChange = {
                onAction(UserViewModel.Action.ChangeFieldsRegister.Firstname(it))
            },
            value = state.registerScreenState.firstnameValue,
            error = if (state.registerScreenState.registerError == UserViewModel.State.AuthError.NONE && !firstnameFocused) {
                when (state.registerScreenState.firstnameFieldValidation) {
                    UserViewModel.State.ValidationError.EMPTY -> stringResource(R.string.rs_empty_field)
                    UserViewModel.State.ValidationError.FORMAT_FIRSTNAME -> stringResource(R.string.rsIncorrectFirstname)
                    UserViewModel.State.ValidationError.NONE -> null
                    else -> null
                }
            } else {
                null
            },
            keyboardType = KeyboardType.Text
        )

        InputField(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) onAction(UserViewModel.Action.ChangeFieldsRegister.RemoveErrors)
                patronymicFocused = it.isFocused
            },
            label = stringResource(R.string.rsPatronymic),
            onChange = {
                onAction(UserViewModel.Action.ChangeFieldsRegister.Patronymic(it))
            },
            value = state.registerScreenState.patronymicValue,
            error = if (state.registerScreenState.registerError == UserViewModel.State.AuthError.NONE && !patronymicFocused) {
                when (state.registerScreenState.patronymicFieldValidation) {
                    UserViewModel.State.ValidationError.EMPTY -> stringResource(R.string.rs_empty_field)
                    UserViewModel.State.ValidationError.FORMAT_PATRONYMIC -> stringResource(R.string.rsIncorrectPatronymic)
                    UserViewModel.State.ValidationError.NONE -> null
                    else -> null
                }
            } else {
               null
            },
            keyboardType = KeyboardType.Text
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            style = AppTheme.typography.text2,
            color = AppTheme.colors.onBackground,
            text = stringResource(R.string.rsGender),
            lineHeight = 24.sp,
        )

        GenderPicker(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .onFocusChanged {
                    if (it.isFocused) onAction(UserViewModel.Action.ChangeFieldsRegister.RemoveErrors)
                },
            isMaleChecked = state.registerScreenState.genderIsMale,
            onClick = {
                onAction(UserViewModel.Action.ChangeFieldsRegister.ChangeGender(it))
            }
        )

        InputField(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) onAction(UserViewModel.Action.ChangeFieldsRegister.RemoveErrors)
                phoneFocused = it.isFocused
            },
            label = stringResource(R.string.rsPhone),
            onChange = {
                onAction(UserViewModel.Action.ChangeFieldsRegister.Phone(it))
            },
            value = state.registerScreenState.phoneValue,
            error = if (state.registerScreenState.registerError == UserViewModel.State.AuthError.NONE && !phoneFocused) {
                when (state.registerScreenState.phoneFieldValidation) {
                    UserViewModel.State.ValidationError.EMPTY -> stringResource(R.string.rs_empty_field)
                    UserViewModel.State.ValidationError.FORMAT_PHONE -> stringResource(R.string.rsIncorrectPhone)
                    UserViewModel.State.ValidationError.NONE -> null
                    else -> null
                }
            } else {
                null
            },
            keyboardType = KeyboardType.Phone
        )

        InputField(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) onAction(UserViewModel.Action.ChangeFieldsRegister.RemoveErrors)
                emailFocused = it.isFocused
            },
            label = stringResource(R.string.rsEmail),
            onChange = {
                onAction(UserViewModel.Action.ChangeFieldsRegister.Email(it))
            },
            value = state.registerScreenState.emailValue,
            error = if (state.registerScreenState.registerError == UserViewModel.State.AuthError.NONE && !emailFocused) {
                when (state.registerScreenState.emailFieldValidation) {
                    UserViewModel.State.ValidationError.EMPTY -> stringResource(R.string.rs_empty_field)
                    UserViewModel.State.ValidationError.FORMAT_EMAIL -> stringResource(R.string.rsIncorrectEmail)
                    UserViewModel.State.ValidationError.NONE -> null
                    else -> null
                }
            } else {
                when (state.registerScreenState.registerError) {
                    UserViewModel.State.AuthError.INCORRECT -> stringResource(R.string.rsUserExist)
                    UserViewModel.State.AuthError.SERVER_ERROR -> stringResource(R.string.rsServerError)
                    else -> null
                }
            },
            keyboardType = KeyboardType.Email
        )

        InputField(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) onAction(UserViewModel.Action.ChangeFieldsRegister.RemoveErrors)
                passwordFocused = it.isFocused
            },
            isPassword = true,
            label = stringResource(R.string.rsPassword),
            onChange = {
                onAction(UserViewModel.Action.ChangeFieldsRegister.Password(it))
            },
            value = state.registerScreenState.passwordValue,
            error = if (state.registerScreenState.registerError == UserViewModel.State.AuthError.NONE && !passwordFocused) {
                when (state.registerScreenState.passwordFieldValidation) {
                    UserViewModel.State.ValidationError.EMPTY -> stringResource(R.string.rs_empty_field)
                    UserViewModel.State.ValidationError.FORMAT_PASSWORD -> stringResource(R.string.rsIncorrectPassword)
                    UserViewModel.State.ValidationError.NONE -> null
                    else -> null
                }
            } else {
                null
            },
            keyboardType = KeyboardType.Password
        )

        PrimaryButton(
            modifier = Modifier.padding(top = 32.dp),
            text = stringResource(R.string.rsRegister),
            enabled = state.registerScreenState.btnEnabled,
            onClick = {
                onAction(
                    UserViewModel.Action.Register(
                        state.registerScreenState.emailValue,
                        state.registerScreenState.firstnameValue,
                        state.registerScreenState.lastnameValue,
                        state.registerScreenState.patronymicValue,
                        state.registerScreenState.phoneValue,
                        state.registerScreenState.passwordValue,
                        state.registerScreenState.genderIsMale
                    )
                )
            }
        )

        Row(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 32.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.rsHaveAcc),
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
                        toLoginScreen()
                    },
                text = stringResource(R.string.rsAuth),
                style = AppTheme.typography.text3,
                color = AppTheme.colors.primary
            )
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    AppTheme {
        RegisterScreen(
            modifier = Modifier,
            state = UserViewModel.State(),
            onAction = {},
            toLoginScreen = {},
            onBackClicked = {}
        )
    }
}
