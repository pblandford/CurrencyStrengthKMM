package com.philblandford.currencystrength.features.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.philblandford.currencystrength.common.log.log
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.features.chart.ui.ColoredPairString
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

private const val animationTime = 3000L

@Composable
fun HomeScreen(notificationAlert: Alert? = null) {
    val viewModel: HomeViewModel = koinViewModel()

    var showAlert by remember { mutableStateOf<Alert?>(null) }
    var isVisible by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {

        LaunchedEffect(showAlert) {
            if (showAlert != null) {

                isVisible = true
                delay(animationTime)
                isVisible = false
                delay(animationTime)
                showAlert = null
            }
        }

        if (viewModel.isTablet()) {
            HomeScreenTablet(notificationAlert)
        } else {
            HomeScreenPortrait(notificationAlert)
        }
    }

    AnimatedVisibility(
        isVisible,
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth * 2 },
            animationSpec = tween(durationMillis = animationTime.toInt(),
                easing = LinearEasing)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(durationMillis = animationTime.toInt(),
                easing = LinearEasing)
        )
    ) {
        showAlert?.let {
            Row(Modifier.background(MaterialTheme.colorScheme.surface)) {
                it.lastPair.ColoredPairString()
                Text(" ${it.period} ${it.sample} ${it.threshold}")
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.alertFlow.collect {
            showAlert = it
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.init(lifecycleOwner)
    }

}