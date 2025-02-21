package com.philblandford.currencystrength.features.help.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import currencystrengthcmm.composeapp.generated.resources.Res
import currencystrengthcmm.composeapp.generated.resources.common_ok
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getResourceUri
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HelpDialog(show: Boolean, onDismiss: () -> Unit) {
    if (show) {
        Dialog(onDismiss) {

            val uri = Res.getUri("files/help.html")
            val state = rememberWebViewState(uri)
            val navigator = rememberWebViewNavigator()


            Column(
                Modifier.fillMaxWidth().fillMaxHeight(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WebView(
                    state,
                    Modifier.background(Color.Black).padding(20.dp).fillMaxWidth().weight(1f),
                    navigator = navigator,

                )
                Spacer(Modifier.height(10.dp))
                Button({onDismiss()}) {
                    Text(stringResource(Res.string.common_ok))
                }
            }
        }
    }
}