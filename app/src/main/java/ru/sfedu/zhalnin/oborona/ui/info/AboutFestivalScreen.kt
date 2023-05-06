package ru.sfedu.zhalnin.oborona.ui.info

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme
import ru.sfedu.zhalnin.oborona.ui.top_toolbar.CommonToolbar
import ru.sfedu.zhalnin.oborona.ui.top_toolbar.TopToolbarViewModel

@Composable
fun AboutFestivalScreen(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
) {
    BackHandler(true) {
        onBackClicked()
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
                tittle = "О фестивале"
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
                    height = Dimension.fillToConstraints
                }
                .padding(horizontal = 16.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 24.dp),
                text = stringResource(id = R.string.festivalName),
                style = AppTheme.typography.header2,
                color = AppTheme.colors.onBackground
            )

            Text(
                modifier = Modifier,
                style = AppTheme.typography.text3,
                color = AppTheme.colors.onBackground,
                text = stringResource(R.string.afText1),
                lineHeight = 24.sp
            )

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                painter = painterResource(id = R.drawable.about_festival_1),
                contentScale = ContentScale.FillWidth,
                contentDescription = "illustration 1"
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = AppTheme.typography.text3,
                color = AppTheme.colors.onBackground,
                text = stringResource(R.string.afText2),
                lineHeight = 24.sp
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                style = AppTheme.typography.text3,
                color = AppTheme.colors.onBackground,
                text = stringResource(R.string.afText3),
                lineHeight = 24.sp
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                painter = painterResource(id = R.drawable.about_festival_2),
                contentScale = ContentScale.FillWidth,
                contentDescription = "illustration 2"
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = AppTheme.typography.text3,
                color = AppTheme.colors.onBackground,
                text = stringResource(R.string.afText4),
                lineHeight = 24.sp
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                style = AppTheme.typography.text3,
                color = AppTheme.colors.onBackground,
                text = stringResource(id = R.string.afText5),
                lineHeight = 24.sp
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                painter = painterResource(id = R.drawable.about_festival_3),
                contentScale = ContentScale.FillWidth,
                contentDescription = "illustration 2"
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = AppTheme.typography.text3,
                color = AppTheme.colors.onBackground,
                text = stringResource(R.string.afText6),
                lineHeight = 24.sp
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                style = AppTheme.typography.text3,
                color = AppTheme.colors.onBackground,
                text = stringResource(R.string.afText7),
                lineHeight = 24.sp
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                style = AppTheme.typography.text3,
                color = AppTheme.colors.onBackground,
                text = stringResource(R.string.afText8),
                lineHeight = 24.sp
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                style = AppTheme.typography.text3,
                color = AppTheme.colors.onBackground,
                text = stringResource(R.string.afText9),
                lineHeight = 24.sp
            )
            Text(
                modifier = Modifier.padding(top = 16.dp, bottom = 32.dp),
                style = AppTheme.typography.text3,
                color = AppTheme.colors.onBackground,
                text = stringResource(R.string.afText10),
                lineHeight = 24.sp
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xFF00FF00
)
@Composable
fun AboutFestivalScreenPreview() {
    AppTheme {
        AboutFestivalScreen(
            onBackClicked = {}
        )
    }
}