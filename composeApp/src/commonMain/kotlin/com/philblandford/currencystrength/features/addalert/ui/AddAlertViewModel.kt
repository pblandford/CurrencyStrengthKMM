package com.philblandford.currencystrength.features.addalert.ui

import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.model.Period
import com.philblandford.currencystrength.common.ui.BaseViewModel
import com.philblandford.currencystrength.common.ui.ScreenState
import com.philblandford.currencystrength.features.addalert.usecase.AddAlert
import com.philblandford.currencystrength.features.showalerts.ui.AlertsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.roundToInt

sealed class AddAlertState : ScreenState() {
    data class Main(
        val period: Period,
        val sample: Int,
        val threshold: Float,
        val periodEntries: List<Period>,
        val sampleEntries: List<Int>,
        val maxThreshold: Float
    ) : AddAlertState()

    data object Added : AddAlertState()
}

interface AddAlertInterface {
    fun onPeriodChange(period: Period)
    fun onSampleChange(sample: Int)
    fun onThresholdChange(threshold: Float)
    fun add()
}

class AddAlertViewModel(private val addAlertUC: AddAlert) : BaseViewModel<AddAlertState>(),
    AddAlertInterface {
    override val state = MutableStateFlow<AddAlertState>(initState)

    fun init() {
        updateState { initState }
    }

    override fun onPeriodChange(period: Period) {
        updateMainState {
            copy(period = period)
        }
    }

    override fun onSampleChange(sample: Int) {
        updateMainState {
            copy(sample = sample)
        }
    }

    override fun onThresholdChange(threshold: Float) {
        val rounded = (threshold * 10).roundToInt().toFloat() / 10
        updateMainState {
            copy(threshold = rounded)
        }
    }

    override fun add() {
        (state.value as? AddAlertState.Main)?.let { state ->
            tryResult {
                addAlertUC(
                    Alert(
                        state.period,
                        state.sample,
                        state.threshold
                    )
                ).onSuccess {
                    updateState { AddAlertState.Added }
                }
            }
        }
    }

    private fun updateMainState(func: AddAlertState.Main.() -> AddAlertState.Main) {
        updateState {
            (this as? AddAlertState.Main)?.func() ?: this
        }
    }

    private val initState
        get() = AddAlertState.Main(
            period = Period.M5,
            sample = 50,
            threshold = 1.0f,
            periodEntries = Period.entries.filterNot { it == Period.H4 },
            sampleEntries = (10..100 step 10).toList(),
            maxThreshold = 10f
        )
}