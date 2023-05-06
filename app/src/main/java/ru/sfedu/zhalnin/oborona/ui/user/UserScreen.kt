package ru.sfedu.zhalnin.oborona.ui.user

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.data.model.dto.User
import ru.sfedu.zhalnin.oborona.data.utils.getFullName
import ru.sfedu.zhalnin.oborona.data.utils.qrData
import ru.sfedu.zhalnin.oborona.ui.common.OutlinedButton
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    userInfo: User? = null,
    onLoginClicked: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        color = Color.Transparent
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, top = 64.dp)
                    .aspectRatio(1.0f),
                shape = AppTheme.shapes.medium,
                elevation = 4.dp
            ) {
                if (userInfo != null) {
                    QrCode(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxSize(),
                        data = userInfo.qrData(),
                        qrCodeProperties = QrCodeProperties(
                            foreground = AppTheme.colors.onBackground,
                            background = AppTheme.colors.background
                        )
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            style = AppTheme.typography.header2,
                            color = AppTheme.colors.onBackground,
                            text = stringResource(R.string.usYouNotLogin)
                        )
                        OutlinedButton(
                            modifier = Modifier
                                .padding(24.dp)
                                .wrapContentSize(),
                            text = stringResource(id = R.string.usAuth),
                            onClick = onLoginClicked
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if (userInfo != null) {
                Text(
                    style = AppTheme.typography.header2,
                    color = AppTheme.colors.onBackground,
                    text = userInfo.getFullName()
                )
                Text(
                    style = AppTheme.typography.text1,
                    color = AppTheme.colors.onBackground,
                    text = userInfo.email
                )
                Text(
                    style = AppTheme.typography.text1,
                    color = AppTheme.colors.onBackgroundVariant,
                    text = stringResource(R.string.usGender) + if (userInfo.sex == "m") stringResource(R.string.usMale) else stringResource(
                        R.string.usFemale
                    )
                )
            }


            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
@Preview
fun UserScreenPreview() {
    AppTheme {
        UserScreen()
    }
}