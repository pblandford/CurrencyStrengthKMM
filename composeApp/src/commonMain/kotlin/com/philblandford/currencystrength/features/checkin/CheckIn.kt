package com.philblandford.currencystrength.features.checkin

import com.philblandford.currencystrength.common.log.log
import com.philblandford.currencystrength.common.network.NetworkClient
import com.philblandford.currencystrength.common.notifications.NotificationManager
import com.philblandford.currencystrength.common.util.flatMap
import com.philblandford.currencystrength.features.alerthistory.usecase.GetAlertHistory
import com.philblandford.currencystrength.features.alerthistory.usecase.RefreshAlertHistory
import com.philblandford.currencystrength.features.listenforalert.usecase.ListenForAlert
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

expect suspend fun waitForToken()

class CheckIn(
    private val notificationManager: NotificationManager,
    private val networkClient: NetworkClient,
    private val listenForAlert: ListenForAlert,
    private val refreshAlertHistory: RefreshAlertHistory
) {

    suspend operator fun invoke(): Result<Unit> {
        while (notificationManager.getToken().isFailure) {
            delay(1000)
        }
        log("Get token ${notificationManager.getToken()}")
        return notificationManager.getToken().flatMap {
            networkClient.post<String>("checkin", mapOf("regid" to it))
        }.flatMap {
            refreshAlertHistory()
            listenForAlert()
        }
    }
}