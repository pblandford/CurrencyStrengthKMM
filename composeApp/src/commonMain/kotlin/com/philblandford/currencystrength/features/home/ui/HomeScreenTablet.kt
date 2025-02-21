package com.philblandford.currencystrength.features.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.features.alerthistory.ui.AlertHistoryScreen
import com.philblandford.currencystrength.features.chart.ui.ChartScreen
import com.philblandford.currencystrength.features.showalerts.ui.AlertsScreen

@Composable
fun HomeScreenTablet(notificationAlert: Alert? = null) {
    Column(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxWidth().weight(0.55f)) {
            ChartScreen(notificationAlert)
        }
        Row(Modifier.fillMaxWidth().weight(0.45f)) {
            Box(Modifier.fillMaxWidth(0.5f)) {
                AlertsScreen()
            }
            Box(Modifier.fillMaxWidth()) {
                AlertHistoryScreen()
            }
        }
    }
}