package com.philblandford

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.philblandford.currencystrength.common.app.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CurrencyStrengthCMM",
    ) {
        App()
    }
}