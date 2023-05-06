package ru.sfedu.zhalnin.oborona.ui.info

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.ui.common.MailBadge
import ru.sfedu.zhalnin.oborona.ui.common.PhoneBadge
import ru.sfedu.zhalnin.oborona.ui.common.VkBadge
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme
import ru.sfedu.zhalnin.oborona.ui.top_toolbar.CommonToolbar
import ru.sfedu.zhalnin.oborona.ui.top_toolbar.TopToolbarViewModel

@Composable
fun ContactsScreen(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
) {
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
                tittle = stringResource(R.string.contacts)
            ),
            onAction = { onBackClicked() }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                style = AppTheme.typography.text2,
                color = AppTheme.colors.onBackground,
                text = stringResource(id = R.string.contactsContactsAbout),
                lineHeight = 24.sp
            )
            PhoneBadge(
                modifier = Modifier.padding(top = 16.dp),
                value = stringResource(R.string.contactsPhone)
            )
            MailBadge(
                modifier = Modifier.padding(top = 16.dp),
                value = stringResource(R.string.contactsMail)
            )
            Text(
                modifier = Modifier.padding(top = 24.dp),
                style = AppTheme.typography.text2,
                color = AppTheme.colors.onBackground,
                text = stringResource(R.string.contactsAboutFest),
                lineHeight = 24.sp
            )
            VkBadge(
                modifier = Modifier.padding(top = 16.dp),
                url = stringResource(R.string.contactsUrl)
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
fun ContactsScreenPreview() {
    AppTheme {
        ContactsScreen(
            onBackClicked = {}
        )
    }
}