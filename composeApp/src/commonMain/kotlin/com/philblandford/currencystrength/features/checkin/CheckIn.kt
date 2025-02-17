package com.philblandford.currencystrength.features.checkin

import com.philblandford.currencystrength.common.log.log
import com.philblandford.currencystrength.common.network.NetworkClient
import com.philblandford.currencystrength.common.notifications.NotificationManager
import com.philblandford.currencystrength.common.util.flatMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

expect suspend fun waitForToken()

class CheckIn(
    private val notificationManager: NotificationManager,
    private val networkClient: NetworkClient
) {

    suspend operator fun invoke(): Result<Unit> {
        while (notificationManager.getToken().isFailure) {
            delay(1000)
        }
        log("Get token ${notificationManager.getToken()}")
        return notificationManager.getToken().flatMap {
            networkClient.post<String>("checkin", mapOf("regid" to it))
        }.map { }
    }
}