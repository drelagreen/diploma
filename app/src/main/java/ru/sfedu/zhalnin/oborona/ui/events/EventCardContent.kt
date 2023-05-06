package ru.sfedu.zhalnin.oborona.ui.events

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.data.model.dto.FullEvent
import ru.sfedu.zhalnin.oborona.data.model.dto.Role
import ru.sfedu.zhalnin.oborona.data.utils.monthsPeriod
import ru.sfedu.zhalnin.oborona.data.utils.timePeriod
import ru.sfedu.zhalnin.oborona.ui.common.PhoneBadge
import ru.sfedu.zhalnin.oborona.ui.common.PrimaryButton
import ru.sfedu.zhalnin.oborona.ui.maps.MapDefaults
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventCardContent(
    modifier: Modifier = Modifier,
    eventData: FullEvent,
    userLoaded: Boolean,
    openRoleDescription: (String) -> Unit,
    unsubscribe: Boolean = false
) {
    val scrollState = rememberScrollState()
    val cameraPositionState = rememberCameraPositionState()
    val context = LocalContext.current

    Box(
        modifier = modifier.verticalScroll(
            scrollState,
        )
    ) {
        Column(
            modifier = Modifier
                .padding(top = 220.dp)
                .background(
                    AppTheme.colors.background,
                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(vertical = 24.dp),
                text = eventData.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AppTheme.typography.header2,
                color = AppTheme.colors.onBackground
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = AppTheme.icons.Calendar,
                    tint = AppTheme.colors.primary,
                    contentDescription = null
                )
                Column(
                    modifier = Modifier.padding(start = 12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 2.dp),
                        text = monthsPeriod(
                            date1 = eventData.startDateTime,
                            date2 = eventData.endDateTime
                        ),
                        style = AppTheme.typography.text2,
                        color = AppTheme.colors.onBackground
                    )
                    Text(
                        text = timePeriod(
                            date1 = eventData.startDateTime,
                            date2 = eventData.endDateTime
                        ),
                        style = AppTheme.typography.text3Bold,
                        color = AppTheme.colors.onBackgroundVariant
                    )
                }
            }

            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = AppTheme.icons.Location,
                    tint = AppTheme.colors.primary,
                    contentDescription = null
                )
                Column(
                    modifier = Modifier.padding(start = 12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 2.dp),
                        text = eventData.adress,
                        style = AppTheme.typography.text2,
                        color = AppTheme.colors.onBackground
                    )
                }
            }

            Text(
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                text = stringResource(R.string.eccDescription),
                style = AppTheme.typography.text1,
                color = AppTheme.colors.onBackground
            )

            Text(
                style = AppTheme.typography.text2,
                color = AppTheme.colors.onBackground,
                text = eventData.about,
                lineHeight = 21.sp
            )

            Text(
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                text = stringResource(R.string.eccWhere),
                style = AppTheme.typography.text1,
                color = AppTheme.colors.onBackground
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                elevation = 0.dp,
                shape = AppTheme.shapes.medium,
            ) {
                GoogleMap(
                    modifier = Modifier.background(
                        shape = AppTheme.shapes.medium,
                        color = Color.Transparent
                    ),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false,
                        compassEnabled = false,
                        myLocationButtonEnabled = false,
                        scrollGesturesEnabled = false,
                        zoomGesturesEnabled = false,
                        tiltGesturesEnabled = false,
                        rotationGesturesEnabled = false,
                    ),
                    properties = MapProperties(
                        minZoomPreference = MapDefaults.MIN_ZOOM,
                        maxZoomPreference = MapDefaults.MAX_ZOOM,
                    ),
                    onMapLoaded = {
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    eventData.coordinateLatitude.toDouble(),
                                    eventData.coordinateLongitude.toDouble(),
                                ),
                                MapDefaults.DEFAULT_ZOOM
                            )
                        )
                    }
                ) {
                    Marker(
                        state = rememberMarkerState(
                            position = LatLng(
                                eventData.coordinateLatitude.toDouble(),
                                eventData.coordinateLongitude.toDouble()
                            )
                        ),
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.png_position_blue),
                        onClick = {
                            val uri =
                                "http://maps.google.com/maps?q=loc:${it.position.latitude},${it.position.longitude}"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                            ContextCompat.startActivity(context, intent, null);
                            true
                        },
                        title = eventData.name
                    )
                }
            }

            Text(
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                text = stringResource(R.string.eccSelectRole),
                style = AppTheme.typography.text1,
                color = AppTheme.colors.onBackground
            )

            val options = eventData.roles.map { it.name }

            var expanded by remember { mutableStateOf(false) }
            var selectedOptionText by remember { mutableStateOf(options[0]) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    value = selectedOptionText,
                    onValueChange = { },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        backgroundColor = AppTheme.colors.transparent,
                        unfocusedIndicatorColor = AppTheme.colors.onBackgroundVariant,
                        focusedIndicatorColor = AppTheme.colors.onBackgroundVariant,
                        focusedTrailingIconColor = AppTheme.colors.onBackgroundVariant,
                        trailingIconColor = AppTheme.colors.onBackgroundVariant,

                        ),
                    textStyle = AppTheme.typography.text3,
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                            }
                        ) {
                            Text(text = selectionOption)
                        }
                    }
                }
            }
            eventData.phone?.let {
                Text(
                    modifier = Modifier.padding(top = 24.dp, bottom = 24.dp),
                    text = stringResource(R.string.eccContacts),
                    style = AppTheme.typography.text1,
                    color = AppTheme.colors.onBackground
                )

                PhoneBadge(value = eventData.phone)
            }

            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 54.dp, bottom = 24.dp),
                text = if (unsubscribe) stringResource(R.string.eccSubscribe) else stringResource(R.string.eccUnsubscribe),
                enabled = userLoaded,
                onClick = {
                    openRoleDescription(
                        eventData.roles.find {
                            it.name == selectedOptionText
                        }?.id.toString()
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun EventCardContentPreview() {
    AppTheme {
        EventCardContent(
            eventData = FullEvent(
                name = "Информация типичного события",
                about = """
                    Масштабная  реконструкция  различных  эпизодов  обороны  Таганрога  и  Приазовья  1855 года - это ядро праздника. На глазах десятков тысяч людей происходят: 
                    -подход  военных кораблей англичан и французов к Таганрогу, 
                    -высадка парламентеров и вражеского десанта,
                    -бомбардировка с моря, 
                    -сражения на берегу войск союзников с ополченцами и казаками, 
                    -подвиг мирных жителей (спасение ребенка Анисьей Лядовой), 
                    -уникальный захват  казаками по морю английской канонерки «Джаспер» и другие эпизоды. 

                """.trimIndent(),
                phone = "+79885392305",
                adress = "г. Таганрог, ул.Чехова 2а",
                startDateTime = Date(),
                endDateTime = null,
                roles = listOf(
                    Role(0, "Зритель"), Role(1, "Участник"), Role(2, "Волонтёр")
                ),
                coordinateLatitude = "",
                coordinateLongitude = "",
                isEpic = true,
                imgUrl = "https://img-host.ru/V2tWl.png"
            ),
            openRoleDescription = {},
            userLoaded = false,
        )
    }
}