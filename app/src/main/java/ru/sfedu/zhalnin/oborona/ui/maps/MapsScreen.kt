package ru.sfedu.zhalnin.oborona.ui.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.data.model.dto.MapPointEvent
import ru.sfedu.zhalnin.oborona.data.model.dto.MapPointHoreca
import ru.sfedu.zhalnin.oborona.data.utils.monthsPeriod
import ru.sfedu.zhalnin.oborona.data.utils.timePeriod
import ru.sfedu.zhalnin.oborona.ui.common.OpenWithMapsBadge
import ru.sfedu.zhalnin.oborona.ui.common.PhoneBadge
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapsScreen(
    modifier: Modifier = Modifier,
    state: MapsViewModel.State = MapsViewModel.State.EMPTY,
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onEventClick: (MapPointEvent) -> Unit = { },
    onHorecaClick: (MapPointHoreca) -> Unit = { },
    onEventsVisibilityChecked: (Boolean) -> Unit = { },
    onHorecassVisibilityChecked: (Boolean) -> Unit = { },
    onDismiss: () -> Unit = { },
    mapLoadedCallback: () -> Unit
) {
    LaunchedEffect(state.openedEvent) {
        if (state.openedEvent != null) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        state.openedEvent.latitude,
                        state.openedEvent.longitude
                    ), if (cameraPositionState.position.zoom < 15f)
                        15f
                    else
                        cameraPositionState.position.zoom
                ),
                400
            )
            sheetState.show()
        }

        if (state.openedEvent == null && state.openedHoreca == null) {
            sheetState.hide()
        }
    }

    LaunchedEffect(state.openedHoreca) {
        if (state.openedHoreca != null) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        state.openedHoreca.latitude,
                        state.openedHoreca.longitude
                    ),
                    if (cameraPositionState.position.zoom < 15f)
                        15f
                    else
                        cameraPositionState.position.zoom
                ),
                400
            )
            sheetState.show()
        }

        if (state.openedEvent == null && state.openedHoreca == null) {
            sheetState.hide()
        }
    }

    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = sheetState,
        sheetBackgroundColor = AppTheme.colors.transparent,
        sheetContent = {
            SheetContent(
                state = state,
                onDismiss = {
                    onDismiss()
                }
            )
        },
        content = {
            MapContent(
                state = state,
                cameraPositionState = cameraPositionState,
                onEventClick = onEventClick,
                onHorecaClick = onHorecaClick,
                onEventsVisibilityChecked = onEventsVisibilityChecked,
                onHorecassVisibilityChecked = onHorecassVisibilityChecked,
                mapLoadedCallback = mapLoadedCallback
            )
        }
    )
}

@Composable
private fun SheetContent(
    modifier: Modifier = Modifier,
    state: MapsViewModel.State = MapsViewModel.State.EMPTY,
    onDismiss: () -> Unit = { }
) {
    Column(
        modifier = modifier
            .background(
                AppTheme.colors.background, AppTheme.shapes.backgroundShape
            )
            .padding(16.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth(),
        ) {
            val (icon1Ref, textRef, icon2Ref) = createRefs()

            Icon(
                modifier = Modifier.constrainAs(
                    icon1Ref
                ) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
                imageVector = if (state.openedEvent != null) {
                    AppTheme.icons.BlueMapPoint
                } else {
                    AppTheme.icons.RedMapPoint
                },
                tint = if (state.openedEvent != null) {
                    AppTheme.colors.primary
                } else {
                    AppTheme.colors.secondary
                },
                contentDescription = ""
            )
            Text(
                modifier = Modifier.constrainAs(
                    textRef
                ) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(icon1Ref.end, 13.dp)
                    end.linkTo(icon2Ref.start, 13.dp)
                    width = Dimension.fillToConstraints
                },
                style = AppTheme.typography.header2,
                text = state.openedEvent?.name ?: state.openedHoreca?.name ?: "",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            IconButton(
                modifier = Modifier.constrainAs(
                    icon2Ref
                ) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
                onClick = { onDismiss() }
            ) {
                Icon(imageVector = AppTheme.icons.ArrowDown, contentDescription = "")
            }
        }

        if (state.openedEvent != null) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                style = AppTheme.typography.button1,
                text = stringResource(R.string.msTimeEvent)
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                style = AppTheme.typography.text2,
                text = monthsPeriod(state.openedEvent.timeStart, state.openedEvent.timeEnd)
            )
            Text(
                modifier = Modifier.padding(bottom = 24.dp),
                style = AppTheme.typography.text2,
                text = timePeriod(state.openedEvent.timeStart, state.openedEvent.timeEnd)
            )

            OpenWithMapsBadge(
                latLng = LatLng(
                    state.openedEvent.latitude,
                    state.openedEvent.longitude
                )
            )
        } else if (state.openedHoreca != null) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                style = AppTheme.typography.button1,
                text = stringResource(R.string.msVIPBonuses)
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                style = AppTheme.typography.text3,
                text = state.openedHoreca.description.orEmpty(),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(top = 24.dp),
                style = AppTheme.typography.button1,
                text = stringResource(R.string.msContacts)
            )

            PhoneBadge(
                modifier = Modifier.padding(top = 24.dp),
                value = state.openedHoreca.phone
            )
        }
    }
}

