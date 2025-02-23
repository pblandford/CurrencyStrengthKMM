package com.philblandford.currencystrength.common.notifications

import com.philblandford.currencystrength.common.log.log
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import kotlin.coroutines.resume

class NotificationManagerIos : NotificationManagerPlatform {
    private var notificationListener: (() -> Unit) = { }

    init {
        setup()
    }

    @OptIn(ExperimentalForeignApi::class)
    fun setup() {
        log("NM, adding observer")
        NSNotificationCenter.defaultCenter.addObserver(
            name = "didReceiveForegroundNotification",
            selector = null,
            `object` = null, observer = {
                log("kotlin: didReceiveForegroundNotification")
                notificationListener()
            })
    }

    override fun clearNotifications() {

    }

    override fun setForegroundNotificationListener(listener: () -> Unit) {
        this.notificationListener = listener
    }
}