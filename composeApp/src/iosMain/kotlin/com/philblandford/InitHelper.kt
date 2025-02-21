package com.philblandford

import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.philblandford.currencystrength.common.di.module
import com.philblandford.currencystrength.common.di.platformModule
import com.philblandford.currencystrength.common.log.log
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(module + platformModule)
    }
}

fun initNotifications() {

    log("initNotifications")

    NotifierManager.initialize(
        configuration = NotificationPlatformConfiguration.Ios(
            showPushNotification = true
        )
    )

}

