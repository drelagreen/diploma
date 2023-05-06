package ru.sfedu.zhalnin.oborona.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ru.sfedu.zhalnin.oborona.domain.EventInfo
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun SpecialList(
    modifier: Modifier = Modifier,
    items: List<EventInfo> = emptyList(),
    onClick: (EventInfo) -> Unit = {}
) {
    LazyRow(modifier = modifier.padding(8.dp)) {
        items(items) {
            SpecialCard(
                modifier.padding(8.dp),
                contentUrl = it.imgUrl,
                tittle = it.tittle,
                onClick = { onClick(it) }
            )
        }
    }
}

@Preview
@Composable
fun SpecialListPreview() {
    AppTheme {
        SpecialList(
            items = listOf(
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
        )
    }
}