package ru.sfedu.zhalnin.oborona.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ru.sfedu.zhalnin.oborona.domain.EventInfo
import ru.sfedu.zhalnin.oborona.ui.events.EventsViewModel
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun EventList(
    modifier: Modifier = Modifier,
    state: EventsViewModel.EventsState,
    hideSpecial: Boolean = false,
    onEventClicked: (EventInfo) -> Unit = {},
) {
    LazyColumn(modifier.padding(
        top = 64.dp
    )) {
        item {
            AnimatedVisibility(
                visible = (state.specialItems.isNotEmpty() && !hideSpecial),
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                SpecialList(
                    modifier = Modifier.fillMaxWidth(),
                    items = state.specialItems,
                    onClick = onEventClicked
                )
            }
        }
        items(state.commonItems) {
            CommonCard(
                contentUrl = it.imgUrl,
                tittle = it.tittle,
                description = it.description.orEmpty(),
                onClick = { onEventClicked(it) }
            )
        }
    }
}


@Preview
@Composable
fun EventListPreview() {
    val special = listOf(
        EventInfo(
            type = EventInfo.Type.SPECIAL,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/140/192",
            id = "0",
            description = null
        ),
        EventInfo(
            type = EventInfo.Type.SPECIAL,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/140/192",
            id = "0",
            description = null
        ),
        EventInfo(
            type = EventInfo.Type.SPECIAL,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/140/192",
            id = "0",
            description = null
        ),
        EventInfo(
            type = EventInfo.Type.SPECIAL,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/140/192",
            id = "0",
            description = null
        ),
        EventInfo(
            type = EventInfo.Type.SPECIAL,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/140/192",
            id = "0",
            description = null
        ),
        EventInfo(
            type = EventInfo.Type.SPECIAL,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/140/192",
            id = "0",
            description = null
        ),
        EventInfo(
            type = EventInfo.Type.SPECIAL,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/140/192",
            id = "0",
            description = null
        ),
        EventInfo(
            type = EventInfo.Type.SPECIAL,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/140/192",
            id = "0",
            description = null
        ),
        EventInfo(
            type = EventInfo.Type.SPECIAL,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/140/192",
            id = "0",
            description = null
        ),
    )

    val common = listOf(
        EventInfo(
            type = EventInfo.Type.COMMON,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/100/100",
            id = "0",
            description = LoremIpsum(20).values.first()
        ),
        EventInfo(
            type = EventInfo.Type.COMMON,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/100/100",
            id = "0",
            description = LoremIpsum(20).values.first()
        ),
        EventInfo(
            type = EventInfo.Type.COMMON,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/140/192",
            id = "0",
            description = LoremIpsum(20).values.first()
        ),
        EventInfo(
            type = EventInfo.Type.COMMON,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/100/100",
            id = "0",
            description = LoremIpsum(20).values.first()
        ),
        EventInfo(
            type = EventInfo.Type.COMMON,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/140/192",
            id = "0",
            description = LoremIpsum(20).values.first()
        ),
        EventInfo(
            type = EventInfo.Type.COMMON,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/100/100",
            id = "0",
            description = null
        ),
        EventInfo(
            type = EventInfo.Type.COMMON,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/100/100",
            id = "0",
            description = LoremIpsum(20).values.first()
        ),
        EventInfo(
            type = EventInfo.Type.COMMON,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/100/100",
            id = "0",
            description = LoremIpsum(20).values.first()
        ),
        EventInfo(
            type = EventInfo.Type.COMMON,
            tittle = LoremIpsum(20).values.first(),
            imgUrl = "https://random.imagecdn.app/100/100",
            id = "0",
            description = null
        )
    )

    val specialVisible by remember { mutableStateOf(true) }

    AppTheme {
        EventList(
            state = EventsViewModel.EventsState(specialItems = special, commonItems = common),
            hideSpecial = !specialVisible
        )
    }
}