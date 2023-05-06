package ru.sfedu.zhalnin.oborona.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.ui.common.GenderPicker
import ru.sfedu.zhalnin.oborona.ui.common.InputField
import ru.sfedu.zhalnin.oborona.ui.common.PrimaryButton
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme
import ru.sfedu.zhalnin.oborona.ui.top_toolbar.CommonToolbar
import ru.sfedu.zhalnin.oborona.ui.top_toolbar.TopToolbarViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onAction: (UserViewModel.Action) -> Unit,
    changeProfileDataState: UserViewModel.State.ChangeProfileDataState,
    changeEmailDataState: UserViewModel.State.ChangeEmailState,
    changePasswordDataState: UserViewModel.State.ChangePasswordState,
    changeProfile: (String, String, String, String, Boolean) -> Unit,
    changeEmail: (String) -> Unit,
    changePassword: (String, String, String) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val (toolbarRef, contentRef) = createRefs()

        val focusManager = LocalFocusManager.current

        CommonToolbar(
            modifier = Modifier.constrainAs(toolbarRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            state = TopToolbarViewModel.State(
                mode = TopToolbarViewModel.Mode.COMMON,
                tittle = stringResource(id = ru.sfedu.zhalnin.oborona.R.string.profile)
            ),
            onAction = { onBackClicked() }
        )

        Column(
            modifier = Modifier
                .background(
                    AppTheme.colors.background,
                    shape = AppTheme.shapes.backgroundShape
                )
                .constrainAs(contentRef) {
                    top.linkTo(parent.top, 48.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                }
                .padding(horizontal = 16.dp)
        ) {
            Text(
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                text = stringResource(R.string.changeData),
                style = AppTheme.typography.header2,
                color = AppTheme.colors.onBackground,
            )

            var focusLastname by remember { mutableStateOf(false) }
            var focusFirstname by remember { mutableStateOf(false) }
            var focusPatronymic by remember { mutableStateOf(false) }
            var focusPhone by remember { mutableStateOf(false) }

            InputField(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .onFocusChanged {
                        focusLastname = it.isFocused
                    },
                label = stringResource(id = R.string.rs_lastname),
                onChange = {
                    onAction(UserViewModel.Action.ChangeFieldsProfileData.Lastname(it))
                },
                error = if (!focusLastname) getErrorMessage(changeProfileDataState.lastnameFieldValidation) else null,
                value = changeProfileDataState.lastnameValue,
                keyboardType = KeyboardType.Text
            )
            InputField(
                modifier = Modifier.onFocusChanged {
                    focusFirstname = it.isFocused
                },
                label = stringResource(id = R.string.rsFirstname),
                onChange = {
                    onAction(UserViewModel.Action.ChangeFieldsProfileData.Firstname(it))
                },
                error = if (!focusFirstname) getErrorMessage(changeProfileDataState.firstnameFieldValidation) else null,
                value = changeProfileDataState.firstnameValue,
                keyboardType = KeyboardType.Text
            )
            InputField(
                modifier = Modifier.onFocusChanged {
                    focusPatronymic = it.isFocused
                },
                label = stringResource(id = R.string.rsPatronymic),
                onChange = {
                    onAction(UserViewModel.Action.ChangeFieldsProfileData.Patronymic(it))
                },
                error = if (!focusPatronymic) getErrorMessage(changeProfileDataState.patronymicFieldValidation) else null,
                value = changeProfileDataState.patronymicValue,
                keyboardType = KeyboardType.Text
            )
            InputField(
                modifier = Modifier.onFocusChanged {
                    focusPhone = it.isFocused
                },
                label = stringResource(R.string.phoneInFormat),
                onChange = {
                    onAction(UserViewModel.Action.ChangeFieldsProfileData.Phone(it))
                },
                error = if (!focusPhone)
                    when (changeProfileDataState.changeDataError) {
                        UserViewModel.State.AuthError.INCORRECT -> stringResource(R.string.badData)
                        UserViewModel.State.AuthError.SERVER_ERROR -> stringResource(R.string.psServerError)
                        UserViewModel.State.AuthError.NONE -> getErrorMessage(changeProfileDataState.phoneFieldValidation)
                    } else null,
                value = changeProfileDataState.phoneValue,
                keyboardType = KeyboardType.Phone
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                style = AppTheme.typography.text2,
                color = AppTheme.colors.onBackground,
                text = stringResource(R.string.gender),
                lineHeight = 24.sp,
            )
            GenderPicker(
                modifier = Modifier.padding(top = 8.dp),
                onClick = {
                    onAction(UserViewModel.Action.ChangeFieldsProfileData.Gender(it))
                },
                isMaleChecked = changeProfileDataState.genderIsMale
            )
            PrimaryButton(
                modifier = Modifier
                    .padding(top = 32.dp),
                text = stringResource(R.string.save),
                enabled = changeProfileDataState.btnEnabled,
                onClick = {
                    changeProfile(
                        changeProfileDataState.lastnameValue,
                        changeProfileDataState.firstnameValue,
                        changeProfileDataState.patronymicValue,
                        changeProfileDataState.phoneValue,
                        changeProfileDataState.genderIsMale
                    )

                    focusManager.clearFocus(true)
                }
            )

            //--- Change Email ---
            var focusEmail by remember { mutableStateOf(false) }

            Text(
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                text = stringResource(R.string.psChangeEmail),
                style = AppTheme.typography.header2,
                color = AppTheme.colors.onBackground
            )
            InputField(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .onFocusChanged {
                        focusEmail = it.isFocused
                    },
                label = stringResource(id = R.string.rsEmail),
                onChange = {
                    onAction(UserViewModel.Action.ChangeFieldsEmail.Email(it))
                },
                error = if (!focusEmail)
                    when (changeEmailDataState.changeEmailError) {
                        UserViewModel.State.AuthError.INCORRECT -> stringResource(R.string.psMailInUse)
                        UserViewModel.State.AuthError.SERVER_ERROR -> stringResource(R.string.psNoConnection)
                        UserViewModel.State.AuthError.NONE -> getErrorMessage(changeEmailDataState.emailFieldValidation)
                    }
                else
                    null,
                value = changeEmailDataState.emailValue,
                keyboardType = KeyboardType.Email
            )
            PrimaryButton(
                modifier = Modifier
                    .padding(top = 16.dp),
                text = stringResource(R.string.save),
                enabled = changeEmailDataState.btnEnabled,
                onClick = {
                    changeEmail(changeEmailDataState.emailValue)
                    focusManager.clearFocus(true)
                }
            )

            //--- Change Password ---
            var focusOld by remember { mutableStateOf(false) }
            var focusNew by remember { mutableStateOf(false) }
            var focusRepeat by remember { mutableStateOf(false) }

            Text(
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                text = stringResource(R.string.psChangePass),
                style = AppTheme.typography.header2,
                color = AppTheme.colors.onBackground,
            )
            InputField(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .onFocusChanged { focusOld = it.isFocused },
                label = stringResource(R.string.psOldPass),
                isPassword = true,
                onChange = {
                    onAction(UserViewModel.Action.ChangeFieldsPassword.OldPassword(it))
                },
                value = changePasswordDataState.oldPasswordValue,
                error = if (!focusOld) {
                    when (changePasswordDataState.changePasswordError) {
                        UserViewModel.State.AuthError.INCORRECT -> stringResource(R.string.psBadPass)
                        UserViewModel.State.AuthError.SERVER_ERROR -> stringResource(R.string.psNoConnection)
                        UserViewModel.State.AuthError.NONE -> getErrorMessage(
                            changePasswordDataState.oldPasswordFieldValidation
                        )
                    }
                } else {
                    null
                },
                keyboardType = KeyboardType.Password
            )
            InputField(
                modifier = Modifier
                    .onFocusChanged { focusNew = it.isFocused },
                label = stringResource(R.string.psNewPass),
                isPassword = true,
                onChange = {
                    onAction(UserViewModel.Action.ChangeFieldsPassword.NewPassword(it))
                },
                value = changePasswordDataState.newPasswordValue,
                error = if (!focusNew) {
                    getErrorMessage(changePasswordDataState.newPasswordFieldValidation)
                } else {
                    null
                },
                keyboardType = KeyboardType.Password
            )
            InputField(
                modifier = Modifier
                    .onFocusChanged { focusRepeat = it.isFocused },
                label = stringResource(R.string.psRepeatPass),
                isPassword = true,
                onChange = {
                    onAction(UserViewModel.Action.ChangeFieldsPassword.RepeatPassword(it))
                },
                value = changePasswordDataState.repeatPasswordValue,
                error = if (!focusRepeat) {
                    getErrorMessage(changePasswordDataState.repeatPasswordFieldValidation)
                } else {
                    null
                },
                keyboardType = KeyboardType.Password
            )
            PrimaryButton(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 32.dp),
                text = stringResource(R.string.save),
                enabled = changePasswordDataState.btnEnabled,
                onClick = {
                    changePassword(
                        changePasswordDataState.oldPasswordValue,
                        changePasswordDataState.newPasswordValue,
                        changePasswordDataState.repeatPasswordValue
                    )
                    focusManager.clearFocus(true)
                }
            )
        }
    }
}

