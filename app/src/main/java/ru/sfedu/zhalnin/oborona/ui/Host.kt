package ru.sfedu.zhalnin.oborona.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.sfedu.zhalnin.oborona.BuildConfig
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.data.model.repository.DefaultServiceRepository
import ru.sfedu.zhalnin.oborona.data.model.repository.ServerApi
import ru.sfedu.zhalnin.oborona.data.model.repository.ServiceRepository
import ru.sfedu.zhalnin.oborona.data.prefs.SecretPreferencesRepo
import ru.sfedu.zhalnin.oborona.data.validators.FieldsValidatorImpl
import ru.sfedu.zhalnin.oborona.ui.common.GlobalModalBottomSheet
import ru.sfedu.zhalnin.oborona.ui.common.LocalGlobalModalBottomSheet
import ru.sfedu.zhalnin.oborona.ui.dialog.CustomSponsorDialog
import ru.sfedu.zhalnin.oborona.ui.events.EventsViewModel
import ru.sfedu.zhalnin.oborona.ui.info.*
import ru.sfedu.zhalnin.oborona.ui.menu.HelpMenu
import ru.sfedu.zhalnin.oborona.ui.menu.MainMenu
import ru.sfedu.zhalnin.oborona.ui.menu.MenuViewModel
import ru.sfedu.zhalnin.oborona.ui.auth.LoginScreen
import ru.sfedu.zhalnin.oborona.ui.auth.PasswordRecoveryScreen
import ru.sfedu.zhalnin.oborona.ui.auth.RegisterScreen
import ru.sfedu.zhalnin.oborona.ui.events.EventsViewModelFactory
import ru.sfedu.zhalnin.oborona.ui.maps.MapsViewModel
import ru.sfedu.zhalnin.oborona.ui.maps.MapsViewModelFactory
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme
import ru.sfedu.zhalnin.oborona.ui.user.ProfileScreen
import ru.sfedu.zhalnin.oborona.ui.user.UserViewModel
import ru.sfedu.zhalnin.oborona.ui.user.UserViewModelFactory
import ru.sfedu.zhalnin.oborona.ui.toolbar.AppToolbarViewModel
import ru.sfedu.zhalnin.oborona.ui.top_toolbar.TopToolbarViewModel

