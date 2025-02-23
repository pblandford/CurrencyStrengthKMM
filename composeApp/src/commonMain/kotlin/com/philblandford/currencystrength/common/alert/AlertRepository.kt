package com.philblandford.currencystrength.common.alert

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.philblandford.currencystrength.common.log.log
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.network.NetworkClient
import com.philblandford.currencystrength.common.notifications.NotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job

class AlertRepository(
    private val notificationManager: NotificationManager,
    private val networkClient: NetworkClient
) : DefaultLifecycleObserver {
    val alertFlow = MutableSharedFlow<Alert>()
    val alertHistoryFlow = MutableStateFlow<List<Alert>>(listOf())
    private var listenJob:Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun registerLifecycleObserver(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    suspend fun refreshAlertHistory() {
        notificationManager.getToken().onSuccess { token ->
            networkClient.getJson<List<Alert>>("alerts/log/$token").onSuccess {
                log("Got alerts")
                alertHistoryFlow.emit(it.sortedByDescending { it.lastAlert })
            }
        }
    }

    suspend fun listenForAlerts() {
        notificationManager.getToken().onSuccess {
            listenJob =  coroutineScope.launch {
                networkClient.websocket(it) { alert ->
                    println("Got alert $alert")
                    alertFlow.emit(alert)
                    refreshAlertHistory()
                }
            }
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        listenJob?.cancel()
        coroutineScope.launch {
            listenForAlerts()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        listenJob?.cancel()
    }
}