package com.philblandford.currencystrength.features.notification.usecase

import com.philblandford.currencystrength.common.notifications.NotificationManager

class GetNotificationFlow(private val notificationManager: NotificationManager) {
    operator fun invoke() = notificationManager.notificationFlow
}