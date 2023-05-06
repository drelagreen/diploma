package ru.sfedu.zhalnin.oborona.ui.common

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.maps.model.LatLng
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun OpenWithMapsBadge(
    modifier: Modifier = Modifier,
    latLng: LatLng
) {
    val context = LocalContext.current
    val uri = stringResource(id = R.string.mapsURL, latLng.latitude, latLng.longitude)

    Row(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = true),
        ) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(context, intent, null);
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = AppTheme.icons.Maps,
            tint = AppTheme.colors.primary,
            contentDescription = null
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = stringResource(R.string.badgeMaps),
                style = AppTheme.typography.text2,
                textDecoration = TextDecoration.Underline,
                color = AppTheme.colors.primary
            )
        }
    }
}

@Composable
fun PhoneBadge(
    modifier: Modifier = Modifier,
    value: String
) {
    val context = LocalContext.current

    Row(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = true),
        ) {
            context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$value")))
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = AppTheme.icons.Telephone,
            tint = AppTheme.colors.primary,
            contentDescription = null
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = value,
                style = AppTheme.typography.text2,
                textDecoration = TextDecoration.Underline,
                color = AppTheme.colors.primary
            )
        }
    }
}

@Composable
fun MailBadge(
    modifier: Modifier = Modifier,
    value: String
) {
    val context = LocalContext.current


    Row(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = true),
        ) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("mailto:$value")
                )
            )
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = AppTheme.icons.Mail,
            tint = AppTheme.colors.primary,
            contentDescription = null
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = value,
                style = AppTheme.typography.text2,
                textDecoration = TextDecoration.Underline,
                color = AppTheme.colors.primary
            )
        }
    }
}

@Composable
fun VkBadge(
    modifier: Modifier = Modifier,
    url: String
) {
    val context = LocalContext.current

    Row(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = true),
        ) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = AppTheme.icons.VK,
            contentDescription = null
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = stringResource(R.string.badgeVK),
                style = AppTheme.typography.text2,
                textDecoration = TextDecoration.Underline,
                color = AppTheme.colors.primary
            )
        }
    }
}
