package ru.sfedu.zhalnin.oborona.ui.common

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun Toast(state: Any?, text: String, statement: Boolean) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state) {
        if (statement)
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}