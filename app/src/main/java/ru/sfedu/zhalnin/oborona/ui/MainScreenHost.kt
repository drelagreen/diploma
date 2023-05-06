package ru.sfedu.zhalnin.oborona.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.rememberCameraPositionState
import ru.sfedu.zhalnin.oborona.data.model.dto.InfoWindow
import ru.sfedu.zhalnin.oborona.ui.calendar.CalendarContent
import ru.sfedu.zhalnin.oborona.ui.dialog.ConfirmationDialog
import ru.sfedu.zhalnin.oborona.ui.events.EventCardContent
import ru.sfedu.zhalnin.oborona.ui.events.EventCardImage
import ru.sfedu.zhalnin.oborona.ui.events.EventsViewModel
import ru.sfedu.zhalnin.oborona.ui.events.ReconstructorPageContent
import ru.sfedu.zhalnin.oborona.ui.home.EventList
import ru.sfedu.zhalnin.oborona.ui.info.InfoScreen
import ru.sfedu.zhalnin.oborona.ui.info.InfoScreenViewModel
import ru.sfedu.zhalnin.oborona.ui.maps.MapDefaults.DEFAULT_LOCATION
import ru.sfedu.zhalnin.oborona.ui.maps.MapDefaults.DEFAULT_ZOOM
import ru.sfedu.zhalnin.oborona.ui.maps.MapsScreen
import ru.sfedu.zhalnin.oborona.ui.maps.MapsViewModel
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme
import ru.sfedu.zhalnin.oborona.ui.toolbar.AppToolbar
import ru.sfedu.zhalnin.oborona.ui.toolbar.AppToolbarViewModel
import ru.sfedu.zhalnin.oborona.ui.top_toolbar.TopToolbar
import ru.sfedu.zhalnin.oborona.ui.top_toolbar.TopToolbarViewModel
import ru.sfedu.zhalnin.oborona.ui.user.UserScreen
import ru.sfedu.zhalnin.oborona.ui.user.UserViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreenHost(
    modifier: Modifier = Modifier,
    topToolbarViewModel: TopToolbarViewModel,
    appToolbarViewModel: AppToolbarViewModel,
    mainScreenViewModel: MainScreenViewModel,
    eventsViewModel: EventsViewModel,
    mapsViewModel: MapsViewModel,
    infoScreenViewModel: InfoScreenViewModel,
    userScreenViewModel: UserViewModel,
    snackbarHostState: SnackbarHostState,
    openMenu: () -> Unit = {},
) {
    var loadingEvents by remember { mutableStateOf(true) }
    var loadingMap by remember { mutableStateOf(true) }
    var googleMapLoaded by remember { mutableStateOf(false) }
    var loadingInfoScreens by remember { mutableStateOf(false) }
    var loadingUserScreen by remember { mutableStateOf(false) }

    val mapCameraPositionState = rememberCameraPositionState()

    val topToolbarState by topToolbarViewModel.state.collectAsState()
    val mainScreenState by mainScreenViewModel.state.collectAsState()
    val appToolbarState by appToolbarViewModel.state.collectAsState()
    val eventsState by eventsViewModel.state.collectAsState()
    val mapsState by mapsViewModel.state.collectAsState()
    val infoScreensState by infoScreenViewModel.state.collectAsState()
    val userScreenState by userScreenViewModel.state.collectAsState()

    val appToolbarResult by appToolbarViewModel.result.collectAsState()
    val topToolbarResult by topToolbarViewModel.result.collectAsState()

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val userHeight = (screenHeight.value / 1.7).dp
    val homeHeight = screenHeight - 64.dp
    val commonHeight = screenHeight - 48.dp
    val mapHeight = screenHeight - 48.dp

    val calendarListState = rememberLazyListState()

    BackHandler(eventsState.isEventScreenOpen && eventsState.openedFullRole == null) {
        eventsViewModel.onAction(EventsViewModel.Action.CloseFullEvent)
        topToolbarViewModel.onAction(TopToolbarViewModel.Action.ChangeMode(TopToolbarViewModel.Mode.SEARCH))
    }

    BackHandler(eventsState.openedFullRole != null) {
        eventsViewModel.onAction(EventsViewModel.Action.CloseFullRole)
    }

    LaunchedEffect(true) {
        userScreenViewModel.onAction(UserViewModel.Action.LoadUser)
    }

    LaunchedEffect(userScreenState) {
        loadingUserScreen = !userScreenState.dataHasLoaded
    }

    LaunchedEffect(eventsState) {
        loadingEvents = !eventsState.dataHasLoaded
    }

    LaunchedEffect(mapsState) {
        loadingMap = !mapsState.dataHasLoaded
    }

    LaunchedEffect(infoScreensState) {
        loadingInfoScreens = !infoScreensState.dataHasLoaded
    }

    LaunchedEffect(topToolbarResult) {
        when (val result = topToolbarResult) {
            TopToolbarViewModel.Result.BackClicked -> {
                if (eventsState.isEventScreenOpen && eventsState.openedFullRole == null) {
                    eventsViewModel.onAction(EventsViewModel.Action.CloseFullEvent)
                    topToolbarViewModel.onAction(
                        TopToolbarViewModel.Action.ChangeMode(
                            TopToolbarViewModel.Mode.SEARCH
                        )
                    )
                } else if (eventsState.openedFullRole != null) {
                    eventsViewModel.onAction(EventsViewModel.Action.CloseFullRole)
                }
            }
            is TopToolbarViewModel.Result.Filter -> {
                eventsViewModel.onAction(
                    EventsViewModel.Action.SetFilter(result.value)
                )
            }

            is TopToolbarViewModel.Result.OpenMenu -> {
                openMenu()
            }

            null -> {}
        }
        if (topToolbarResult != null) {
            topToolbarViewModel.onAction(TopToolbarViewModel.Action.ResultCaught)
        }
    }

    LaunchedEffect(appToolbarResult) {
        when (appToolbarResult) {
            AppToolbarViewModel.Result.CalendarButtonClicked -> {
                mainScreenViewModel.onAction(
                    MainScreenViewModel.Action.OpenCalendar
                )
                topToolbarViewModel.onAction(
                    TopToolbarViewModel.Action.ChangeMode(
                        TopToolbarViewModel.Mode.COMMON
                    )
                )
                topToolbarViewModel.onAction(
                    TopToolbarViewModel.Action.ChangeBackButtonSate(
                        false
                    )
                )
                topToolbarViewModel.onAction(
                    TopToolbarViewModel.Action.ChangeTittle(
                        "Календарь"
                    )
                )
            }
            AppToolbarViewModel.Result.HomeButtonClicked -> {
                mainScreenViewModel.onAction(
                    MainScreenViewModel.Action.OpenHome
                )
                topToolbarViewModel.onAction(
                    TopToolbarViewModel.Action.ChangeMode(
                        TopToolbarViewModel.Mode.SEARCH
                    )
                )
            }
            AppToolbarViewModel.Result.MapButtonClicked -> {
                mainScreenViewModel.onAction(
                    MainScreenViewModel.Action.OpenMap
                )
                topToolbarViewModel.onAction(
                    TopToolbarViewModel.Action.ChangeMode(
                        TopToolbarViewModel.Mode.COMMON
                    )
                )
                topToolbarViewModel.onAction(
                    TopToolbarViewModel.Action.ChangeBackButtonSate(
                        false
                    )
                )
                topToolbarViewModel.onAction(
                    TopToolbarViewModel.Action.ChangeTittle(
                        "Карта"
                    )
                )
            }
            AppToolbarViewModel.Result.UserButtonClicked -> {
                mainScreenViewModel.onAction(
                    MainScreenViewModel.Action.OpenUser
                )
                topToolbarViewModel.onAction(
                    TopToolbarViewModel.Action.ChangeMode(
                        TopToolbarViewModel.Mode.USER
                    )
                )
            }
            AppToolbarViewModel.Result.None -> {}
        }
    }

    LaunchedEffect(eventsState.isEventScreenOpen) {
        if (eventsState.isEventScreenOpen) {
            topToolbarViewModel.onAction(
                TopToolbarViewModel.Action.ChangeMode(TopToolbarViewModel.Mode.COMMON)
            )
            topToolbarViewModel.onAction(
                TopToolbarViewModel.Action.ChangeBackButtonSate(
                    true
                )
            )
            topToolbarViewModel.onAction(
                TopToolbarViewModel.Action.ChangeTittle(
                    ""
                )
            )
        }
    }

    BeautifulLoading(
        modifier = modifier.fillMaxWidth(),
        isError = eventsState.isError || mapsState.isError || infoScreensState.isError || userScreenState.isError,
        loading = loadingEvents || loadingMap || loadingInfoScreens || loadingUserScreen,
        surfaceHeight = {
            if (eventsState.isEventScreenOpen) {
                LocalConfiguration.current.screenHeightDp.dp - 240.dp
            } else when (mainScreenState.currentScreen) {
                MainScreenViewModel.CurrentScreen.HOME -> homeHeight
                MainScreenViewModel.CurrentScreen.MAP -> mapHeight
                MainScreenViewModel.CurrentScreen.CALENDAR -> commonHeight
                MainScreenViewModel.CurrentScreen.USER -> userHeight
            }
        },
        backContent = {
            if (eventsState.isEventScreenOpen) {
                EventCardImage(imageUrl = eventsState.openedFullEvent?.imgUrl.orEmpty())
            }
        }
    ) {
        ConstraintLayout {
            val (toolbarRef, contentRef, topToolbarRef, snackbarRef, roleRef, recRef) = createRefs()

            Box(modifier = Modifier.constrainAs(contentRef) {
                top.linkTo(parent.top)
                bottom.linkTo(toolbarRef.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)

                height = Dimension.fillToConstraints
            }) {
                if (eventsState.isEventScreenOpen && eventsState.openedFullEvent != null) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = AppTheme.colors.transparent
                    ) {
                        val unsubscribe =
                            eventsState.userEntries.find { it.event.id == eventsState.openedFullEvent?.id } != null

                        var showConfirmationDialog by remember { mutableStateOf(false) }

                        EventCardContent(
                            eventData = eventsState.openedFullEvent!!,
                            openRoleDescription = {
                                if (unsubscribe) {
                                    showConfirmationDialog = true
                                } else {
                                    eventsViewModel.onAction(
                                        EventsViewModel.Action.OpenFullRole(
                                            it
                                        )
                                    )
                                }
                            },
                            unsubscribe = unsubscribe,
                            userLoaded = eventsState.user != null && !eventsState.userToken.isNullOrBlank()
                        )

                        AnimatedVisibility(
                            visible = showConfirmationDialog,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            ConfirmationDialog(
                                question = "Вы действительно хотите отписаться от события?",
                                onDismiss = {
                                    showConfirmationDialog = false
                                },
                                onConfirm = {
                                    eventsViewModel.onAction(EventsViewModel.Action.Unsubscribe)
                                    showConfirmationDialog = false
                                }
                            )
                        }
                    }
                } else when (mainScreenState.currentScreen) {
                    MainScreenViewModel.CurrentScreen.USER -> {
                        UserScreen(
                            userInfo = userScreenState.userInfo,
                            onLoginClicked = {
                                userScreenViewModel.onAction(
                                    UserViewModel.Action.OpenLoginSheet
                                )
                            },
                        )
                    }
                    MainScreenViewModel.CurrentScreen.MAP -> {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = AppTheme.colors.transparent
                        ) {
                            MapsScreen(
                                cameraPositionState = mapCameraPositionState,
                                state = mapsState,
                                onEventsVisibilityChecked = {
                                    mapsViewModel.onAction(
                                        MapsViewModel.Action.CheckEvents(
                                            it
                                        )
                                    )
                                },
                                onHorecassVisibilityChecked = {
                                    mapsViewModel.onAction(
                                        MapsViewModel.Action.CheckHorecas(
                                            it
                                        )
                                    )
                                },
                                onEventClick = {
                                    mapsViewModel.onAction(
                                        MapsViewModel.Action.OpenEvent(
                                            it
                                        )
                                    )
                                },
                                onHorecaClick = {
                                    mapsViewModel.onAction(
                                        MapsViewModel.Action.OpenHoreca(
                                            it
                                        )
                                    )
                                },
                                onDismiss = {
                                    mapsViewModel.onAction(
                                        MapsViewModel.Action.CloseBottomSheet
                                    )
                                },
                                mapLoadedCallback = {
                                    if (!googleMapLoaded) {
                                        googleMapLoaded = true
                                        mapCameraPositionState.move(
                                            CameraUpdateFactory.newLatLngZoom(
                                                DEFAULT_LOCATION,
                                                DEFAULT_ZOOM
                                            )
                                        )
                                    }
                                }
                            )
                        }
                    }
                    MainScreenViewModel.CurrentScreen.HOME ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth(),
                            color = AppTheme.colors.transparent
                        ) {
                            EventList(
                                state = eventsState,
                                hideSpecial = false,
                                onEventClicked = {
                                    eventsViewModel.onAction(
                                        EventsViewModel.Action.OpenFullEventInfo(
                                            it
                                        )
                                    )
                                }
                            )
                        }

                    MainScreenViewModel.CurrentScreen.CALENDAR -> {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = AppTheme.colors.transparent
                        ) {
                            CalendarContent(
                                modifier = Modifier.padding(top = 48.dp),
                                eventsState = eventsState,
                                onEventsListButtonClicked = {
                                    appToolbarViewModel.onAction(
                                        AppToolbarViewModel.Action.HomeButtonClicked(true)
                                    )
                                },
                                lazyListState = calendarListState,
                                unsubscribe = {
                                    eventsViewModel.onAction(
                                        EventsViewModel.Action.UnsubscribeEntry(it)
                                    )
                                }
                            )
                        }
                    }
                }
            }
            TopToolbar(
                modifier = Modifier.constrainAs(topToolbarRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                state = topToolbarState,
                onAction = topToolbarViewModel::onAction
            )

            AnimatedVisibility(
                modifier = Modifier.constrainAs(toolbarRef) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                enter = fadeIn(),
                exit = fadeOut(),
                visible = !eventsState.isEventScreenOpen
            ) {
                AppToolbar(
                    state = appToolbarState,
                    onAction = appToolbarViewModel::onAction
                )
            }

            AnimatedVisibility(
                modifier = Modifier.constrainAs(roleRef) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                visible = eventsState.openedFullRole != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                if (eventsState.openedFullRole?.description != null) {
                    Surface {
                        var showConfirmationDialog by remember { mutableStateOf(false) }

                        InfoScreen(
                            data = InfoWindow(
                                id = "",
                                name = eventsState.openedFullRole?.roleName ?: "",
                                description = eventsState.openedFullRole?.description
                                    ?: "",
                                pictureUrl = eventsState.openedFullEvent?.imgUrl ?: "",
                                phoneNumber = eventsState.openedFullRole?.phone ?: ""
                            ),
                            onBackClicked = {
                                eventsViewModel.onAction(
                                    EventsViewModel.Action.CloseFullRole
                                )
                            },
                            isButtonVisible = true,
                            isButtonEnabled = true,
                            buttonText = "Записаться",
                            onButtonClicked = {
                                if (
                                    eventsState.openedFullEvent?.name?.contains(
                                        "реконструкция",
                                        ignoreCase = true
                                    ) == true &&
                                    eventsState.openedFullRole?.roleName?.contains(
                                        "участник",
                                        ignoreCase = true
                                    ) == true
                                ) {
                                    eventsViewModel.onAction(
                                        EventsViewModel.Action.ShowReconstructorPage(true)
                                    )
                                } else {
                                    showConfirmationDialog = true
                                }
                            }
                        )

                        AnimatedVisibility(visible = showConfirmationDialog) {
                            ConfirmationDialog(
                                question = "Подтвердить запись?",
                                onDismiss = {
                                    showConfirmationDialog = false
                                },
                                onConfirm = {
                                    eventsViewModel.onAction(
                                        EventsViewModel.Action.Enroll(
                                            eventsState.openingFullEventInfo!!,
                                            eventsState.openedFullRole!!.id
                                        )
                                    )
                                    showConfirmationDialog = false
                                    eventsViewModel.onAction(
                                        EventsViewModel.Action.CloseFullRole
                                    )
                                }
                            )
                        }
                    }
                } else if (eventsState.openedFullEvent != null && eventsState.openedFullRole != null) {
                    ConfirmationDialog(
                        question = "Подтвердить запись?",
                        onDismiss = {
                            eventsViewModel.onAction(EventsViewModel.Action.CloseFullRole)
                        },
                        onConfirm = {
                            eventsViewModel.onAction(
                                EventsViewModel.Action.Enroll(
                                    eventsState.openingFullEventInfo!!,
                                    eventsState.openedFullRole!!.id
                                )
                            )
                        }
                    )
                }
            }

            AnimatedVisibility(
                modifier = Modifier.constrainAs(recRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
                visible = eventsState.reconstructorPageIsOpen,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ReconstructorPageContent(
                    modifier = Modifier.fillMaxSize(),
                    sendData = {
                        eventsViewModel.onAction(
                            EventsViewModel.Action.SendReconstructorForm(it)
                        )

                        eventsViewModel.onAction(
                            EventsViewModel.Action.ShowReconstructorPage(false)
                        )

                        eventsViewModel.onAction(
                            EventsViewModel.Action.CloseFullRole
                        )
                    },
                    backClicked = {
                        eventsViewModel.onAction(
                            EventsViewModel.Action.ShowReconstructorPage(false)
                        )
                    }
                )
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .constrainAs(snackbarRef) {
                        bottom.linkTo(if (!eventsState.isEventScreenOpen) toolbarRef.top else parent.bottom)
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
                                    snackbarHostState.currentSnackbarData?.dismiss()
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
