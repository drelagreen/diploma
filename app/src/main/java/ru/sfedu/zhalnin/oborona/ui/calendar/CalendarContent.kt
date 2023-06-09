package ru.sfedu.zhalnin.oborona.ui.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.data.model.dto.Entry
import ru.sfedu.zhalnin.oborona.data.utils.monthsPeriod
import ru.sfedu.zhalnin.oborona.data.utils.removeTime
import ru.sfedu.zhalnin.oborona.ui.common.PrimaryButton
import ru.sfedu.zhalnin.oborona.ui.dialog.ConfirmationDialog
import ru.sfedu.zhalnin.oborona.ui.events.EventsViewModel
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun CalendarContent(
    modifier: Modifier = Modifier,
    eventsState: EventsViewModel.EventsState,
    onEventsListButtonClicked: () -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
    unsubscribe: (Entry) -> Unit
) {
    var confirmDialogVisible by remember { mutableStateOf(false) }
    var entryForUnsubscribe: Entry? by remember {
        mutableStateOf(null)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (eventsState.userEntries.isEmpty()) {
            Text(
                modifier = Modifier.padding(64.dp),
                textAlign = TextAlign.Center,
                style = AppTheme.typography.text1,
                text = stringResource(R.string.ссYourEvents),
                color = AppTheme.colors.onBackgroundVariant
            )

            Spacer(modifier = Modifier.weight(1f))
        }
        if (eventsState.user != null) {
            if (eventsState.userEntries.isEmpty()) {
                Text(
                    modifier = Modifier.padding(32.dp),
                    textAlign = TextAlign.Center,
                    style = AppTheme.typography.text2,
                    text = stringResource(R.string.ccNoEvents)
                )
                PrimaryButton(
                    modifier = Modifier.padding(32.dp),
                    onClick = onEventsListButtonClicked,
                    text = stringResource(R.string.ccEventsList)
                )
                Spacer(modifier = Modifier.weight(1f))
            } else {
                val events = eventsState.userEntries.groupBy { it.event.timeStart.removeTime() }

                LazyColumn(
                    modifier = Modifier,
                    state = lazyListState
                ) {
                    items(events.keys.toList().reversed()) { date ->
                        events[date]?.let {

                            CalendarDate(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                date = monthsPeriod(date1 = date, date2 = null)
                            )
                            it.forEach { entry ->
                                CalendarCard(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 8.dp
                                    ),
                                    timeStart = entry.event.timeStart,
                                    timeEnd = entry.event.timeEnd,
                                    tittle = entry.event.name,
                                    role = entry.entryRole.name,
                                    onUnsubscribeButtonClicked = {
                                        entryForUnsubscribe = entry
                                        confirmDialogVisible = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Text(
                modifier = Modifier
                    .padding(64.dp),
                textAlign = TextAlign.Center,
                style = AppTheme.typography.text2,
                text = stringResource(R.string.ccLogin)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }

    AnimatedVisibility(
        visible = confirmDialogVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        ConfirmationDialog(
            question = stringResource(R.string.unsubscribeQuestion),
            onDismiss = {
                confirmDialogVisible = false
            },
            onConfirm = {
                entryForUnsubscribe?.let {
                    unsubscribe(it)
                }
                confirmDialogVisible = false
            }
        )
    }

}

@Preview
@Composable
fun CalendarContentPreview() {
    AppTheme {
        CalendarContent(
            eventsState = EventsViewModel.EventsState(
                userEntries = listOf()
            ),
            onEventsListButtonClicked = {},
            unsubscribe = {}
        )
    }
}