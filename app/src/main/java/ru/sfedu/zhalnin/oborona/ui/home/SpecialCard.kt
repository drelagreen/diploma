package ru.sfedu.zhalnin.oborona.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import ru.sfedu.zhalnin.oborona.ui.common.FailurePlaceholderBox
import ru.sfedu.zhalnin.oborona.ui.common.LoadingPlaceholderBox
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SpecialCard(
    modifier: Modifier = Modifier,
    contentUrl: String,
    tittle: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .size(140.dp, 192.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
            ) {
                onClick()
            }
            .clipToBounds(),
        shape = AppTheme.shapes.medium
    ) {
        GlideImage(
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

        Box(contentAlignment = Alignment.BottomCenter) {
            Box(
                Modifier
                    .height(54.dp)
                    .fillMaxWidth()
                    .background(Color(0x80000000))
            )
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    style = AppTheme.typography.text2,
                    color = AppTheme.colors.onPrimary,
                    lineHeight = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    text = tittle
                )
            }
        }
    }
}

@Composable
@Preview
fun SpecialCardPreview() {
    AppTheme {
        SpecialCard(
            contentUrl = "https://random.imagecdn.app/140/192",
            tittle = "Test test test test test test"
        )
    }
}