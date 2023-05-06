package ru.sfedu.zhalnin.oborona.ui.info

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.data.model.dto.InfoWindow
import ru.sfedu.zhalnin.oborona.ui.common.PhoneBadge
import ru.sfedu.zhalnin.oborona.ui.common.PrimaryButton
import ru.sfedu.zhalnin.oborona.ui.events.EventCardImage
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme
import ru.sfedu.zhalnin.oborona.ui.top_toolbar.CommonToolbar
import ru.sfedu.zhalnin.oborona.ui.top_toolbar.TopToolbarViewModel

@Composable
fun InfoScreen(
    modifier: Modifier = Modifier,
    data: InfoWindow,
    isButtonVisible: Boolean = false,
    onBackClicked: () -> Unit,
    onButtonClicked: () -> Unit = {},
    isButtonEnabled: Boolean = true,
    buttonText: String = stringResource(R.string.infoScreenBtnText)
) {
    BackHandler(true) {
        onBackClicked()
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(
                state = rememberScrollState()
            )
    ) {
        val (toolbarRef, imgRef, contentRef) = createRefs()

        EventCardImage(
            modifier = Modifier.constrainAs(imgRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            imageUrl = data.pictureUrl
        )

        CommonToolbar(
            modifier = modifier.constrainAs(toolbarRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            state = TopToolbarViewModel.State(
                mode = TopToolbarViewModel.Mode.COMMON
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
                    top.linkTo(parent.top, 224.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(horizontal = 16.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 24.dp),
                text = data.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AppTheme.typography.header2,
                color = AppTheme.colors.onBackground
            )
            Text(
                style = AppTheme.typography.text2,
                color = AppTheme.colors.onBackground,
                text = data.description,
                lineHeight = 24.sp
            )
            data.phoneNumber?.let {
                PhoneBadge(
                    modifier = Modifier.padding(vertical = 24.dp),
                    value = data.phoneNumber
                )
            }

            if (isButtonVisible) {
                PrimaryButton(
                    modifier = modifier.padding(vertical = 16.dp),
                    enabled = isButtonEnabled,
                    text = buttonText,
                    onClick = { onButtonClicked() }
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFF0000
)
@Composable
fun InfoScreenPreview() {
    AppTheme {
        InfoScreen(
            data = InfoWindow(
                id = "5",
                name = "Спонсор",
                description = "(Примерная информация) Он дает деньги на проведение фестиваля, логотипы спонсоров будут на баннерах, на входе и на сцене, они могут поставить свою площадку на фестивале. Спонсоры также получают медийную поддержку, участвуют в пиар компании фестиваля, участвуют в эфирах на радио.\n\nДля получения подробной информация обратитесь по номеру:\n\nСпонсоры могут быть трех вариантов:\n1. Генеральный\nИнформация о том, кто это за спонсор....\n2. Средний\nИнформация о том, кто это за спонсор....\n3. Малый\nИнформация о том, кто это за спонсор....",
                pictureUrl = "https://i.ibb.co/8dLzc1k/image.png",
                phoneNumber = "+79997775544"
            ),
            onBackClicked = {}
        )
    }
}