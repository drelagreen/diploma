package ru.sfedu.zhalnin.oborona.ui.dialog

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.ui.common.OutlinedButton
import ru.sfedu.zhalnin.oborona.ui.common.PrimaryButton
import ru.sfedu.zhalnin.oborona.ui.info.InfoScreenViewModel
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun CustomSponsorDialog(
    state: InfoScreenViewModel.State.DialogState,
    sendCode: (String) -> Unit,
    onDismiss: () -> Unit,
    onValueChange: (String) -> Unit,
    errorColor: Color = AppTheme.colors.error,
    successColor: Color = AppTheme.colors.success,
) {
    val aMessageColor by updateTransition(targetState = state.messageType, label = "").animateColor(
        label = ""
    ) {
        when (it) {
            InfoScreenViewModel.State.DialogState.MessageType.ERROR -> errorColor
            InfoScreenViewModel.State.DialogState.MessageType.SUCCESS -> successColor
            InfoScreenViewModel.State.DialogState.MessageType.NONE -> AppTheme.colors.onBackgroundVariant
        }
    }

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
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = AppTheme.colors.background,
                        textColor = AppTheme.colors.onBackground,
                        focusedBorderColor = AppTheme.colors.primary,
                        focusedLabelColor = AppTheme.colors.primary,
                        unfocusedBorderColor = AppTheme.colors.onBackgroundVariant,
                        unfocusedLabelColor = AppTheme.colors.onBackgroundVariant,
                        errorBorderColor = aMessageColor,
                        errorCursorColor = aMessageColor,
                        errorLabelColor = aMessageColor,
                        disabledBorderColor = aMessageColor,
                        disabledLabelColor = aMessageColor,
                    ),
                    textStyle = AppTheme.typography.text3,
                    value = state.dialogValue,
                    onValueChange = onValueChange,
                    label = {
                        Text(
                            style = AppTheme.typography.text3,
                            text = stringResource(R.string.sdEnterPromo)
                        )
                    },
                    singleLine = true,
                    isError = state.dialogMessage != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    enabled = state.messageType != InfoScreenViewModel.State.DialogState.MessageType.SUCCESS,
                )

                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = state.dialogMessage?.let { stringResource(id = state.dialogMessage) }
                        .orEmpty(),
                    color = aMessageColor,
                    style = AppTheme.typography.text3,
                )


                Column {
                    PrimaryButton(
                        modifier = Modifier
                            .padding(top = 8.dp),
                        onClick = { sendCode(state.dialogValue) },
                        text = stringResource(R.string.sdSend),
                        enabled = state.messageType != InfoScreenViewModel.State.DialogState.MessageType.SUCCESS,
                    )
                    OutlinedButton(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        onClick = { onDismiss() },
                        text = stringResource(R.string.sdCancel),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CustomSponsorDialogPreview() {
    var okValue by remember { mutableStateOf("1234") }
    var value by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }

    AppTheme {
        CustomSponsorDialog(
            state = InfoScreenViewModel.State.DialogState(
                dialogValue = value,
                messageType = if (isError) {
                    InfoScreenViewModel.State.DialogState.MessageType.ERROR
                } else {
                    if (isSuccess) {
                        InfoScreenViewModel.State.DialogState.MessageType.SUCCESS
                    } else {
                        InfoScreenViewModel.State.DialogState.MessageType.NONE
                    }
                },
                dialogMessage = null,
            ),
            sendCode = {
                if (it == okValue) {
                    value = ""
                    isError = false
                    isSuccess = true
                } else {
                    isError = true
                    isSuccess = false
                }
            },
            onDismiss = {},
            onValueChange = {
                value = it
            },
        )
    }
}