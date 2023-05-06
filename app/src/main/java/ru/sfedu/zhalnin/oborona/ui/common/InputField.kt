package ru.sfedu.zhalnin.oborona.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: String = "",
    label: String = "",
    error: String? = null,
    isPassword: Boolean = false,
    onChange: (String) -> Unit = {},
    keyboardType: KeyboardType
) {
    var isPasswordVisible by remember { mutableStateOf(!isPassword) }

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = AppTheme.colors.background,
                textColor = AppTheme.colors.onBackground,
                focusedBorderColor = AppTheme.colors.primary,
                focusedLabelColor = AppTheme.colors.primary,
                unfocusedBorderColor = AppTheme.colors.onBackgroundVariant,
                unfocusedLabelColor = AppTheme.colors.onBackgroundVariant,
                errorBorderColor = AppTheme.colors.error,
                errorCursorColor = AppTheme.colors.error,
                errorLabelColor = AppTheme.colors.error,
                errorTrailingIconColor = AppTheme.colors.error
            ),
            textStyle = AppTheme.typography.text3,
            value = value,
            onValueChange = onChange,
            label = {
                Text(
                    style = AppTheme.typography.text3,
                    text = label
                )
            },
            singleLine = true,
            isError = error != null,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = if (isPassword) {
                KeyboardOptions(keyboardType = KeyboardType.Password)
            } else {
                KeyboardOptions(keyboardType = keyboardType)
            },
            trailingIcon = {
                if (isPassword) {
                    val description = if (isPasswordVisible) "Hide password" else "Show password"

                    IconButton(
                        onClick = { isPasswordVisible = !isPasswordVisible }
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible) {
                                AppTheme.icons.PasswordToggleVisible
                            } else {
                                AppTheme.icons.PasswordToggleInvisible
                            }, description
                        )
                    }
                }
            }
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = error ?: "",
            color = AppTheme.colors.error,
            style = AppTheme.typography.text4,
        )

    }
}
