package com.philblandford.currencystrength.features.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.features.alerthistory.ui.AlertHistoryScreen
import com.philblandford.currencystrength.features.chart.ui.ChartScreen
import com.philblandford.currencystrength.features.showalerts.ui.AlertsScreen

private sealed class RouteNode {
    data object Charts : RouteNode()
    data object Alerts : RouteNode()
    data object AlertHistory : RouteNode()
}

private data class NavigationItem(val icon: ImageVector, val node: RouteNode)


@Composable
fun HomeScreenPortrait(notificationAlert: Alert? = null) {
    val items = listOf(
        NavigationItem(Icons.Default.AccountBox, RouteNode.Charts),
        NavigationItem(Icons.Default.Notifications, RouteNode.Alerts),
        NavigationItem(Icons.Default.AccountBox, RouteNode.AlertHistory)
    )
    var selectedItem by remember { mutableStateOf(items.first()) }
    var alert by remember { mutableStateOf(notificationAlert) }

    Column(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxWidth().weight(1f)) {
            when (selectedItem.node) {
                RouteNode.Charts -> {
                    ChartScreen(alert)
                    alert = null
                }
                RouteNode.Alerts -> {
                    AlertsScreen()
                }
                RouteNode.AlertHistory -> {
                    AlertHistoryScreen()
                }
            }
        }

        BottomAppBar(Modifier.height(50.dp)) {
            items.forEach { item ->
                NavigationBarItem(selectedItem == item,
                    onClick = { selectedItem = item }, icon = {
                        Icon(item.icon, contentDescription = null)
                    })
            }
        }
    }
}