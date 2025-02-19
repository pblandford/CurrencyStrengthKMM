package com.philblandford.currencystrength.common.notifications

import com.mmk.kmpnotifier.notification.NotifierManager
import com.philblandford.currencystrength.common.log.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

interface NotificationManagerPlatform {
    fun clearNotifications()
}

class NotificationManager(private val notifierManager: NotifierManager,
    private val notificationManagerPlatform: NotificationManagerPlatform) {
    val notificationFlow = MutableSharedFlow<Unit>()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    init {
        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                log("onNewToken: $token") //Update user token in the server if needed
            }

            override fun onPushNotification(title: String?, body: String?) {
                log("onPushNotification: $title, $body")
                coroutineScope.launch {
                    notificationFlow.emit(Unit)
                }
            }
        })
    }

    suspend fun getToken(): Result<String> {
        return notifierManager.getPushNotifier().getToken()?.let {
            log("Got token $it")
            Result.success(it)
        } ?: Result.failure(Exception("Failed to get token"))
    }

    fun clear() {
        notificationManagerPlatform.clearNotifications()
    }
}