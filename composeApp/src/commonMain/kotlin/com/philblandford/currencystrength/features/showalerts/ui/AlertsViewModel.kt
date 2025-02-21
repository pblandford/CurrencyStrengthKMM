package com.philblandford.currencystrength.features.showalerts.ui

import androidx.lifecycle.viewModelScope
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.model.Period
import com.philblandford.currencystrength.common.notifications.NotificationManager
import com.philblandford.currencystrength.common.ui.BaseViewModel
import com.philblandford.currencystrength.common.ui.ScreenState
import com.philblandford.currencystrength.features.deletealert.usecase.DeleteAlert
import com.philblandford.currencystrength.features.showalerts.usecase.GetAlerts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed class AlertsState : ScreenState() {
    data object Loading : AlertsState()
    data class Loaded(val alerts: List<Alert>) : AlertsState()
}


class AlertsViewModel(
    private val getAlerts: GetAlerts,
    private val deleteAlertUC: DeleteAlert,
    private val notificationManager: NotificationManager
) : BaseViewModel<AlertsState>() {
    override val state = MutableStateFlow<AlertsState>(AlertsState.Loading)

    fun init() {
        viewModelScope.launch {
            notificationManager.haveTokenFlow.collectLatest {
                if (it) {
                    tryResult {
                        getAlerts().onSuccess {
                            updateState { AlertsState.Loaded(it) }
                        }
                    }
                }
            }
        }
    }


    fun deleteAlert(alert: Alert) {
        tryResult {
            deleteAlertUC(alert).onSuccess {
                init()
            }
        }
    }
}
