package ru.sfedu.zhalnin.oborona.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import ru.sfedu.zhalnin.oborona.ui.common.FailurePlaceholderBox
import ru.sfedu.zhalnin.oborona.ui.common.LoadingPlaceholderBox
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CommonCard(
    modifier: Modifier = Modifier,
    contentUrl: String,
    tittle: String,
    description: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
            ) {
                onClick()
            }
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .clipToBounds()
    ) {
        Card(
            modifier = Modifier.size(100.dp),
            shape = AppTheme.shapes.medium,
            elevation = 0.dp
        ) {
            GlideImage(
                modifier = Modifier.size(100.dp),
                model = contentUrl,
                contentScale = ContentScale.FillHeight,
                contentDescription = null,
                loading = placeholder {
                    LoadingPlaceholderBox()
                },
                failure = placeholder{
                    FailurePlaceholderBox()
                }
            )
        }

        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                style = AppTheme.typography.text2,
                color = AppTheme.colors.onBackground,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 19.sp,
                maxLines = 2,
                text = tittle
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                style = AppTheme.typography.text3,
                color = AppTheme.colors.onBackgroundVariant,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 14.sp,
                maxLines = 3,
                text = description
            )
        }
    }
}

@Preview
@Composable
fun CommonCardPreview() {
    AppTheme {
        CommonCard(
            contentUrl = "https://random.imagecdn.app/100/100",
            tittle = "Квест-экскурсия «Крымская война: места и герои»",
            description = "Квест проходит 12.05.2019 в рамках подготовки к IV Международному фестивалю \"Оборона Таганрога..."
        )
    }
}