package ru.sfedu.zhalnin.oborona.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun CalendarDate(
    modifier: Modifier = Modifier,
    date: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(end = 20.dp),
            text = date,
            style = AppTheme.typography.text1,
            maxLines = 1,
            color = AppTheme.colors.primary
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(color = AppTheme.colors.primary),
        )
    }
}

@Preview
@Composable
fun CalendarDatePreview() {
    AppTheme {
        CalendarDate(
            date = "16.05"
        )
    }
}