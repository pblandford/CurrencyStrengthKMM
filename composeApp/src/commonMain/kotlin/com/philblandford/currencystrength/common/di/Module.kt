package com.philblandford.currencystrength.common.di

import org.koin.core.module.Module
import org.koin.dsl.module
import com.philblandford.currencystrength.common.app.AppViewModel
import org.koin.core.module.dsl.viewModelOf
import com.philblandford.currencystrength.common.network.NetworkClient
import com.philblandford.currencystrength.common.error.ErrorHandler
import com.philblandford.currencystrength.common.notifications.NotificationManager
import com.philblandford.currencystrength.common.data.FileSystemManager
import com.philblandford.currencystrength.common.data.RoomDatabaseManager
import com.philblandford.currencystrength.common.alert.AlertRepository
import com.philblandford.currencystrength.features.checkin.CheckIn
import com.philblandford.currencystrength.features.addalert.ui.AddAlertViewModel
import com.philblandford.currencystrength.features.addalert.usecase.AddAlert
import com.philblandford.currencystrength.features.alerthistory.usecase.GetAlertHistory
import com.philblandford.currencystrength.features.alerthistory.usecase.RefreshAlertHistory
import com.philblandford.currencystrength.features.alerthistory.usecase.InitAlertRepository
import com.philblandford.currencystrength.features.alerthistory.ui.AlertHistoryViewModel
import com.philblandford.currencystrength.features.logalert.usecase.GetLoggedAlerts
import com.philblandford.currencystrength.features.deletealert.usecase.DeleteAlert
import com.philblandford.currencystrength.features.home.ui.HomeViewModel
import com.philblandford.currencystrength.features.listenforalert.usecase.ListenForAlert
import com.philblandford.currencystrength.features.listenforalert.usecase.GetAlertFlow
import com.philblandford.currencystrength.features.showalerts.ui.AlertsViewModel
import com.philblandford.currencystrength.features.showalerts.usecase.GetAlerts
import com.philblandford.currencystrength.features.chart.usecase.GetPercentages
import com.philblandford.currencystrength.features.chart.ui.ChartViewModel
import com.philblandford.currencystrength.features.notification.usecase.GetNotificationFlow
import com.philblandford.currencystrength.features.notification.usecase.ClearNotifications
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf

expect val platformModule: Module

val module = module {

    singleOf(::NetworkClient)
    singleOf(::ErrorHandler)
    singleOf(::NotificationManager)
    singleOf(::FileSystemManager)
    singleOf(::RoomDatabaseManager)
    singleOf(::AlertRepository)

    factoryOf(::CheckIn)
    factoryOf(::AddAlert)
    factoryOf(::DeleteAlert)
    factoryOf(::GetPercentages)
    factoryOf(::GetAlerts)
    factoryOf(::GetLoggedAlerts)
    factoryOf(::GetAlertHistory)
    factoryOf(::GetNotificationFlow)
    factoryOf(::ClearNotifications)
    factoryOf(::GetAlertFlow)
    factoryOf(::ListenForAlert)
    factoryOf(::RefreshAlertHistory)
    factoryOf(::InitAlertRepository)

    viewModelOf(::AppViewModel)
    viewModelOf(::AddAlertViewModel)
    viewModelOf(::AlertsViewModel)
    viewModelOf(::ChartViewModel)
    viewModelOf(::AlertHistoryViewModel)
    viewModelOf(::HomeViewModel)
}