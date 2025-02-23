package com.philblandford.currencystrength.features.alerthistory.usecase

import androidx.compose.runtime.State
import com.philblandford.currencystrength.common.alert.AlertRepository
import com.philblandford.currencystrength.common.log.log
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.network.NetworkClient
import com.philblandford.currencystrength.common.notifications.NotificationManager
import com.philblandford.currencystrength.common.util.flatMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GetAlertHistory(private val alertRepository: AlertRepository) {
    operator fun invoke():StateFlow<List<Alert>> = alertRepository.alertHistoryFlow
}