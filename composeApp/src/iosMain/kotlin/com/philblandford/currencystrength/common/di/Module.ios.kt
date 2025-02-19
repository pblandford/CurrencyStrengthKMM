package com.philblandford.currencystrength.common.di

import com.mmk.kmpnotifier.notification.NotifierManager
import com.philblandford.currencystrength.common.data.KeyValueStore
import com.philblandford.currencystrength.common.data.KeyValueStoreIos
import com.philblandford.currencystrength.common.notifications.NotificationManagerPlatform
import com.philblandford.currencystrength.common.notifications.NotificationManagerIos

import com.philblandford.currencystrength.common.permissions.PermissionGranter
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.philblandford.currencystrength.common.permissions.PermissionGranterIos
import com.philblandford.currencystrength.common.ui.OrientationManager
import com.philblandford.currencystrength.common.ui.OrientationManagerIos
import org.koin.dsl.bind

actual val platformModule: Module = module {
    singleOf(::PermissionGranterIos).bind(PermissionGranter::class)
    singleOf(::KeyValueStoreIos).bind(KeyValueStore::class)
    singleOf(::OrientationManagerIos).bind(OrientationManager::class)
    singleOf(::NotificationManagerIos).bind(NotificationManagerPlatform::class)

    single { NotifierManager }

}