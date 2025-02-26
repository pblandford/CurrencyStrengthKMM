package com.philblandford.currencystrength.common.alert

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.philblandford.currencystrength.common.audio.AudioPlayer
import com.philblandford.currencystrength.common.log.log
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.network.NetworkClient
import com.philblandford.currencystrength.common.notifications.NotificationManager
import com.philblandford.currencystrength.common.util.flatMap
import currencystrengthcmm.composeapp.generated.resources.Res
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import org.jetbrains.compose.resources.ExperimentalResourceApi

class AlertRepository(
    private val notificationManager: NotificationManager,
    private val networkClient: NetworkClient,
    private val audioPlayer: AudioPlayer

) : DefaultLifecycleObserver {
    val alertFlow = MutableSharedFlow<Alert>()
    val alertHistoryFlow = MutableStateFlow<List<Alert>>(listOf())
    private var listenJob: Job? = null
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

    @OptIn(ExperimentalResourceApi::class)
    suspend fun listenForAlerts(): Result<Unit> {
        return notificationManager.getToken().flatMap {
            try {
                listenJob = coroutineScope.launch {
                    networkClient.websocket(it) { alert ->
                        println("Got alert $alert")
                        runCatching {
                            audioPlayer.play("files/jingle")
                        }.onFailure {
                            println("Failed to play audio: $it")
                        }
                        alertFlow.emit(alert)
                        refreshAlertHistory()
                    }
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }



    override fun onResume(owner: LifecycleOwner) {
        listenJob?.cancel()
        coroutineScope.launch {
            listenForAlerts()
            refreshAlertHistory()
        }
    }


    override fun onPause(owner: LifecycleOwner) {
        listenJob?.cancel()
    }
}