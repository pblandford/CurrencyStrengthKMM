package com.philblandford.currencystrength.common.notifications

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.philblandford.currencystrength.common.log.log

class NotificationLoggerService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let { sbNotification ->
            val packageName = sbNotification.packageName

            val title = sbNotification.notification.extras.getString(Notification.EXTRA_TITLE)
            val text = sbNotification.notification.extras.getString(Notification.EXTRA_TEXT)
            val timestamp = sbNotification.postTime
            log("[$timestamp] Notification extras: ${sbNotification.notification.extras}")
        }
    }
}