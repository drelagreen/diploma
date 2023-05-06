package ru.sfedu.zhalnin.oborona.ui.user

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.sfedu.zhalnin.oborona.data.model.dto.*
import ru.sfedu.zhalnin.oborona.data.model.dto.requests.LoginBody
import ru.sfedu.zhalnin.oborona.data.model.dto.requests.RegisterBody
import ru.sfedu.zhalnin.oborona.data.model.dto.responses.ModelResponse
import ru.sfedu.zhalnin.oborona.data.model.repository.ServiceRepository
import ru.sfedu.zhalnin.oborona.data.utils.asPassword
import ru.sfedu.zhalnin.oborona.data.validators.FieldsValidator

@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(
    private val prefs: SharedPreferences,
    private val serviceRepository: ServiceRepository,
    private val validator: FieldsValidator
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = UserViewModel(
        prefs, serviceRepository, validator
    ) as T
}

class UserViewModel(
    private val prefs: SharedPreferences,
    private val serviceRepository: ServiceRepository,
    private val validator: FieldsValidator
) : ViewModel() {
    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> get() = _state

    private val _result = MutableStateFlow<Result?>(null)
    val result: StateFlow<Result?> get() = _result

    private var registerBtnEnabledFlags = mutableMapOf(
        "firstname" to false,
        "lastname" to false,
        "patronymic" to true,
        "phone" to true,
        "email" to false,
        "password" to false,
    )

    private val changeDataBtnEnabledFlags = mutableMapOf(
        "firstname" to false,
        "lastname" to false,
        "patronymic" to true,
        "phone" to true,
    )

    fun onAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                is Action.UserToUserEditor -> {
                    onAction(Action.ChangeFieldsProfileData.Gender(_state.value.userInfo?.sex == "m"))
                    onAction(Action.ChangeFieldsProfileData.Firstname(_state.value.userInfo?.firstname.orEmpty()))
                    onAction(Action.ChangeFieldsProfileData.Lastname(_state.value.userInfo?.lastname.orEmpty()))
                    onAction(Action.ChangeFieldsProfileData.Patronymic(_state.value.userInfo?.middlename.orEmpty()))
                    onAction(Action.ChangeFieldsProfileData.Phone(_state.value.userInfo?.phonenumber.orEmpty()))

                    onAction(Action.ChangeFieldsEmail.Email(_state.value.userInfo?.email.orEmpty()))
                }

                is Action.ResetResult -> {
                    _result.emit(null)
                }

                is Action.LoadUser -> {
                    _state.emit(
                        _state.value.copy(
                            dataHasLoaded = false,
                            isError = false,
                        )
                    )
                    prefs.getString("token", null)?.let { token ->
                        val response =
                            withContext(Dispatchers.IO) { serviceRepository.getUser(token) }
                        when (response.result) {
                            ModelResponse.ModelResponseResult.OK -> {
                                _state.emit(
                                    _state.value.copy(
                                        userInfo = response.data,
                                        token = token,
                                        isError = false,
                                        dataHasLoaded = true
                                    )
                                )

                                _result.emit(
                                    Result.UserLoaded(
                                        _state.value.userInfo, _state.value.token
                                    )
                                )
                            }

                            ModelResponse.ModelResponseResult.USER_ERROR -> {
                                onAction(Action.LogoutToken)
                            }

                            else -> {
                                _state.emit(
                                    _state.value.copy(
                                        userInfo = null,
                                        token = null,
                                        dataHasLoaded = false,
                                        isError = false
                                    )
                                )
                            }
                        }
                    } ?: _state.emit(
                        _state.value.copy(
                            userInfo = null, token = null, dataHasLoaded = true, isError = false
                        )
                    )
                }

                is Action.Logout -> {
                    _state.emit(_state.value.copy(userInfo = null, token = null))
                    prefs.edit().remove("token").apply()

                    _result.emit(Result.Logout)
                }

                is Action.LogoutToken -> {
                    _state.emit(_state.value.copy(userInfo = null, token = null))
                    prefs.edit().remove("token").apply()

                    _result.emit(Result.LogoutToken)
                }

                is Action.OpenLoginSheet -> {
                    _state.emit(_state.value.copy(loginSheetIsOpen = true))
                }

                is Action.CloseLoginSheet -> {
                    _state.emit(_state.value.copy(loginSheetIsOpen = false))
                }

                is Action.ChangeFieldsLogin.RemoveErrors -> {
                    _state.emit(
                        _state.value.copy(
                            loginScreenState = _state.value.loginScreenState.copy(
                                authError = State.AuthError.NONE,
                            )
                        )
                    )
                }

                is Action.Login -> {
                    _state.emit(
                        _state.value.copy(
                            loginScreenState = _state.value.loginScreenState.copy(
                                btnEnabled = false,
                                authError = State.AuthError.NONE,
                            ), dataHasLoaded = true
                        )
                    )

                    val loginResult = withContext(Dispatchers.IO) {
                        serviceRepository.login(
                            LoginBody(
                                username = action.email, password = action.password.asPassword()
                            )
                        )
                    }

                    if (loginResult.token == null) {
                        if (loginResult.id == "SERVER ERROR") {
                            _state.emit(
                                _state.value.copy(
                                    loginScreenState = _state.value.loginScreenState.copy(
                                        authError = State.AuthError.SERVER_ERROR,
                                        btnEnabled = true,
                                    )
                                )
                            )
                        } else {
                            _state.emit(
                                _state.value.copy(
                                    loginScreenState = _state.value.loginScreenState.copy(
                                        authError = State.AuthError.INCORRECT,
                                        btnEnabled = true,
                                    )
                                )
                            )
                        }
                    } else {
                        _state.emit(
                            _state.value.copy(
                                token = loginResult.token,
                                loginScreenState = _state.value.loginScreenState.copy(
                                    btnEnabled = true,
                                    emailValue = "",
                                    passwordValue = "",
                                    emailFieldValidation = State.ValidationError.NONE,
                                    passwordFieldValidation = State.ValidationError.NONE,
                                ),
                            )
                        )

                        onAction(Action.CloseLoginSheet)

                        _state.emit(
                            _state.value.copy(
                                dataHasLoaded = false,
                            )
                        )

                        val userResult = withContext(Dispatchers.IO) {
                            serviceRepository.getUser(_state.value.token!!)
                        }

                        if (userResult.result == ModelResponse.ModelResponseResult.OK) {
                            _state.emit(
                                _state.value.copy(
                                    userInfo = userResult.data,
                                    dataHasLoaded = true,
                                )
                            )
                            prefs.edit().putString("token", _state.value.token).apply()
                            _result.emit(
                                Result.Login(
                                    token = _state.value.token, user = _state.value.userInfo
                                )
                            )
                        } else {
                            _state.emit(
                                _state.value.copy(
                                    isError = true,
                                )
                            )
                        }
                    }
                }

                is Action.ChangeEmail -> {
                    _state.emit(
                        _state.value.copy(
                            changeEmailState = _state.value.changeEmailState.copy(
                                emailFieldValidation = State.ValidationError.NONE,
                                btnEnabled = false,
                                changeEmailError = State.AuthError.NONE,
                            )
                        )
                    )

                    val response = withContext(Dispatchers.IO) {
                        serviceRepository.updateEmail(
                            token = _state.value.token.orEmpty(), EmailUpdatable(
                                email = action.email,
                            )
                        )
                    }

                    if (response.result == ModelResponse.ModelResponseResult.OK) {
                        _state.emit(
                            _state.value.copy(
                                changeEmailState = _state.value.changeEmailState.copy(
                                    btnEnabled = true,
                                )
                            )
                        )

                        _result.emit(Result.ChangeEmail)

                    } else if (response.result == ModelResponse.ModelResponseResult.SERVER_ERROR) {
                        _state.emit(
                            _state.value.copy(
                                changeEmailState = _state.value.changeEmailState.copy(
                                    btnEnabled = true,
                                    changeEmailError = State.AuthError.SERVER_ERROR,
                                )
                            )
                        )
                    } else if (response.errorBody?.charStream()?.readText()
                            ?.contains("mail") == true
                    ) {
                        _state.emit(
                            _state.value.copy(
                                changeEmailState = _state.value.changeEmailState.copy(
                                    btnEnabled = true,
                                    changeEmailError = State.AuthError.INCORRECT,
                                )
                            )
                        )
                    } else {
                        _state.emit(
                            _state.value.copy(
                                changeEmailState = _state.value.changeEmailState.copy(
                                    btnEnabled = true,
                                )
                            )
                        )

                        onAction(Action.LogoutToken)
                    }
                }

                is Action.ChangePassword -> {
                    _state.emit(
                        _state.value.copy(
                            changePasswordState = _state.value.changePasswordState.copy(
                                btnEnabled = false,
                                changePasswordError = State.AuthError.NONE,
                            )
                        )
                    )

                    val response = withContext(Dispatchers.IO) {
                        serviceRepository.updatePassword(
                            token = _state.value.token.orEmpty(), PasswordUpdatable(
                                old = action.oldPassword.asPassword(),
                                new = action.newPassword.asPassword(),
                            )
                        )
                    }

                    when (response.result) {
                        ModelResponse.ModelResponseResult.OK -> {
                            _state.emit(
                                _state.value.copy(
                                    changePasswordState = _state.value.changePasswordState.copy(
                                        btnEnabled = true,
                                    )
                                )
                            )

                            _result.emit(Result.ChangePassword)

                        }

                        ModelResponse.ModelResponseResult.SERVER_ERROR -> {
                            _state.emit(
                                _state.value.copy(
                                    changePasswordState = _state.value.changePasswordState.copy(
                                        btnEnabled = true,
                                        changePasswordError = State.AuthError.SERVER_ERROR,
                                    )
                                )
                            )
                        }

                        else -> {
                            if (response.errorBody?.charStream()?.readText()
                                    ?.contains("old") == true
                            ) {
                                _state.emit(
                                    _state.value.copy(
                                        changePasswordState = _state.value.changePasswordState.copy(
                                            btnEnabled = true,
                                            changePasswordError = State.AuthError.INCORRECT
                                        )
                                    )
                                )
                            } else {
                                _state.emit(
                                    _state.value.copy(
                                        changePasswordState = _state.value.changePasswordState.copy(
                                            btnEnabled = true,
                                        )
                                    )
                                )

                                onAction(Action.LogoutToken)
                            }
                        }
                    }
                }

                is Action.ChangeUserData -> {
                    _state.emit(
                        _state.value.copy(
                            changePasswordState = _state.value.changePasswordState.copy(
                                btnEnabled = false,
                                changePasswordError = State.AuthError.NONE,
                            )
                        )
                    )

                    val response = withContext(Dispatchers.IO) {
                        serviceRepository.updateUser(
                            token = _state.value.token.orEmpty(), body = UserUpdatable(
                                firstname = action.firstname,
                                lastname = action.lastname,
                                middlename = action.patronymic.orEmpty(),
                                phone = action.phone.orEmpty(),
                                sex = if (action.genderIsMale) "m" else "f"
                            )
                        )
                    }

                    when (response.result) {
                        ModelResponse.ModelResponseResult.OK -> {
                            _state.emit(
                                _state.value.copy(
                                    changeProfileDataState = _state.value.changeProfileDataState.copy(
                                        btnEnabled = true,
                                    )
                                )
                            )

                            _result.emit(Result.ChangeUserData)

                        }

                        ModelResponse.ModelResponseResult.SERVER_ERROR -> {
                            _state.emit(
                                _state.value.copy(
                                    changeProfileDataState = _state.value.changeProfileDataState.copy(
                                        btnEnabled = true,
                                        changeDataError = State.AuthError.SERVER_ERROR,
                                    )
                                )
                            )
                        }

                        else -> {
                            _state.emit(
                                _state.value.copy(
                                    changeProfileDataState = _state.value.changeProfileDataState.copy(
                                        btnEnabled = true,
                                    )
                                )
                            )

                            onAction(Action.LogoutToken)
                        }
                    }
                }

                is Action.Register -> {
                    _state.emit(
                        _state.value.copy(
                            registerScreenState = _state.value.registerScreenState.copy(
                                btnEnabled = false,
                                registerError = State.AuthError.NONE,
                            ),
                        )
                    )

                    val response = withContext(Dispatchers.IO) {
                        serviceRepository.register(
                            RegisterBody(
                                firstname = action.firstname,
                                lastname = action.lastname,
                                middlename = action.patronymic,
                                email = action.email,
                                password = action.password.asPassword(),
                                phonenumber = action.phone,
                                sex = if (action.genderIsMale) "m" else "f",
                            )
                        )
                    }

                    when (response.id) {
                        "ok" -> {
                            _state.emit(
                                _state.value.copy(
                                    isError = false,
                                    dataHasLoaded = true,
                                    registerSheetIsOpen = false,
                                    registerScreenState = _state.value.registerScreenState.copy(
                                        emailValue = "",
                                        firstnameValue = "",
                                        lastnameValue = "",
                                        patronymicValue = "",
                                        phoneValue = "",
                                        passwordValue = "",
                                        registerError = State.AuthError.NONE,
                                        firstnameFieldValidation = State.ValidationError.NONE,
                                        passwordFieldValidation = State.ValidationError.NONE,
                                        emailFieldValidation = State.ValidationError.NONE,
                                        phoneFieldValidation = State.ValidationError.NONE,
                                        patronymicFieldValidation = State.ValidationError.NONE,
                                        lastnameFieldValidation = State.ValidationError.NONE,
                                        repeatPasswordFieldValidation = State.ValidationError.NONE
                                    )
                                )
                            )

                            _result.emit(Result.Register)
                        }

                        "SERVER_ERROR" -> {
                            _state.emit(
                                _state.value.copy(
                                    registerScreenState = _state.value.registerScreenState.copy(
                                        btnEnabled = true,
                                        registerError = State.AuthError.SERVER_ERROR,
                                    )
                                )
                            )
                        }

                        else -> {
                            _state.emit(
                                _state.value.copy(
                                    registerScreenState = _state.value.registerScreenState.copy(
                                        btnEnabled = true,
                                        registerError = State.AuthError.INCORRECT,
                                    )
                                )
                            )
                        }
                    }
                }

                //LOGIN SCREEN
                is Action.ChangeFieldsLogin.Email -> {
                    val emailValid = validator.validateEmail(action.value)

                    _state.emit(
                        _state.value.copy(
                            loginScreenState = _state.value.loginScreenState.copy(
                                emailValue = action.value,
                                emailFieldValidation = if (emailValid) State.ValidationError.NONE
                                else State.ValidationError.FORMAT_EMAIL
                            )
                        )
                    )

                    _state.emit(
                        _state.value.copy(
                            loginScreenState = _state.value.loginScreenState.copy(
                                btnEnabled = _state.value.loginScreenState.passwordFieldValidation == State.ValidationError.NONE && _state.value.loginScreenState.emailFieldValidation == State.ValidationError.NONE
                            )
                        )
                    )
                }

                is Action.ChangeFieldsLogin.Password -> {
                    val passwordValid = validator.validatePassword(action.value)

                    _state.emit(
                        _state.value.copy(
                            loginScreenState = _state.value.loginScreenState.copy(
                                passwordValue = action.value,
                                passwordFieldValidation = if (passwordValid) State.ValidationError.NONE
                                else State.ValidationError.FORMAT_PASSWORD
                            )
                        )
                    )
                }

                //REGISTER SCREEN

                is Action.OpenRegisterSheet -> {
                    _state.emit(
                        _state.value.copy(
                            registerSheetIsOpen = true, loginSheetIsOpen = false
                        )
                    )
                }

                is Action.CloseRegisterSheet -> {
                    _state.emit(_state.value.copy(registerSheetIsOpen = false))
                }

                is Action.ChangeFieldsRegister.RemoveErrors -> {
                    _state.emit(
                        _state.value.copy(
                            registerScreenState = _state.value.registerScreenState.copy(
                                registerError = State.AuthError.NONE,
                            )
                        )
                    )
                }

                is Action.ChangeFieldsRegister.Email -> {
                    val emailValid = validator.validateEmail(action.value)

                    _state.emit(
                        _state.value.copy(
                            registerScreenState = _state.value.registerScreenState.copy(
                                emailValue = action.value,
                                emailFieldValidation = if (emailValid) State.ValidationError.NONE
                                else if (action.value.isNotEmpty()) State.ValidationError.FORMAT_EMAIL
                                else State.ValidationError.EMPTY
                            )
                        )
                    )

                    registerBtnEnabledFlags["email"] = emailValid && action.value.isNotEmpty()
                    checkRegisterButton()
                }

                is Action.ChangeFieldsRegister.ChangeGender -> {
                    _state.emit(
                        _state.value.copy(
                            registerScreenState = _state.value.registerScreenState.copy(
                                genderIsMale = action.isMaleChecked
                            )
                        )
                    )
                }

                is Action.ChangeFieldsRegister.Firstname -> {
                    val firstnameValid = validator.validateFirstname(action.value)

                    _state.emit(
                        _state.value.copy(
                            registerScreenState = _state.value.registerScreenState.copy(
                                firstnameValue = action.value,
                                firstnameFieldValidation = if (firstnameValid) State.ValidationError.NONE
                                else if (action.value.isNotEmpty()) State.ValidationError.FORMAT_EMAIL
                                else State.ValidationError.EMPTY
                            )
                        )
                    )

                    registerBtnEnabledFlags["firstname"] =
                        firstnameValid && action.value.isNotEmpty()
                    checkRegisterButton()
                }

                is Action.ChangeFieldsRegister.Lastname -> {
                    val lastnameValid = validator.validateLastname(action.value)

                    _state.emit(
                        _state.value.copy(
                            registerScreenState = _state.value.registerScreenState.copy(
                                lastnameValue = action.value,
                                lastnameFieldValidation = if (lastnameValid) State.ValidationError.NONE
                                else if (action.value.isNotEmpty()) State.ValidationError.FORMAT_LASTNAME
                                else State.ValidationError.EMPTY
                            )
                        )
                    )

                    registerBtnEnabledFlags["lastname"] = lastnameValid && action.value.isNotEmpty()
                    checkRegisterButton()
                }

                is Action.ChangeFieldsRegister.Patronymic -> {
                    val patronymicValid = validator.validatePatronymic(action.value)

                    _state.emit(
                        _state.value.copy(
                            registerScreenState = _state.value.registerScreenState.copy(
                                patronymicValue = action.value,
                                patronymicFieldValidation = if (patronymicValid) State.ValidationError.NONE
                                else State.ValidationError.FORMAT_PATRONYMIC
                            )
                        )
                    )

                    registerBtnEnabledFlags["patronymic"] = patronymicValid
                    checkRegisterButton()
                }

                is Action.ChangeFieldsRegister.Phone -> {
                    val phoneValid = validator.validatePhone(action.value)

                    _state.emit(
                        _state.value.copy(
                            registerScreenState = _state.value.registerScreenState.copy(
                                phoneValue = action.value,
                                phoneFieldValidation = if (phoneValid) State.ValidationError.NONE
                                else if (action.value.isNotEmpty()) State.ValidationError.FORMAT_PHONE
                                else State.ValidationError.EMPTY
                            )
                        )
                    )

                    registerBtnEnabledFlags["phone"] = phoneValid
                    checkRegisterButton()
                }

                is Action.ChangeFieldsRegister.Password -> {
                    val passwordValid = validator.validatePassword(action.value)

                    _state.emit(
                        _state.value.copy(
                            registerScreenState = _state.value.registerScreenState.copy(
                                passwordValue = action.value,
                                passwordFieldValidation = if (passwordValid) State.ValidationError.NONE
                                else if (action.value.isNotEmpty()) State.ValidationError.FORMAT_PASSWORD
                                else State.ValidationError.EMPTY
                            )
                        )
                    )

                    registerBtnEnabledFlags["password"] = passwordValid && action.value.isNotEmpty()
                    checkRegisterButton()
                }

                //CHANGE PROFILE DATA SCREEN

                is Action.ChangeFieldsProfileData.Gender -> {
                    _state.emit(
                        _state.value.copy(
                            changeProfileDataState = _state.value.changeProfileDataState.copy(
                                genderIsMale = action.isMaleChecked
                            )
                        )
                    )
                }

                is Action.ChangeFieldsProfileData.Firstname -> {
                    val firstnameValid = validator.validateFirstname(action.value)

                    _state.emit(
                        _state.value.copy(
                            changeProfileDataState = _state.value.changeProfileDataState.copy(
                                firstnameValue = action.value,
                                firstnameFieldValidation = if (firstnameValid) State.ValidationError.NONE
                                else if (action.value.isNotEmpty()) State.ValidationError.FORMAT_FIRSTNAME
                                else State.ValidationError.EMPTY
                            )
                        )
                    )

                    changeDataBtnEnabledFlags["firstname"] = firstnameValid

                    checkUpdateProfileButton()
                }

                is Action.ChangeFieldsProfileData.Lastname -> {
                    val lastnameValid = validator.validateLastname(action.value)

                    _state.emit(
                        _state.value.copy(
                            changeProfileDataState = _state.value.changeProfileDataState.copy(
                                lastnameValue = action.value,
                                lastnameFieldValidation = if (lastnameValid) State.ValidationError.NONE
                                else if (action.value.isNotEmpty()) State.ValidationError.FORMAT_LASTNAME
                                else State.ValidationError.EMPTY
                            )
                        )
                    )

                    changeDataBtnEnabledFlags["lastname"] = lastnameValid

                    checkUpdateProfileButton()
                }

                is Action.ChangeFieldsProfileData.Patronymic -> {
                    val patronymicValid = validator.validatePatronymic(action.value)

                    _state.emit(
                        _state.value.copy(

                            changeProfileDataState = _state.value.changeProfileDataState.copy(
                                patronymicValue = action.value,
                                patronymicFieldValidation = if (patronymicValid) State.ValidationError.NONE
                                else State.ValidationError.FORMAT_PATRONYMIC
                            )
                        )
                    )

                    changeDataBtnEnabledFlags["patronymic"] = patronymicValid

                    checkUpdateProfileButton()
                }

                is Action.ChangeFieldsProfileData.Phone -> {
                    val phoneValid = validator.validatePhone(action.value)

                    _state.emit(
                        _state.value.copy(
                            changeProfileDataState = _state.value.changeProfileDataState.copy(
                                phoneValue = action.value,
                                phoneFieldValidation = if (phoneValid) State.ValidationError.NONE
                                else if (action.value.isNotEmpty()) State.ValidationError.FORMAT_PHONE
                                else State.ValidationError.EMPTY
                            )
                        )
                    )

                    changeDataBtnEnabledFlags["phone"] = phoneValid

                    checkUpdateProfileButton()
                }

                //CHANGE EMAIL SCREEN
                is Action.ChangeFieldsEmail.Email -> {
                    val emailValid = validator.validateEmail(action.value)

                    _state.emit(
                        _state.value.copy(
                            changeEmailState = _state.value.changeEmailState.copy(
                                emailValue = action.value,
                                emailFieldValidation = if (emailValid) State.ValidationError.NONE
                                else if (action.value.isNotEmpty()) State.ValidationError.FORMAT_EMAIL
                                else State.ValidationError.EMPTY
                            )
                        )
                    )

                    _state.emit(
                        _state.value.copy(
                            changeEmailState = _state.value.changeEmailState.copy(
                                btnEnabled = _state.value.changeEmailState.emailFieldValidation == State.ValidationError.NONE
                            )
                        )
                    )
                }

                //CHANGE PASSWORD SCREEN

                is Action.ChangeFieldsPassword.OldPassword -> {
                    checkPasswords(
                        _state.value.changePasswordState.newPasswordValue,
                        _state.value.changePasswordState.repeatPasswordValue,
                        action.value
                    )
                }

                is Action.ChangeFieldsPassword.NewPassword -> {
                    checkPasswords(
                        action.value,
                        _state.value.changePasswordState.repeatPasswordValue,
                        _state.value.changePasswordState.oldPasswordValue
                    )
                }

                is Action.ChangeFieldsPassword.RepeatPassword -> {
                    checkPasswords(
                        _state.value.changePasswordState.newPasswordValue,
                        action.value,
                        _state.value.changePasswordState.oldPasswordValue
                    )
                }

                // PASSWORD RECOVERY
                is Action.PasswordRecovery.Code -> {
                    _state.emit(
                        _state.value.copy(
                            passwordRecoveryState = _state.value.passwordRecoveryState.copy(
                                codeField = action.value
                            )
                        )
                    )
                }

                is Action.PasswordRecovery.Email -> {
                    val valid = validator.validateEmail(action.value)

                    _state.emit(
                        _state.value.copy(
                            passwordRecoveryState = _state.value.passwordRecoveryState.copy(
                                emailField = action.value, isEmailValidationError = !valid
                            )
                        )
                    )
                }

                is Action.PasswordRecovery.Password -> {
                    val valid = validator.validatePassword(action.value)

                    _state.emit(
                        _state.value.copy(
                            passwordRecoveryState = _state.value.passwordRecoveryState.copy(
                                passwordField = action.value, isPasswordValidationError = !valid
                            )
                        )
                    )
                }

                is Action.PasswordRecovery.SendCode -> {
                    _state.emit(
                        _state.value.copy(
                            passwordRecoveryState = _state.value.passwordRecoveryState.copy(
                                isCodeError = false,
                                isEmailError = false,
                                isServerError = false,
                                btnsEnabled = false,
                            )
                        )
                    )

                    val response = withContext(Dispatchers.IO) {
                        serviceRepository.sendCode(
                            EmailUpdatable(action.email)
                        )
                    }

                    when (response.result) {
                        ModelResponse.ModelResponseResult.OK -> {}
                        ModelResponse.ModelResponseResult.SERVER_ERROR -> {
                            _state.emit(
                                _state.value.copy(
                                    passwordRecoveryState = _state.value.passwordRecoveryState.copy(
                                        isServerError = true,
                                    )
                                )
                            )
                        }

                        ModelResponse.ModelResponseResult.USER_ERROR -> _state.emit(
                            _state.value.copy(
                                passwordRecoveryState = _state.value.passwordRecoveryState.copy(
                                    isEmailError = true,
                                )
                            )
                        )

                        ModelResponse.ModelResponseResult.FAIL -> _state.emit(
                            _state.value.copy(
                                passwordRecoveryState = _state.value.passwordRecoveryState.copy(
                                    isEmailError = true,
                                )
                            )
                        )
                    }

                    _state.emit(
                        _state.value.copy(
                            passwordRecoveryState = _state.value.passwordRecoveryState.copy(
                                btnsEnabled = true,
                            )
                        )
                    )
                }

                is Action.CloseRecoveryScreen -> {
                    _state.emit(
                        _state.value.copy(
                            recoveryScreenIsOpen = false,
                            passwordRecoveryState = _state.value.passwordRecoveryState.copy(
                                isCodeError = false,
                                isEmailError = false,
                                isServerError = false,
                                isEmailValidationError = false,
                                isPasswordValidationError = false,
                                codeField = "",
                                emailField = "",
                                passwordField = "",
                                btnsEnabled = true,
                            )
                        )
                    )
                }

                is Action.OpenRecoveryScreen -> {
                    _state.emit(
                        _state.value.copy(
                            recoveryScreenIsOpen = true,
                        )
                    )
                }

                is Action.PasswordRecovery.RemoveErrors -> {
                    _state.emit(
                        _state.value.copy(
                            passwordRecoveryState = _state.value.passwordRecoveryState.copy(
                                isCodeError = false,
                                isEmailError = false,
                                isServerError = false,
                            )
                        )
                    )
                }

                is Action.PasswordRecovery.ChangePassword -> {
                    _state.emit(
                        _state.value.copy(
                            passwordRecoveryState = _state.value.passwordRecoveryState.copy(
                                isCodeError = false,
                                isEmailError = false,
                                isServerError = false,
                                btnsEnabled = false,
                            )
                        )
                    )

                    val response = withContext(Dispatchers.IO) {
                        serviceRepository.resetPassword(
                            ResetBody(
                                action.code, action.newPassword.asPassword()
                            )
                        )
                    }

                    when (response.result) {
                        ModelResponse.ModelResponseResult.OK -> {
                            onAction(Action.CloseRecoveryScreen)
                            _result.emit(Result.RecoverySuccess)
                        }

                        ModelResponse.ModelResponseResult.SERVER_ERROR -> {
                            _state.emit(
                                _state.value.copy(
                                    passwordRecoveryState = _state.value.passwordRecoveryState.copy(
                                        isServerError = true,
                                    )
                                )
                            )
                        }

                        ModelResponse.ModelResponseResult.USER_ERROR -> {
                            _state.emit(
                                _state.value.copy(
                                    passwordRecoveryState = _state.value.passwordRecoveryState.copy(
                                        isCodeError = true,
                                    )
                                )
                            )
                        }

                        ModelResponse.ModelResponseResult.FAIL -> {
                            _state.emit(
                                _state.value.copy(
                                    passwordRecoveryState = _state.value.passwordRecoveryState.copy(
                                        isCodeError = true,
                                    )
                                )
                            )
                        }
                    }

                    _state.emit(
                        _state.value.copy(
                            passwordRecoveryState = _state.value.passwordRecoveryState.copy(
                                btnsEnabled = true,
                            )
                        )
                    )

                }
            }
        }
    }

    private suspend fun checkUpdateProfileButton() {
        _state.emit(
            _state.value.copy(
                changeProfileDataState = _state.value.changeProfileDataState.copy(
                    btnEnabled = !changeDataBtnEnabledFlags.values.contains(
                        false
                    )
                )
            )
        )
    }

    private suspend fun checkRegisterButton() {
        _state.emit(
            _state.value.copy(
                registerScreenState = _state.value.registerScreenState.copy(
                    btnEnabled = !registerBtnEnabledFlags.values.contains(false)
                )
            )
        )
    }

    private suspend fun checkPasswords(
        pNew: String, pRepeat: String, pOld: String
    ) {
        _state.emit(
            _state.value.copy(
                changePasswordState = _state.value.changePasswordState.copy(
                    newPasswordFieldValidation = State.ValidationError.NONE,
                    oldPasswordFieldValidation = State.ValidationError.NONE,
                    repeatPasswordFieldValidation = State.ValidationError.NONE,
                    newPasswordValue = pNew,
                    repeatPasswordValue = pRepeat,
                    oldPasswordValue = pOld
                )
            )
        )

        if (pNew.isNotEmpty()) {
            if (pOld.isEmpty()) {
                _state.emit(
                    _state.value.copy(
                        changePasswordState = _state.value.changePasswordState.copy(
                            oldPasswordFieldValidation = State.ValidationError.EMPTY
                        )
                    )
                )
            }
            if (pRepeat.isEmpty()) {
                _state.emit(
                    _state.value.copy(
                        changePasswordState = _state.value.changePasswordState.copy(
                            repeatPasswordFieldValidation = State.ValidationError.EMPTY
                        )
                    )
                )
            }
        }

        if (pRepeat.isNotEmpty()) {
            if (pOld.isEmpty()) {
                _state.emit(
                    _state.value.copy(
                        changePasswordState = _state.value.changePasswordState.copy(
                            oldPasswordFieldValidation = State.ValidationError.EMPTY
                        )
                    )
                )
            }
            if (pNew.isEmpty()) {
                _state.emit(
                    _state.value.copy(
                        changePasswordState = _state.value.changePasswordState.copy(
                            newPasswordFieldValidation = State.ValidationError.EMPTY
                        )
                    )
                )
            }
        }

        if (pOld.isNotEmpty()) {
            if (pRepeat.isEmpty()) {
                _state.emit(
                    _state.value.copy(
                        changePasswordState = _state.value.changePasswordState.copy(
                            repeatPasswordFieldValidation = State.ValidationError.EMPTY
                        )
                    )
                )
            }
            if (pNew.isEmpty()) {
                _state.emit(
                    _state.value.copy(
                        changePasswordState = _state.value.changePasswordState.copy(
                            newPasswordFieldValidation = State.ValidationError.EMPTY
                        )
                    )
                )
            }
        }

        if (pNew.isNotEmpty() && pRepeat.isNotEmpty()) {
            if (pNew != pRepeat) {
                _state.emit(
                    _state.value.copy(
                        changePasswordState = _state.value.changePasswordState.copy(
                            repeatPasswordFieldValidation = State.ValidationError.PASSWORDS_NOT_EQUAL
                        )
                    )
                )
            }
        }

        if (pOld.isNotEmpty()) {
            if (!validator.validatePassword(pOld)) {
                _state.emit(
                    _state.value.copy(
                        changePasswordState = _state.value.changePasswordState.copy(
                            oldPasswordFieldValidation = State.ValidationError.FORMAT_PASSWORD
                        )
                    )
                )
            }
        }

        if (pNew.isNotEmpty()) {
            if (!validator.validatePassword(pNew)) {
                _state.emit(
                    _state.value.copy(
                        changePasswordState = _state.value.changePasswordState.copy(
                            newPasswordFieldValidation = State.ValidationError.FORMAT_PASSWORD
                        )
                    )
                )
            }
        }

        val v1 =
            _state.value.changePasswordState.oldPasswordFieldValidation == State.ValidationError.NONE && _state.value.changePasswordState.oldPasswordValue.isNotEmpty()
        val v2 =
            _state.value.changePasswordState.newPasswordFieldValidation == State.ValidationError.NONE && _state.value.changePasswordState.newPasswordValue.isNotEmpty()
        val v3 =
            _state.value.changePasswordState.repeatPasswordFieldValidation == State.ValidationError.NONE && _state.value.changePasswordState.repeatPasswordValue.isNotEmpty()

        _state.emit(
            _state.value.copy(
                changePasswordState = _state.value.changePasswordState.copy(
                    btnEnabled = v1 && v2 && v3
                )
            )
        )
    }


    sealed class Action {
        object ResetResult : Action()

        object OpenLoginSheet : Action()
        object CloseLoginSheet : Action()
        object OpenRegisterSheet : Action()
        object CloseRegisterSheet : Action()
        object CloseRecoveryScreen : Action()
        object OpenRecoveryScreen : Action()
        object Logout : Action()
        object LogoutToken : Action()
        object LoadUser : Action()

        object UserToUserEditor : Action()

        object ChangeFieldsLogin {
            class Email(val value: String) : Action()
            class Password(val value: String) : Action()
            object RemoveErrors : Action()
        }

        object PasswordRecovery {
            class Email(val value: String) : Action()
            class Code(val value: String) : Action()
            class Password(val value: String) : Action()
            class SendCode(val email: String) : Action()
            object RemoveErrors : Action()
            class ChangePassword(val code: String, val newPassword: String) : Action()
        }

        object ChangeFieldsRegister {
            class Email(val value: String) : Action()
            class Firstname(val value: String) : Action()
            class Lastname(val value: String) : Action()
            class Patronymic(val value: String) : Action()
            class Phone(val value: String) : Action()
            class Password(val value: String) : Action()
            class ChangeGender(val isMaleChecked: Boolean) : Action()
            object RemoveErrors : Action()
        }

        object ChangeFieldsProfileData {
            class Firstname(val value: String) : Action()
            class Lastname(val value: String) : Action()
            class Patronymic(val value: String) : Action()
            class Phone(val value: String) : Action()
            class Gender(val isMaleChecked: Boolean) : Action()
        }

        object ChangeFieldsEmail {
            class Email(val value: String) : Action()
        }

        object ChangeFieldsPassword {
            class OldPassword(val value: String) : Action()
            class NewPassword(val value: String) : Action()
            class RepeatPassword(val value: String) : Action()
        }

        class Login(
            val email: String, val password: String
        ) : Action()

        class Register(
            val email: String,
            val firstname: String,
            val lastname: String,
            val patronymic: String?,
            val phone: String?,
            val password: String,
            val genderIsMale: Boolean
        ) : Action()

        class ChangeUserData(
            val firstname: String,
            val lastname: String,
            val patronymic: String?,
            val phone: String?,
            val genderIsMale: Boolean
        ) : Action()

        class ChangeEmail(
            val email: String,
        ) : Action()

        class ChangePassword(
            val oldPassword: String,
            val newPassword: String,
        ) : Action()
    }

    data class State(
        val isError: Boolean = false,
        val userInfo: User? = null,

        val loginSheetIsOpen: Boolean = false,
        val registerSheetIsOpen: Boolean = false,
        val recoveryScreenIsOpen: Boolean = false,

        val dataHasLoaded: Boolean = false,
        val token: String? = null,

        val registerScreenState: RegisterScreenState = RegisterScreenState(),
        val loginScreenState: LoginScreenState = LoginScreenState(),
        val changeProfileDataState: ChangeProfileDataState = ChangeProfileDataState(),
        val changeEmailState: ChangeEmailState = ChangeEmailState(),
        val changePasswordState: ChangePasswordState = ChangePasswordState(),
        val passwordRecoveryState: PasswordRecoveryState = PasswordRecoveryState()
    ) {
        data class RegisterScreenState(
            val emailValue: String = "",
            val firstnameValue: String = "",
            val lastnameValue: String = "",
            val patronymicValue: String = "",
            val phoneValue: String = "",
            val passwordValue: String = "",
            val genderIsMale: Boolean = true,

            val emailFieldValidation: ValidationError = ValidationError.NONE,
            val firstnameFieldValidation: ValidationError = ValidationError.NONE,
            val lastnameFieldValidation: ValidationError = ValidationError.NONE,
            val patronymicFieldValidation: ValidationError = ValidationError.NONE,
            val phoneFieldValidation: ValidationError = ValidationError.NONE,
            val passwordFieldValidation: ValidationError = ValidationError.NONE,
            val repeatPasswordFieldValidation: ValidationError = ValidationError.NONE,

            val registerError: AuthError = AuthError.NONE,
            val btnEnabled: Boolean = false,
        )

        data class LoginScreenState(
            val emailValue: String = "",
            val passwordValue: String = "",

            val emailFieldValidation: ValidationError = ValidationError.NONE,
            val passwordFieldValidation: ValidationError = ValidationError.NONE,

            val authError: AuthError = AuthError.NONE,
            val btnEnabled: Boolean = false,
        )

        data class ChangeProfileDataState(
            val firstnameValue: String = "",
            val lastnameValue: String = "",
            val patronymicValue: String = "",
            val phoneValue: String = "",
            val genderIsMale: Boolean = true,

            val firstnameFieldValidation: ValidationError = ValidationError.NONE,
            val lastnameFieldValidation: ValidationError = ValidationError.NONE,
            val patronymicFieldValidation: ValidationError = ValidationError.NONE,
            val phoneFieldValidation: ValidationError = ValidationError.NONE,

            val btnEnabled: Boolean = false,
            val changeDataError: AuthError = AuthError.NONE,
        )

        data class ChangeEmailState(
            val emailValue: String = "",

            val emailFieldValidation: ValidationError = ValidationError.NONE,
            val btnEnabled: Boolean = false,
            val changeEmailError: AuthError = AuthError.NONE,
        )

        data class ChangePasswordState(
            val oldPasswordValue: String = "",
            val newPasswordValue: String = "",
            val repeatPasswordValue: String = "",

            val oldPasswordFieldValidation: ValidationError = ValidationError.NONE,
            val newPasswordFieldValidation: ValidationError = ValidationError.NONE,
            val repeatPasswordFieldValidation: ValidationError = ValidationError.NONE,

            val changePasswordError: AuthError = AuthError.NONE,
            val btnEnabled: Boolean = false,
        )

        data class PasswordRecoveryState(
            val emailField: String = "",
            val codeField: String = "",
            val passwordField: String = "",
            val isEmailValidationError: Boolean = false,
            val isPasswordValidationError: Boolean = false,
            val isEmailError: Boolean = false,
            val isServerError: Boolean = false,
            val isCodeError: Boolean = false,
            val btnsEnabled: Boolean = true,
        )

        enum class ValidationError {
            FORMAT_PASSWORD, EMPTY, PASSWORDS_NOT_EQUAL, FORMAT_EMAIL, NONE, FORMAT_FIRSTNAME, FORMAT_PHONE, FORMAT_LASTNAME, FORMAT_PATRONYMIC
        }

        enum class AuthError {
            NONE, INCORRECT, SERVER_ERROR
        }
    }

    sealed class Result {
        class Login(val user: User?, val token: String?) : Result()
        object Register : Result()
        object ChangeUserData : Result()
        object ChangeEmail : Result()
        object ChangePassword : Result()
        object RecoverySuccess : Result()
        object Logout : Result()
        object LogoutToken : Result()
        class UserLoaded(val user: User?, val token: String?) : Result()
    }
}