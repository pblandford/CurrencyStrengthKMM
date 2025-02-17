package com.philblandford.currencystrength.features.addalert.usecase

import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.network.NetworkClient
import com.philblandford.currencystrength.common.notifications.NotificationManager
import com.philblandford.currencystrength.common.util.flatMap

class AddAlert(
    private val notificationManager: NotificationManager,
    private val networkClient: NetworkClient
) {
    suspend operator fun invoke(alert: Alert): Result<Unit> {
        return notificationManager.getToken().flatMap { token ->
            networkClient.post<String>(
                "alert/add", mapOf(
                    "regid" to token,
                    "period" to alert.period.name,
                    "sample" to alert.sample.toString(),
                    "threshold" to alert.threshold.toString()
                )
            ).map { }
        }
    }
}