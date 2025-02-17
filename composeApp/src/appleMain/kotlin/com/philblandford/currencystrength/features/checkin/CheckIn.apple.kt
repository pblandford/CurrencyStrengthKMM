package com.philblandford.currencystrength.features.checkin

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
actual suspend fun waitForToken() {

    suspendCancellableCoroutine { continuation ->
        val observer = NSNotificationCenter.defaultCenter.addObserver(
            name = "didReceiveFCMToken",
            selector = null,
            `object` = null, observer = { notification: NSNotification ->
                val token = (notification.`object` as? String)
                if (!token.isNullOrBlank()) {
                    continuation.resume(token)
                }
            })
    }
}