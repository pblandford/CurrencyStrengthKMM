package com.philblandford.currencystrength.features.alerthistory.usecase

import androidx.compose.runtime.State
import com.philblandford.currencystrength.common.log.log
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.network.NetworkClient
import com.philblandford.currencystrength.common.notifications.NotificationManager
import com.philblandford.currencystrength.common.util.flatMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GetAlertHistory(
    private val notificationManager: NotificationManager,
    private val networkClient: NetworkClient) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val alertFlow = MutableStateFlow<List<Alert>>(listOf())

    init {
        coroutineScope.launch {
            notificationManager.haveTokenFlow.collectLatest {
                log("Have token $it")
                if (it) {
                    refresh()
                    notificationManager.notificationFlow.collectLatest {
                        refresh()
                    }
                }
            }
        }
    }

    private suspend fun refresh() {
        notificationManager.getToken().onSuccess { token ->
            networkClient.getJson<List<Alert>>("alerts/log/$token").onSuccess {
                log("Got alerts")
                alertFlow.emit(it.sortedByDescending { it.lastAlert })
            }
        }
    }

    operator fun invoke():StateFlow<List<Alert>> = alertFlow
}