@Composable
private fun FlagsContent(
    modifier: Modifier = Modifier,
    eventsChecked: Boolean = true,
    horecasChecked: Boolean = false,
    onEventsCheckedChange: (Boolean) -> Unit = { },
    onHorecasCheckedChange: (Boolean) -> Unit = { }
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                .fillMaxWidth()
                .background(
                    Color(0xB3FFFFFF),
                    shape = AppTheme.shapes.small
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = eventsChecked,
                onCheckedChange = onEventsCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = AppTheme.colors.primary,
                    uncheckedColor = AppTheme.colors.primary
                )
            )

            Icon(
                modifier = Modifier.padding(start = 8.dp),
                imageVector = AppTheme.icons.BlueMapPoint,
                tint = AppTheme.colors.primary,
                contentDescription = ""
            )

            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .padding(vertical = 10.5.dp),
                text = stringResource(R.string.msPin1),
                style = AppTheme.typography.text2
            )
        }

        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                .fillMaxWidth()
                .background(
                    Color(0xB3FFFFFF),
                    shape = AppTheme.shapes.small
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = horecasChecked,
                onCheckedChange = onHorecasCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = AppTheme.colors.secondary,
                    uncheckedColor = AppTheme.colors.secondary
                )
            )

            Icon(
                modifier = Modifier.padding(start = 8.dp),
                imageVector = AppTheme.icons.RedMapPoint,
                tint = AppTheme.colors.secondary,
                contentDescription = ""
            )

            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .padding(vertical = 10.5.dp),
                text = stringResource(R.string.msPin2),
                style = AppTheme.typography.text2
            )
        }
    }
}

@Preview
@Composable
fun FlagsContentPreview() {
    AppTheme {
        FlagsContent()
    }
}

@Composable
private fun MapContent(
    state: MapsViewModel.State,
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    onEventClick: (MapPointEvent) -> Unit,
    onHorecaClick: (MapPointHoreca) -> Unit,
    onEventsVisibilityChecked: (Boolean) -> Unit,
    onHorecassVisibilityChecked: (Boolean) -> Unit,
    mapLoadedCallback: () -> Unit
) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (mapRef, flagsRef) = createRefs()

        Card(
            modifier = Modifier
                .constrainAs(mapRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom, (-48).dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                }
                .padding(top = 48.dp),
            shape = AppTheme.shapes.backgroundShape,
        ) {
            GoogleMap(
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    compassEnabled = false,
                    myLocationButtonEnabled = false,
                    scrollGesturesEnabled = true,
                    zoomGesturesEnabled = true,
                    tiltGesturesEnabled = false,
                    rotationGesturesEnabled = false,
                ),
                properties = MapProperties(
                    minZoomPreference = MapDefaults.MIN_ZOOM,
                    maxZoomPreference = MapDefaults.MAX_ZOOM,
                ),
                onMapLoaded = {
                    mapLoadedCallback()
                }
            ) {
                if (state.eventsChecked) {
                    for (event in state.events) {
                        Marker(
                            state = rememberMarkerState(
                                position = LatLng(
                                    event.latitude,
                                    event.longitude
                                )
                            ),
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.png_position_blue),
                            onClick = {
                                onEventClick(event)
                                false
                            },
                            title = event.name
                        )
                    }
                }

                if (state.horecasChecked) {
                    for (horeca in state.horecas) {
                        Marker(
                            state = rememberMarkerState(
                                position = LatLng(
                                    horeca.latitude,
                                    horeca.longitude
                                )
                            ),
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.png_position_red),
                            onClick = {
                                onHorecaClick(horeca)
                                false
                            },
                            title = horeca.name
                        )
                    }
                }
            }
        }

        FlagsContent(
            modifier = Modifier.constrainAs(flagsRef) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            eventsChecked = state.eventsChecked,
            horecasChecked = state.horecasChecked,
            onEventsCheckedChange = onEventsVisibilityChecked,
            onHorecasCheckedChange = onHorecassVisibilityChecked
        )

    }
}

object MapDefaults {
    val DEFAULT_LOCATION = LatLng(47.2117242, 38.9326314)
    const val DEFAULT_ZOOM = 14.0f
    const val MIN_ZOOM = 12.5f
    const val MAX_ZOOM = 18f
}


@Preview
@Composable
fun SheetContentPreview() {
    AppTheme {
        SheetContent(
            state = MapsViewModel.State.EMPTY.copy(
                openedEvent = MapPointEvent(
                    id = 1,
                    name = LoremIpsum(16).values.first(),
                    address = LoremIpsum(16).values.first(),
                    latitude = 55.753215,
                    longitude = 37.622504,
                    timeStart = Date(),
                    timeEnd = null
                )
            )
        )
    }
}

@Preview
@Composable
fun SheetContentPreview2() {
    AppTheme {
        SheetContent(
            state = MapsViewModel.State.EMPTY.copy(
                openedHoreca = MapPointHoreca(
                    id = 1,
                    name = LoremIpsum(16).values.first(),
                    description = LoremIpsum(16).values.first(),
                    latitude = 55.753215,
                    longitude = 37.622504,
                    phone = "+78005553535",
                )
            )
        )
    }
}