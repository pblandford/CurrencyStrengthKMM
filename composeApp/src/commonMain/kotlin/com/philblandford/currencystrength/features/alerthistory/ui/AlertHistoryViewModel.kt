package com.philblandford.currencystrength.features.alerthistory.ui

import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.ui.BaseViewModel
import com.philblandford.currencystrength.common.ui.ScreenState
import com.philblandford.currencystrength.features.alerthistory.usecase.GetAlertHistory
import kotlinx.coroutines.flow.MutableStateFlow

sealed class AlertHistoryState : ScreenState() {
    data object Loading : AlertHistoryState()
    data class Loaded(val alerts: List<Alert>) : AlertHistoryState()
}

class AlertHistoryViewModel(private val getAlertHistory: GetAlertHistory) : BaseViewModel<AlertHistoryState>() {
    override val state = MutableStateFlow<AlertHistoryState>(AlertHistoryState.Loading)

    fun init() {
        tryResult {
            getAlertHistory().onSuccess {
                updateState { AlertHistoryState.Loaded(it.sortedBy {
                    it.lastAlert
                }.reversed()) }
            }
        }
    }
}