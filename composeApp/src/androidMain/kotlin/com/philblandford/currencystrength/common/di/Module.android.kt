package com.philblandford.currencystrength.common.di

import com.mmk.kmpnotifier.notification.NotifierManager
import com.philblandford.currencystrength.common.audio.AudioPlayer
import com.philblandford.currencystrength.common.data.KeyValueStore
import com.philblandford.currencystrength.common.data.KeyValueStoreAndroid
import com.philblandford.currencystrength.common.data.FileSystemConfig
import com.philblandford.currencystrength.common.data.FileSystemConfigAndroid
import com.philblandford.currencystrength.common.data.getDatabaseBuilder
import com.philblandford.currencystrength.common.notifications.NotificationManager
import com.philblandford.currencystrength.common.notifications.NotificationManagerPlatform
import com.philblandford.currencystrength.common.notifications.NotificationManagerAndroid
import com.philblandford.currencystrength.common.permissions.PermissionGranter
import com.philblandford.currencystrength.common.permissions.PermissionGranterAndroid
import com.philblandford.currencystrength.common.ui.OrientationManager
import com.philblandford.currencystrength.common.ui.OrientationManagerAndroid
import com.philblandford.currencystrength.common.audio.AudioPlayerAndroid
import dev.icerock.moko.permissions.PermissionsController
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { PermissionsController(get()) }
    single<PermissionGranter> { PermissionGranterAndroid(get()) }
    single { NotifierManager }
    single<KeyValueStore> { KeyValueStoreAndroid(get()) }
    singleOf(::FileSystemConfigAndroid).bind(FileSystemConfig::class)
    singleOf(::OrientationManagerAndroid).bind(OrientationManager::class)
    singleOf(::NotificationManagerAndroid).bind(NotificationManagerPlatform::class)
    singleOf(::AudioPlayerAndroid).bind(AudioPlayer::class)
    single { getDatabaseBuilder(get()) }
}