private fun provideRetrofit(): Retrofit {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
    return Retrofit.Builder().baseUrl(BuildConfig.SERVER_IP).client(client)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

private fun provideApi(retrofit: Retrofit): ServerApi = retrofit.create(ServerApi::class.java)

@Composable
fun Host() {
    val context = LocalContext.current

    val serviceRepository: ServiceRepository by remember {
        mutableStateOf(DefaultServiceRepository(provideApi(provideRetrofit())))
    }

    val secretPrefs by remember {
        mutableStateOf(SecretPreferencesRepo().get(context = context))
    }

    val validator by remember {
        mutableStateOf(FieldsValidatorImpl())
    }

    val navController = rememberNavController()
    val infoScreenViewModel: InfoScreenViewModel = viewModel(
        factory = InfoScreenViewModelFactory(
            serviceRepository = serviceRepository,
        )
    )
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(
            prefs = secretPrefs,
            serviceRepository = serviceRepository,
            validator = validator
        )
    )
    val topToolbarViewModel: TopToolbarViewModel = viewModel()

    val appToolbarViewModel: AppToolbarViewModel = viewModel()

    val mainScreenViewModel: MainScreenViewModel = viewModel()

    val eventsViewModel: EventsViewModel = viewModel(
        factory = EventsViewModelFactory(
            serviceRepository = serviceRepository,
        )
    )
    val mapsViewModel: MapsViewModel = viewModel(
        factory = MapsViewModelFactory(
            serviceRepository = serviceRepository,
        )
    )

    LaunchedEffect(true) {
        eventsViewModel.onAction(EventsViewModel.Action.LoadEvents)
        mapsViewModel.onAction(MapsViewModel.Action.LoadData)
        infoScreenViewModel.onAction(InfoScreenViewModel.Action.LoadInfoWindows)
        userViewModel.onAction(UserViewModel.Action.LoadUser)
        eventsViewModel.onAction(EventsViewModel.Action.LoadUserEntries)
    }

    val eventsError = eventsViewModel.state.collectAsState().value.isError
    val mapsError = mapsViewModel.state.collectAsState().value.isError
    val infoError = infoScreenViewModel.state.collectAsState().value.isError
    val userError = userViewModel.state.collectAsState().value.isError

    val eventsState by eventsViewModel.state.collectAsState()

    val isError = mapsError || eventsError || infoError || userError

    LaunchedEffect(isError) {
        while (isError) {
            eventsViewModel.onAction(EventsViewModel.Action.LoadEvents)
            mapsViewModel.onAction(MapsViewModel.Action.LoadData)
            infoScreenViewModel.onAction(InfoScreenViewModel.Action.LoadInfoWindows)
            userViewModel.onAction(UserViewModel.Action.LoadUser)
            eventsViewModel.onAction(EventsViewModel.Action.LoadUserEntries)

            if (eventsState.isEventScreenOpen && eventsState.openingFullEventInfo != null) {
                eventsViewModel.onAction(
                    EventsViewModel.Action.OpenFullEventInfo(
                        eventsState.openingFullEventInfo!!
                    )
                )
            }

            delay(5000)
        }
    }

    val infoScreenState by infoScreenViewModel.state.collectAsState()
    val userState by userViewModel.state.collectAsState()
    val mainToolbarSnackbarState = remember { SnackbarHostState() }
    val userEditorSnackbarState = remember { SnackbarHostState() }

    val userViewModelResult by userViewModel.result.collectAsState()
    val eventsViewModelResult by eventsViewModel.result.collectAsState()

    val ok = stringResource(id = R.string.ok)

    val m1 = stringResource(R.string.badEnroll)
    val m2 = stringResource(R.string.successEnroll)
    val m3 = stringResource(R.string.cannotLoadEntries)
    val m4 = stringResource(R.string.cannotUnsubscribe)
    val m5 = stringResource(R.string.successUnsubscribe)
    val m6 = stringResource(R.string.passwordChanged)
    val m7 = stringResource(R.string.dataChanged)
    val m8 = stringResource(R.string.authSuccess)
    val m9 = stringResource(R.string.activationSent)
    val m10 = stringResource(R.string.logout)
    val m11 = stringResource(R.string.badSession)
    val m12 = stringResource(R.string.passwordChangeSuccess)

    LaunchedEffect(eventsViewModelResult) {
        when (eventsViewModelResult) {
            EventsViewModel.Result.FailedEnroll -> {
                eventsViewModel.onAction(EventsViewModel.Action.CloseFullRole)
                eventsViewModel.onAction(EventsViewModel.Action.LoadUserEntries)
                mainToolbarSnackbarState.showSnackbar(
                    message = m1,
                    actionLabel = ok,
                    SnackbarDuration.Short
                )
            }

            EventsViewModel.Result.SuccessEnroll -> {
                eventsViewModel.onAction(EventsViewModel.Action.CloseFullRole)
                eventsViewModel.onAction(EventsViewModel.Action.LoadUserEntries)
                mainToolbarSnackbarState.showSnackbar(
                    message = m2,
                    actionLabel = ok,
                    SnackbarDuration.Short
                )
            }

            EventsViewModel.Result.BadUser -> {
                userViewModel.onAction(UserViewModel.Action.LogoutToken)
            }

            EventsViewModel.Result.CantLoadEntries -> {
                mainToolbarSnackbarState.showSnackbar(
                    message = m3,
                    actionLabel = ok,
                    SnackbarDuration.Short
                )
            }

            EventsViewModel.Result.FailedUnsubscribe -> {
                eventsViewModel.onAction(EventsViewModel.Action.LoadUserEntries)
                mainToolbarSnackbarState.showSnackbar(
                    message = m4,
                    actionLabel = ok,
                    SnackbarDuration.Short
                )
            }

            EventsViewModel.Result.SuccessUnsubscribe -> {
                eventsViewModel.onAction(EventsViewModel.Action.LoadUserEntries)
                mainToolbarSnackbarState.showSnackbar(
                    message = m5,
                    actionLabel = ok,
                    SnackbarDuration.Short
                )
            }

            null -> {}
        }

        if (eventsViewModelResult != null) {
            eventsViewModel.onAction(EventsViewModel.Action.ResultCaught)
        }
    }

    LaunchedEffect(userViewModelResult) {
        when (val result = userViewModelResult) {
            is UserViewModel.Result.RecoverySuccess -> {
                mainToolbarSnackbarState.showSnackbar(
                    message = m6,
                    actionLabel = ok,
                    SnackbarDuration.Short
                )
            }

            is UserViewModel.Result.ChangeUserData -> {
                userEditorSnackbarState.showSnackbar(
                    message = m7,
                    actionLabel = ok,
                    SnackbarDuration.Short
                )
            }

            is UserViewModel.Result.Login -> {
                eventsViewModel.onAction(EventsViewModel.Action.SetUserInfo(result.user))
                eventsViewModel.onAction(EventsViewModel.Action.SetUserToken(result.token))
                infoScreenViewModel.onAction(InfoScreenViewModel.Action.ProvideToken(result.token))
                topToolbarViewModel.onAction(TopToolbarViewModel.Action.ProvideUser(result.user))

                mainToolbarSnackbarState.showSnackbar(
                    message = m8,
                    actionLabel = ok,
                    SnackbarDuration.Short
                )
            }

            UserViewModel.Result.Register -> {
                mainToolbarSnackbarState.showSnackbar(
                    message = m9,
                    actionLabel = ok,
                    duration = SnackbarDuration.Long
                )
            }

            UserViewModel.Result.ChangeEmail -> {
                userEditorSnackbarState.showSnackbar(
                    message = m9,
                    actionLabel = ok,
                    duration = SnackbarDuration.Long
                )
            }

            UserViewModel.Result.Logout -> {
                eventsViewModel.onAction(EventsViewModel.Action.SetUserInfo(null))
                eventsViewModel.onAction(EventsViewModel.Action.SetUserToken(null))
                infoScreenViewModel.onAction(InfoScreenViewModel.Action.ProvideToken(null))
                topToolbarViewModel.onAction(TopToolbarViewModel.Action.ProvideUser(null))
                eventsViewModel.onAction(EventsViewModel.Action.LoadUserEntries)

                mainToolbarSnackbarState.showSnackbar(
                    message = m10,
                    actionLabel = ok,
                    SnackbarDuration.Short
                )
            }

            UserViewModel.Result.LogoutToken -> {
                navController.navigate("main")
                eventsViewModel.onAction(EventsViewModel.Action.SetUserInfo(null))
                eventsViewModel.onAction(EventsViewModel.Action.SetUserToken(null))
                infoScreenViewModel.onAction(InfoScreenViewModel.Action.ProvideToken(null))
                topToolbarViewModel.onAction(TopToolbarViewModel.Action.ProvideUser(null))

                eventsViewModel.onAction(EventsViewModel.Action.LoadUserEntries)
                mainToolbarSnackbarState.showSnackbar(
                    message = m11,
                    actionLabel = ok,
                    SnackbarDuration.Short
                )
            }

            UserViewModel.Result.ChangePassword -> {
                userEditorSnackbarState.showSnackbar(
                    message = m12,
                    actionLabel = ok,
                    SnackbarDuration.Short
                )
            }

            is UserViewModel.Result.UserLoaded -> {
                eventsViewModel.onAction(EventsViewModel.Action.SetUserInfo(result.user))
                eventsViewModel.onAction(EventsViewModel.Action.SetUserToken(result.token))
                infoScreenViewModel.onAction(InfoScreenViewModel.Action.ProvideToken(result.token))
                topToolbarViewModel.onAction(TopToolbarViewModel.Action.ProvideUser(result.user))

                eventsViewModel.onAction(EventsViewModel.Action.LoadUserEntries)
            }

            null -> {}
        }

        if (userViewModelResult != null) {
            userViewModel.onAction(UserViewModel.Action.ResetResult)
        }
    }

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreenHost(
                modifier = Modifier,
                mainScreenViewModel = mainScreenViewModel,
                topToolbarViewModel = topToolbarViewModel,
                appToolbarViewModel = appToolbarViewModel,
                eventsViewModel = eventsViewModel,
                mapsViewModel = mapsViewModel,
                infoScreenViewModel = infoScreenViewModel,
                userScreenViewModel = userViewModel,
                openMenu = {
                    navController.navigate("main_menu") {
                        popUpTo("main") { inclusive = false }
                    }
                },
                snackbarHostState = mainToolbarSnackbarState
            )
        }
        composable("main_menu") {
            MainMenu(
                modifier = Modifier,
                userIsLoggedIn = userState.userInfo != null,
                onBackClicked = {
                    navController.popBackStack()
                },
                onAction = {
                    when (it) {
                        MenuViewModel.Action.AboutAppClicked -> {
                            navController.navigate("about_app") {
                                popUpTo("main_menu") { inclusive = false }
                            }
                        }

                        MenuViewModel.Action.AboutFestivalClicked -> {
                            navController.navigate("about_festival") {
                                popUpTo("main_menu") { inclusive = false }
                            }
                        }

                        MenuViewModel.Action.ContactsClicked -> {
                            navController.navigate("contacts") {
                                popUpTo("main_menu") { inclusive = false }
                            }
                        }

                        MenuViewModel.Action.HelpMenuClicked -> {
                            navController.navigate("help_menu") {
                                popUpTo("main_menu") { inclusive = false }
                            }
                        }

                        MenuViewModel.Action.LogoutClicked -> {
                            userViewModel.onAction(
                                UserViewModel.Action.Logout
                            )
                            navController.popBackStack()
                        }

                        MenuViewModel.Action.ProfileClicked -> {
                            userViewModel.onAction(UserViewModel.Action.UserToUserEditor)

                            navController.navigate("profile") {
                                popUpTo("main_menu") { inclusive = false }
                            }
                        }


                        else -> {}
                    }
                }
            )
        }
        composable("help_menu") {
            HelpMenu(
                modifier = Modifier,
                onBackClicked = {
                    navController.popBackStack()
                },
                onAction = {
                    when (it) {
                        MenuViewModel.Action.DonateClicked -> {
                            navController.navigate("info_window/6") {
                                popUpTo("help_menu") { inclusive = false }
                            }
                        }

                        MenuViewModel.Action.ObslClicked -> {
                            navController.navigate("info_window/3") {
                                popUpTo("help_menu") { inclusive = false }
                            }
                        }

                        MenuViewModel.Action.SponsorClicked -> {
                            navController.navigate("info_window/5") {
                                popUpTo("help_menu") { inclusive = false }
                            }
                        }

                        MenuViewModel.Action.TechClicked -> {
                            navController.navigate("info_window/2") {
                                popUpTo("help_menu") { inclusive = false }
                            }
                        }

                        MenuViewModel.Action.TvorchClicked -> {
                            navController.navigate("info_window/4") {
                                popUpTo("help_menu") { inclusive = false }
                            }
                        }

                        MenuViewModel.Action.VolunteerClicked -> {
                            navController.navigate("info_window/1") {
                                popUpTo("help_menu") { inclusive = false }
                            }
                        }

                        else -> {}
                    }
                }
            )
        }
        composable(
            "info_window/{infoWindowId}",
            arguments = listOf(navArgument("infoWindowId") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("infoWindowId") ?: 1

            InfoScreen(
                data = infoScreenState.infoWindowsData[id - 1],
                isButtonVisible = id == 5 && userState.userInfo != null,
                onButtonClicked = {
                    infoScreenViewModel.onAction(InfoScreenViewModel.Action.OpenSponsorDialog)
                },
                onBackClicked = {
                    navController.popBackStack()
                }
            )

            if (infoScreenState.dialogState.isDialogOpen) {
                CustomSponsorDialog(
                    state = infoScreenState.dialogState,
                    sendCode = { infoScreenViewModel.onAction(InfoScreenViewModel.Action.DialogSendBtnClicked) },
                    onDismiss = { infoScreenViewModel.onAction(InfoScreenViewModel.Action.DismissDialog) },
                    onValueChange = { value ->
                        infoScreenViewModel.onAction(
                            InfoScreenViewModel.Action.DialogValueChanged(
                                value
                            )
                        )
                    },
                )
            }
        }
        composable("about_app") {
            AboutDevelopersScreen(
                modifier = Modifier,
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }
        composable("contacts") {
            ContactsScreen(
                modifier = Modifier,
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }
        composable("about_festival") {
            AboutFestivalScreen(
                modifier = Modifier,
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }
        composable("profile") {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (contentRef, snackbarRef) = createRefs()

                ProfileScreen(
                    modifier = Modifier.constrainAs(contentRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    onBackClicked = {
                        navController.popBackStack()
                    },
                    changeEmailDataState = userState.changeEmailState,
                    changePasswordDataState = userState.changePasswordState,
                    changeProfileDataState = userState.changeProfileDataState,
                    onAction = userViewModel::onAction,
                    changeProfile = { lastnameValue,
                                      firstnameValue,
                                      patronymicValue,
                                      phoneValue,
                                      genderIsMale ->
                        userViewModel.onAction(
                            UserViewModel.Action.ChangeUserData(
                                lastname = lastnameValue,
                                firstname = firstnameValue,
                                patronymic = patronymicValue,
                                phone = phoneValue,
                                genderIsMale = genderIsMale
                            )
                        )
                    },
                    changeEmail = { emailValue ->
                        userViewModel.onAction(
                            UserViewModel.Action.ChangeEmail(
                                email = emailValue
                            )
                        )
                    },
                    changePassword = { old, new, _ ->
                        userViewModel.onAction(
                            UserViewModel.Action.ChangePassword(
                                oldPassword = old,
                                newPassword = new,
                            )
                        )
                    },
                )

                SnackbarHost(
                    hostState = userEditorSnackbarState,
                    modifier = Modifier
                        .constrainAs(snackbarRef) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                ) {
                    Snackbar(
                        modifier = Modifier,
                        elevation = 0.dp,
                        shape = RoundedCornerShape(0.dp),
                        action = {
                            it.actionLabel?.let {
                                TextButton(
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = AppTheme.colors.primary
                                    ),
                                    onClick = {
                                        userEditorSnackbarState.currentSnackbarData?.dismiss()
                                    }) {
                                    Text(text = it)
                                }
                            }
                        }
                    ) {
                        Text(text = it.message)
                    }
                }
            }
        }
    }

    GlobalModalBottomSheet {
        val modal = LocalGlobalModalBottomSheet.current

        LaunchedEffect(userState.registerSheetIsOpen) {
            if (userState.registerSheetIsOpen) {
                modal.show(
                    onDismiss = { userViewModel.onAction(UserViewModel.Action.CloseRegisterSheet) }
                ) {
                    RegisterScreen(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                        ),
                        onAction = userViewModel::onAction,
                        state = userState,
                        toLoginScreen = {
                            userViewModel.onAction(UserViewModel.Action.CloseRegisterSheet)
                            userViewModel.onAction(UserViewModel.Action.OpenLoginSheet)
                        },
                        onBackClicked = {
                            userViewModel.onAction(UserViewModel.Action.CloseRegisterSheet)
                        },
                    )
                }
            } else {
                modal.hide()
            }
        }
    }

    var recoveryTimerTicks by remember { mutableStateOf(0) }

    LaunchedEffect(recoveryTimerTicks == 60) {
        while (recoveryTimerTicks != 0) {
            recoveryTimerTicks--
            delay(1000)
        }
    }

    GlobalModalBottomSheet {
        val modal = LocalGlobalModalBottomSheet.current

        LaunchedEffect(userState.recoveryScreenIsOpen) {
            if (userState.recoveryScreenIsOpen) {
                modal.show(
                    onDismiss = { userViewModel.onAction(UserViewModel.Action.CloseRecoveryScreen) }
                ) {
                    PasswordRecoveryScreen(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                        ),
                        state = userState.passwordRecoveryState,
                        changeEmail = {
                            userViewModel.onAction(
                                UserViewModel.Action.PasswordRecovery.Email(
                                    it
                                )
                            )
                        },
                        timerTicks = recoveryTimerTicks,
                        changeCode = {
                            userViewModel.onAction(
                                UserViewModel.Action.PasswordRecovery.Code(
                                    it
                                )
                            )
                        },
                        changePassword = {
                            userViewModel.onAction(
                                UserViewModel.Action.PasswordRecovery.Password(
                                    it
                                )
                            )
                        },
                        onBackClicked = { userViewModel.onAction(UserViewModel.Action.CloseRecoveryScreen) },
                        removeErrors = { userViewModel.onAction(UserViewModel.Action.PasswordRecovery.RemoveErrors) },
                        sendCode = {
                            userViewModel.onAction(
                                UserViewModel.Action.PasswordRecovery.SendCode(
                                    it
                                )
                            )
                            recoveryTimerTicks = 60
                        },
                        sendNewPassword = { code, password ->
                            userViewModel.onAction(
                                UserViewModel.Action.PasswordRecovery.ChangePassword(
                                    code,
                                    password
                                )
                            )
                        },
                        toLoginScreen = {
                            userViewModel.onAction(UserViewModel.Action.CloseRecoveryScreen)
                            userViewModel.onAction(UserViewModel.Action.OpenLoginSheet)
                        })
                }
            } else {
                modal.hide()
            }
        }
    }

    GlobalModalBottomSheet {
        val modal = LocalGlobalModalBottomSheet.current

        LaunchedEffect(userState.loginSheetIsOpen) {
            if (userState.loginSheetIsOpen) {
                modal.show(
                    onDismiss = {
                        userViewModel.onAction(UserViewModel.Action.CloseLoginSheet)
                    }
                ) {
                    LoginScreen(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 32.dp
                        ),
                        onPasswordChanged = {
                            userViewModel.onAction(
                                UserViewModel.Action.ChangeFieldsLogin.Password(
                                    it
                                )
                            )
                        },
                        onEmailChanged = {
                            userViewModel.onAction(
                                UserViewModel.Action.ChangeFieldsLogin.Email(
                                    it
                                )
                            )
                        },
                        onLogin = { login, password ->
                            userViewModel.onAction(UserViewModel.Action.Login(login, password))
                        },
                        btnEnabled = (userState.loginScreenState.emailFieldValidation == UserViewModel.State.ValidationError.NONE
                                && userState.loginScreenState.passwordFieldValidation == UserViewModel.State.ValidationError.NONE
                                && userState.loginScreenState.emailValue.isNotEmpty()
                                && userState.loginScreenState.passwordValue.isNotEmpty()
                                && userState.loginScreenState.btnEnabled
                                ),
                        email = userState.loginScreenState.emailValue,
                        password = userState.loginScreenState.passwordValue,
                        emailValidationError = userState.loginScreenState.emailFieldValidation,
                        passwordValidationError = userState.loginScreenState.passwordFieldValidation,
                        onFieldFocused = { userViewModel.onAction(UserViewModel.Action.ChangeFieldsLogin.RemoveErrors) },
                        authError = userState.loginScreenState.authError,
                        toRegisterScreen = {
                            userViewModel.onAction(UserViewModel.Action.CloseLoginSheet)
                            userViewModel.onAction(UserViewModel.Action.OpenRegisterSheet)
                        },
                        toPasswordRecovery = {
                            userViewModel.onAction(UserViewModel.Action.CloseLoginSheet)
                            userViewModel.onAction(UserViewModel.Action.OpenRecoveryScreen)
                        },
                        onBackClicked = {
                            userViewModel.onAction(UserViewModel.Action.CloseLoginSheet)
                        }
                    )
                }
            } else {
                modal.hide()
            }
        }
    }
}
