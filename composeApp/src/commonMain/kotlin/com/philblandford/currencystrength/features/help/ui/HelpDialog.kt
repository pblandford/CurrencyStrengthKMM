package com.philblandford.currencystrength.features.help.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import currencystrengthcmm.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getResourceUri

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HelpDialog(show: Boolean, onDismiss: () -> Unit) {
    if (show) {
        Dialog(onDismiss) {

            val uri = Res.getUri("files/help.html")
            val state = rememberWebViewState(uri)
            val navigator = rememberWebViewNavigator()

            WebView(
                state,
                Modifier.fillMaxSize().background(Color.White).padding(20.dp),
                navigator = navigator
            )
        }
    }
}