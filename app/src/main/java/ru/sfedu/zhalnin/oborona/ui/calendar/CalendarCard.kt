package ru.sfedu.zhalnin.oborona.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sfedu.zhalnin.oborona.data.utils.timePeriod
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme
import java.util.*

@Composable
fun CalendarCard(
    modifier: Modifier = Modifier,
    timeStart: Date,
    timeEnd: Date?,
    tittle: String,
    role: String,
    onUnsubscribeButtonClicked: () -> Unit
) {
    Row(modifier = modifier) {
        Text(
            modifier = Modifier.width(width =72.dp).padding(end = 20.dp),
            text = timePeriod(date1 = timeStart, date2 = timeEnd, newLine = true),
            style = AppTheme.typography.text2
        )

        Column(
            modifier = Modifier.weight(1f).background(
                color = AppTheme.colors.card,
                shape = AppTheme.shapes.medium
            ),
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp)){
                Text(
                    modifier = Modifier.weight(1f).padding(end = 16.dp),
                    text = tittle,
                    style = AppTheme.typography.text2
                )

                Icon(
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onUnsubscribeButtonClicked()
                    },
                    imageVector = AppTheme.icons.Unsubscribe,
                    tint = AppTheme.colors.onBackgroundVariant,
                    contentDescription = null
                )
            }

            Text(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                text = role,
                color = AppTheme.colors.onBackgroundVariant,
                style = AppTheme.typography.text2
            )
        }
    }
}

@Preview
@Composable
fun CalendarCardPreview() {
    AppTheme {
        CalendarCard(
            modifier = Modifier.fillMaxWidth(),
            tittle = "Историческая реконструкция dasd a ads asd ads ",
            role = "Зритель",
            timeEnd = Date(),
            timeStart = Date(),
            onUnsubscribeButtonClicked = {}
        )
    }
}