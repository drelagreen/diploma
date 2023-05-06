package ru.sfedu.zhalnin.oborona

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import ru.sfedu.zhalnin.oborona.ui.Host
import ru.sfedu.zhalnin.oborona.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<ComposeView>(R.id.composeView).apply {
            setContent {
                AppTheme {
                    Surface(
                        color = AppTheme.colors.transparent
                    ) {
                        Host()
                    }
                }
            }
        }
    }
}
