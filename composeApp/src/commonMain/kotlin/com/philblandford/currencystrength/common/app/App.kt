package com.philblandford.currencystrength.common.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.philblandford.currencystrength.common.error.ErrorHandler
import com.philblandford.currencystrength.common.error.toUserFriendlyMessage
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.theme.CSTheme
import com.philblandford.currencystrength.features.home.ui.HomeScreen
import com.philblandford.currencystrength.features.home.ui.HomeScreenPortrait
import com.philblandford.currencystrength.features.home.ui.HomeScreenTablet
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(notificationAlert: Alert? = null) {
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
            BasicAlertDialog({
                error = null
                errorHandler.clearError()
            }) {
                Surface {
                    Column(Modifier.padding(10.dp)){
                        Text(it.toUserFriendlyMessage())
                        Spacer(Modifier.height(20.dp))
                        Button({
                            error = null
                            errorHandler.clearError()
                        }) {
                            Text("OK")
                        }
                    }
                }
            }
        }

        HomeScreen(notificationAlert)
    }
}
