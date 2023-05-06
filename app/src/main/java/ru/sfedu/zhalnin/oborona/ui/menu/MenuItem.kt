package ru.sfedu.zhalnin.oborona.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    icon: ImageVector = AppTheme.icons.Telephone,
    tittle: String = stringResource(R.string.festHelp),
    topLineVisible: Boolean = false,
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = true),
        ) {
            onClick()
        }) {
        if (topLineVisible) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(1.dp)
                    .background(AppTheme.colors.onBackgroundVariant)
            )
        }
        Row(
            modifier = Modifier.padding(vertical = 18.dp, horizontal = 26.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(20.dp),
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.colors.primary
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = tittle,
                color = AppTheme.colors.onBackground
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(20.dp),
                imageVector = AppTheme.icons.ArrowRight,
                contentDescription = null,
                tint = AppTheme.colors.primary
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(1.dp)
                .background(AppTheme.colors.onBackgroundVariant)
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Menu Item"
)
@Composable
fun MenuItemPreview() {
    AppTheme {
        MenuItem()
    }
}