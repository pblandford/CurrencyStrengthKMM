package com.philblandford.currencystrength.features.alerthistory.ui

import androidx.lifecycle.viewModelScope
import com.philblandford.currencystrength.common.log.log
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.notifications.NotificationManager
import com.philblandford.currencystrength.common.ui.BaseViewModel
import com.philblandford.currencystrength.common.ui.ScreenState
import com.philblandford.currencystrength.features.alerthistory.usecase.GetAlertHistory
import com.philblandford.currencystrength.features.notification.usecase.GetNotificationFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed class AlertHistoryState : ScreenState() {
    data object Loading : AlertHistoryState()
    data class Loaded(val alerts: List<Alert>) : AlertHistoryState()
}

class AlertHistoryViewModel(
    private val getAlertHistory: GetAlertHistory
) : BaseViewModel<AlertHistoryState>() {
    override val state = MutableStateFlow<AlertHistoryState>(AlertHistoryState.Loading)

    fun init() {
        viewModelScope.launch {
            getAlertHistory().collectLatest {
                updateState {
                    AlertHistoryState.Loaded(it)
                }
            }
        }
    }
}