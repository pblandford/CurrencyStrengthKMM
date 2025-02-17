package com.philblandford.currencystrength.feature.showalerts.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.model.Period
import com.philblandford.currencystrength.features.showalerts.ui.AlertsScreenInternal
import com.philblandford.currencystrength.features.showalerts.ui.AlertsState

@Composable
@Preview
fun AlertsScreenPreview() {
    MaterialTheme {
        AlertsScreenInternal(AlertsState.Loaded(testAlerts), {}) {}
    }
}

private val testAlerts = listOf(
    Alert(Period.M5, 10, 1.2f),
    Alert(Period.M15, 20, 1.2f),
    Alert(Period.H1, 50, 2f),
    Alert(Period.H4, 10, 0.5f),
)