@Composable
private fun getErrorMessage(value: UserViewModel.State.ValidationError): String? {
    return when (value) {
        UserViewModel.State.ValidationError.FORMAT_PASSWORD -> stringResource(R.string.psFormatPass)
        UserViewModel.State.ValidationError.EMPTY -> stringResource(R.string.psEmtyField)
        UserViewModel.State.ValidationError.PASSWORDS_NOT_EQUAL -> stringResource(R.string.psDifferentPass)
        UserViewModel.State.ValidationError.FORMAT_EMAIL -> stringResource(R.string.psEmailFormat)
        UserViewModel.State.ValidationError.FORMAT_FIRSTNAME -> stringResource(R.string.psNameFormat)
        UserViewModel.State.ValidationError.FORMAT_PHONE -> stringResource(R.string.psPhoneFormat)
        UserViewModel.State.ValidationError.FORMAT_LASTNAME -> stringResource(R.string.psLastnameFormat)
        UserViewModel.State.ValidationError.FORMAT_PATRONYMIC -> stringResource(R.string.psPatronymicFormat)
        UserViewModel.State.ValidationError.NONE -> null
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    AppTheme {
        val vm: UserViewModel = viewModel()
        val state = vm.state.collectAsState()

        ProfileScreen(
            onBackClicked = {},
            changeEmailDataState = state.value.changeEmailState,
            changePasswordDataState = state.value.changePasswordState,
            changeProfileDataState = state.value.changeProfileDataState,
            onAction = { vm.onAction(it) },
            changeEmail = { },
            changePassword = { _, _, _ -> },
            changeProfile = { _, _, _, _, _ -> }
        )
    }
}