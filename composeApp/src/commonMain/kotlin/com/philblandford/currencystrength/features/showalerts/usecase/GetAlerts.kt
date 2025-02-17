package com.philblandford.currencystrength.features.showalerts.usecase

import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.network.NetworkClient
import com.philblandford.currencystrength.common.notifications.NotificationManager
import com.philblandford.currencystrength.common.util.flatMap

class GetAlerts(
    private val notificationManager: NotificationManager,
    private val networkClient: NetworkClient
) {
    suspend operator fun invoke(): Result<List<Alert>> {
        return notificationManager.getToken().flatMap { regid ->
            networkClient.getJson("alerts/${regid}")
        }

    }
}