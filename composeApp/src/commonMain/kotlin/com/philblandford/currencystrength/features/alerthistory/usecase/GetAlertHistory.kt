package com.philblandford.currencystrength.features.alerthistory.usecase

import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.network.NetworkClient
import com.philblandford.currencystrength.common.notifications.NotificationManager
import com.philblandford.currencystrength.common.util.flatMap

class GetAlertHistory(
    private val notificationManager: NotificationManager,
    private val networkClient: NetworkClient) {

    suspend operator fun invoke(): Result<List<Alert>> {
        return notificationManager.getToken().flatMap { token ->
            networkClient.getJson("alerts/log/$token")
        }
    }
}