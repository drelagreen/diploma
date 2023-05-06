package ru.sfedu.zhalnin.oborona.ui.top_toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun SearchViewTextField(
    modifier: Modifier = Modifier,
    text: String,
    onValueChanged: (String) -> Unit = {}
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = AppTheme.colors.background,
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
    ) {
        BasicTextField(
            cursorBrush = Brush.verticalGradient(
                0.00f to Color.Transparent,
                0.10f to Color.Transparent,
                0.10f to Color.White,
                0.90f to Color.White,
                0.90f to Color.Transparent,
                1.00f to Color.Transparent
            ),
            textStyle = AppTheme.typography.text2.copy(
                color = AppTheme.colors.background
            ),
            value = text,
            onValueChange = {
                onValueChanged(it)
            },
            modifier = Modifier
                .background(AppTheme.colors.transparent, RoundedCornerShape(8.dp))
                .height(32.dp)
                .fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Icon(
                        imageVector = AppTheme.icons.Search,
                        contentDescription = "image",
                        tint = AppTheme.colors.background
                    )
                    Box(
                        modifier = Modifier.weight(1f).padding(start = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (text.isBlank()) Text(
                            color = AppTheme.colors.background,
                            text = stringResource(R.string.search),
                            fontSize = AppTheme.typography.text2.fontSize
                        )
                        innerTextField()
                    }
                    if (text.isNotBlank()) {
                        IconButton(
                            onClick = {
                                onValueChanged("")
                            },
                        ) {
                            Icon(
                                imageVector = AppTheme.icons.Close,
                                contentDescription = "image",
                                tint = AppTheme.colors.background
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF444444)
fun SearchViewTextFieldPreview() {
    var text by remember { mutableStateOf("") }

    AppTheme {
        Surface(color = Color.Black) {
            SearchViewTextField(
                text = text
            ) { text = it }
        }
    }
}