package ru.sfedu.zhalnin.oborona.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun FailurePlaceholderBox() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.onBackgroundVariant),
        contentAlignment = Alignment.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.phCantLoadImage)
        )
    }
}