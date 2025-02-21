package com.philblandford.currencystrength.features.home.ui

import androidx.compose.runtime.Composable
import com.philblandford.currencystrength.common.model.Alert
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(notificationAlert:Alert? = null) {
    val viewModel:HomeViewModel = koinViewModel()

    if (viewModel.isTablet()) {
        HomeScreenTablet(notificationAlert)
    } else {
        HomeScreenPortrait(notificationAlert)
    }
}