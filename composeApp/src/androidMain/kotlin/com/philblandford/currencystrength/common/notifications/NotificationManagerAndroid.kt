package com.philblandford.currencystrength.common.notifications

import android.content.Context
import androidx.core.app.NotificationManagerCompat

class NotificationManagerAndroid(private val context: Context) : NotificationManagerPlatform {
    override fun clearNotifications() {
        NotificationManagerCompat.from(context).cancelAll()
    }
}