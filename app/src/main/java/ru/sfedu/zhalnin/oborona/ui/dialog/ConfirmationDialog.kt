package ru.sfedu.zhalnin.oborona.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.ui.common.OutlinedButton
import ru.sfedu.zhalnin.oborona.ui.common.PrimaryButton
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun ConfirmationDialog(
    question: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = AppTheme.colors.background,
            elevation = 8.dp,
            shape = AppTheme.shapes.medium,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = question,
                    textAlign = TextAlign.Center,
                    color = AppTheme.colors.onBackground,
                    style = AppTheme.typography.text2,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PrimaryButton(
                        modifier = Modifier
                            .weight(1f),
                        onClick = { onConfirm() },
                        text = stringResource(R.string.cdYes),
                    )
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                            .fillMaxWidth(),
                        onClick = { onDismiss() },
                        text = stringResource(R.string.cdCancel),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ConfirmationDialogPreview() {
    AppTheme {
        ConfirmationDialog(
            question = "Вы уверены?",
            onDismiss = {},
            onConfirm = {}
        )
    }
}
