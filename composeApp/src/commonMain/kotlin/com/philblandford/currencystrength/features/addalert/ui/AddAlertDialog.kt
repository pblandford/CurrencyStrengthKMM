package com.philblandford.currencystrength.features.addalert.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.philblandford.currencystrength.common.ui.Spinner
import currencystrengthcmm.composeapp.generated.resources.Res
import currencystrengthcmm.composeapp.generated.resources.add_alert_add
import currencystrengthcmm.composeapp.generated.resources.add_alert_cancel
import currencystrengthcmm.composeapp.generated.resources.add_alert_title
import currencystrengthcmm.composeapp.generated.resources.alerts_period
import currencystrengthcmm.composeapp.generated.resources.alerts_sample
import currencystrengthcmm.composeapp.generated.resources.alerts_threshold
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun AddAlertDialog(onDismiss: () -> Unit,
                   onAdded: () -> Unit) {
    val viewModel: AddAlertViewModel = koinViewModel()

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    when (val state = viewModel.state.collectAsState().value) {
        is AddAlertState.Main -> {
            AddAlertInternal(state, viewModel, onDismiss)
        }
        is AddAlertState.Added -> {
            onAdded()
        }
    }
}

@Composable
internal fun AddAlertInternal(
    state: AddAlertState.Main,
    iface: AddAlertInterface,
    onDismiss: () -> Unit
) {
    Column(
        Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(stringResource(Res.string.add_alert_title))
        Spacer(Modifier.height(10.dp))
        TableRow(Res.string.alerts_period) {
            Spinner(Modifier, state.period, state.periodEntries, { name }, iface::onPeriodChange)
        }
        TableRow(Res.string.alerts_sample) {
            Spinner(
                Modifier,
                state.sample,
                state.sampleEntries,
                { toString() },
                iface::onSampleChange,
            )
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(stringResource(Res.string.alerts_threshold))
            Text(((state.threshold * 10).roundToInt().toFloat()/10).toString())
        }
        val stepSize = 0.1f
        val valueRange = 0f..10f
        val steps = ((valueRange.endInclusive - valueRange.start) / stepSize).toInt() - 1
        Slider(
            state.threshold,
            iface::onThresholdChange,
            Modifier.fillMaxWidth(),
            valueRange = valueRange,
            steps = steps
        )
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth()) {
            Button(onDismiss) {
                Text(stringResource(Res.string.add_alert_cancel))
            }
            Spacer(Modifier.weight(1f))
            Button(iface::add) {
                Text(stringResource(Res.string.add_alert_add))
            }
        }
    }
}

@Composable
private fun TableRow(labelResource: StringResource, widget: @Composable () -> Unit) {
    Row(Modifier.fillMaxWidth()) {
        Text(stringResource(labelResource), Modifier.weight(0.3f))
        widget()
    }
}
