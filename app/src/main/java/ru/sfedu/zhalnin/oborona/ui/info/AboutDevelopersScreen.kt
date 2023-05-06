package ru.sfedu.zhalnin.oborona.ui.info

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme
import ru.sfedu.zhalnin.oborona.ui.top_toolbar.CommonToolbar
import ru.sfedu.zhalnin.oborona.ui.top_toolbar.TopToolbarViewModel

private const val SfeduUrl = "http://sfedu.ru"

private const val IctisUrl = "http://ictis.sfedu.ru"

@Composable
fun AboutDevelopersScreen(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
) {
    val context = LocalContext.current

    BackHandler(true) {
        onBackClicked()
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()

    ) {
        val (toolbarRef, contentRef) = createRefs()

        CommonToolbar(
            modifier = Modifier.constrainAs(toolbarRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            state = TopToolbarViewModel.State(
                mode = TopToolbarViewModel.Mode.COMMON,
                tittle = stringResource(R.string.aboutDevs)
            ),
            onAction = { onBackClicked() }
        )

        Column(
            modifier = Modifier
                .background(
                    AppTheme.colors.background,
                    shape = AppTheme.shapes.backgroundShape
                )
                .constrainAs(contentRef) {
                    top.linkTo(parent.top, 48.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .padding(horizontal = 16.dp)
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                style = AppTheme.typography.text3,
                color = AppTheme.colors.onBackground,
                text = stringResource(R.string.aboutDevsText),
                lineHeight = 24.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true)
                    ) {
                        Intent(Intent.ACTION_VIEW, Uri.parse(SfeduUrl)).apply {
                            context.startActivity(this)
                        }
                    },
                    painter = painterResource(id = R.drawable.ic_logo_sfedu),
                    contentDescription = "sfedu logo"
                )
                Image(
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true)
                    ) {
                        Intent(Intent.ACTION_VIEW, Uri.parse(IctisUrl)).apply {
                            context.startActivity(this)
                        }
                    },
                    painter = painterResource(id = R.drawable.ic_logo_ictis),
                    contentDescription = "sfedu logo"
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xFF00FF00
)
@Composable
fun AboutDeveloperScreenPreview() {
    AppTheme {
        AboutDevelopersScreen(
            onBackClicked = {}
        )
    }
}