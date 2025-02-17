package com.philblandford.currencystrength.common.notifications

import com.mmk.kmpnotifier.notification.NotifierManager
import com.philblandford.currencystrength.common.log.log


class NotificationManager(private val notifierManager: NotifierManager) {

    suspend fun getToken(): Result<String> {
        return notifierManager.getPushNotifier().getToken()?.let {
            log("Got token $it")
            Result.success(it)
        } ?: Result.failure(Exception("Failed to get token"))
    }
}