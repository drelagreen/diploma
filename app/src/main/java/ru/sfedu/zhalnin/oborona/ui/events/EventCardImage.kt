package ru.sfedu.zhalnin.oborona.ui.events

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import ru.sfedu.zhalnin.oborona.ui.BeautifulLoading
import ru.sfedu.zhalnin.oborona.ui.common.FailurePlaceholderBox
import ru.sfedu.zhalnin.oborona.ui.common.LoadingPlaceholderBox
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EventCardImage(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    Column(modifier = modifier) {
        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(234.dp),
            model = imageUrl,
            contentScale = ContentScale.FillBounds,
            contentDescription = "",
            loading = placeholder {
                LoadingPlaceholderBox()
            },
            failure = placeholder {
                FailurePlaceholderBox()
            }
        )
    }
}

@Preview
@Composable
fun EventCardImagePreview() {
    AppTheme {
        BeautifulLoading(
            surfaceHeight = { LocalConfiguration.current.screenHeightDp.dp - 220.dp },
            loading = false,
            backContent = { EventCardImage(imageUrl = "https://random.imagecdn.app/360/228") }) {}
    }
}