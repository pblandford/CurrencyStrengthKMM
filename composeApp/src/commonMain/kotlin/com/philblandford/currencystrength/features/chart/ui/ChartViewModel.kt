package com.philblandford.currencystrength.features.chart.ui

import androidx.lifecycle.viewModelScope
import com.philblandford.currencystrength.common.log.log
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.model.PercentSet
import com.philblandford.currencystrength.common.model.Period
import com.philblandford.currencystrength.common.ui.BaseViewModel
import com.philblandford.currencystrength.common.ui.Orientation
import com.philblandford.currencystrength.common.ui.OrientationManager
import com.philblandford.currencystrength.common.ui.ScreenState
import com.philblandford.currencystrength.common.util.flatMap
import com.philblandford.currencystrength.features.alerthistory.usecase.GetAlertHistory
import com.philblandford.currencystrength.features.chart.usecase.GetPercentages
import com.philblandford.currencystrength.features.listenforalert.usecase.GetAlertFlow
import com.philblandford.currencystrength.features.notification.usecase.ClearNotifications
import com.philblandford.currencystrength.features.notification.usecase.GetNotificationFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

sealed class ChartScreenState : ScreenState() {
    data class Main(
        val period: Period,
        val sample: Int,
        val percentSets: List<PercentSet> = listOf(),
        val periodEntries: List<Period> = Period.entries.filterNot { it == Period.H4 },
        val sampleEntries: List<Int> = (10..100 step 10).toList(),
        val isPortrait: Boolean = true,
        val lastAlert: Alert? = null
    ) : ChartScreenState()

}

interface ChartInterface {
    fun onPeriodChange(period: Period)
    fun onSampleChange(sample: Int)
    fun refresh()
    fun isPortrait(): Boolean
}

class ChartViewModel(
    private val getPercentagesUC: GetPercentages,
    private val orientationManager: OrientationManager,
    private val getAlertHistory: GetAlertHistory,
    private val clearNotifications: ClearNotifications,
    private val getAlertFlow: GetAlertFlow
) :
    BaseViewModel<ChartScreenState>(), ChartInterface {
    override val state = MutableStateFlow<ChartScreenState>(
        ChartScreenState.Main(
            Period.M5, 50,
            isPortrait = orientationManager.isPortrait() && !orientationManager.isTablet()
        )
    )
    private var refreshJob: Job? = null

    fun init(alert: Alert?) {
        viewModelScope.launch {
            clearNotifications()
        }
        alert?.let {
            updateMainState {
                copy(period = it.period, sample = it.sample)
            }
        }
        checkForRefresh()
        viewModelScope.launch {
            orientationManager.orientationFlow.collectLatest {
                updateState {
                    (this as? ChartScreenState.Main)?.copy(
                        isPortrait = it == Orientation.PORTRAIT
                                && !orientationManager.isTablet()
                    )
                        ?: this
                }
            }
        }
        viewModelScope.launch {
            getAlertFlow().collectLatest {
                log("Got notification")
                refresh()
            }
        }
        viewModelScope.launch {
            getAlertHistory().collectLatest {
                updateMainState {
                    copy(lastAlert = it.firstOrNull())
                }
            }
        }
    }

    fun dispose() {
        refreshJob?.cancel()
    }

    override fun isPortrait(): Boolean = orientationManager.isPortrait()

    override fun onPeriodChange(period: Period) {
        updateMainState {
            copy(period = period)
        }
        refresh()
    }

    override fun onSampleChange(sample: Int) {
        updateMainState {
            copy(sample = sample)
        }
        refresh()
    }

    override fun refresh() {
        (state.value as? ChartScreenState.Main)?.let { state ->
            tryResult(displayErrorToUser = false) {
                getPercentagesUC(state.period, state.sample).onSuccess { percentages ->
                    updateMainState {
                        copy(percentSets = percentages)
                    }
                }
            }
        }
    }


    private fun updateMainState(func: ChartScreenState.Main.() -> ChartScreenState.Main) {
        updateState {
            (this as? ChartScreenState.Main)?.func() ?: this
        }
    }

    private fun checkForRefresh() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            while (isActive) {
                refresh()
                delay(1000 * 60)
            }
        }
    }
}