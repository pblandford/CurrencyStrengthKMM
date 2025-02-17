package com.philblandford.currencystrength.common.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.philblandford.currencystrength.common.error.ErrorHandler
import com.philblandford.currencystrength.common.theme.CSTheme
import com.philblandford.currencystrength.features.home.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    CSTheme {
        val viewModel: AppViewModel = koinViewModel()
        val errorHandler = koinInject<ErrorHandler>()

        var error by remember { mutableStateOf<Throwable?>(null) }

        LaunchedEffect(Unit) {
            viewModel.init()
            errorHandler.errorFlow.collect {
                error = it
            }
        }

        error?.let {
            BasicAlertDialog({error = null}) {
                Text(it.message ?: "Unknown error")
            }
        }

        HomeScreen()
    }
}
