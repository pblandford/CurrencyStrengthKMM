package com.philblandford.currencystrength.feature.addalert.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.philblandford.currencystrength.common.model.Period
import com.philblandford.currencystrength.features.addalert.ui.AddAlertInterface
import com.philblandford.currencystrength.features.addalert.ui.AddAlertInternal
import com.philblandford.currencystrength.features.addalert.ui.AddAlertState

@Preview
@Composable
fun AddAlertPreview() {
    var state by remember {
        mutableStateOf(
            AddAlertState.Main(
                period = Period.M5,
                sample = 10,
                threshold = 1.2f,
                periodEntries = Period.entries,
                sampleEntries = (10..100 step 10).toList(),
                maxThreshold = 10f
            )
        )
    }

    AddAlertInternal(state, object : AddAlertInterface  {
        override fun onPeriodChange(period: Period) {
            state = state.copy(period = period)
        }

        override fun onSampleChange(sample: Int) {
            state = state.copy(sample = sample)
        }

        override fun onThresholdChange(threshold: Float) {
            state = state.copy(threshold = threshold)
        }

        override fun add() {
            TODO("Not yet implemented")
        }
    }) {}
}