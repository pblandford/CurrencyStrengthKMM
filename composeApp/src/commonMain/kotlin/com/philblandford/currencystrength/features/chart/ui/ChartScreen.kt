package com.philblandford.currencystrength.features.chart.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.model.Currency
import com.philblandford.currencystrength.common.ui.Spinner
import com.philblandford.currencystrength.common.util.asString
import com.philblandford.currencystrength.features.help.ui.HelpDialog
import currencystrengthcmm.composeapp.generated.resources.Res
import currencystrengthcmm.composeapp.generated.resources.chart_help
import currencystrengthcmm.composeapp.generated.resources.chart_last_alert
import io.ktor.util.hex
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChartScreen() {
    val viewModel: ChartViewModel = koinViewModel()

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.dispose()
        }
    }

    val state = viewModel.state.collectAsState().value
    check(state is ChartScreenState.Main)
    ChartScreenInternal(state, viewModel)
}

@Composable
private fun ChartScreenInternal(state: ChartScreenState.Main, iface: ChartInterface) {
    var showHelp by remember { mutableStateOf(false) }

    if (state.isPortrait) {
        ChartScreenPortrait(state, iface) {
            showHelp = !showHelp
        }
    } else {
        ChartScreenLandscape(state, iface)
    }

    HelpDialog(showHelp) {
        showHelp = false
    }
}

@Composable
internal fun ChartScreenPortrait(
    state: ChartScreenState.Main, iface: ChartInterface,
    toggleHelp: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(10.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Icon(Icons.Default.Info, stringResource(Res.string.chart_help),
                Modifier.clickable { toggleHelp() })
            Row {
                Spinner(
                    Modifier,
                    state.period,
                    state.periodEntries,
                    { name },
                    iface::onPeriodChange
                )
                Spinner(
                    Modifier,
                    state.sample,
                    state.sampleEntries,
                    { toString() },
                    iface::onSampleChange
                )
            }
        }
        Spacer(Modifier.height(50.dp))
        Chart(Modifier.fillMaxWidth().height(200.dp), state.percentSets.map {
            DataSet(it.currency.name, it.percentages, it.currency.color())
        })
        Spacer(Modifier.height(50.dp))
        LegendPortrait()
        Spacer(Modifier.height(50.dp))
        LastAlertPortrait(state.lastAlert)
    }
}

@Composable
private fun ChartScreenLandscape(state: ChartScreenState.Main, iface: ChartInterface) {
    Column(Modifier.fillMaxWidth().padding(10.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            LegendLandscape(Modifier.weight(1f))
            Spacer(Modifier.width(20.dp))
            Spinner(Modifier, state.period, state.periodEntries, { name }, iface::onPeriodChange)
            Spacer(Modifier.width(20.dp))
            Spinner(
                Modifier,
                state.sample,
                state.sampleEntries,
                { toString() },
                iface::onSampleChange
            )
        }
        Spacer(Modifier.height(20.dp))
        Chart(Modifier.fillMaxWidth().weight(1f), state.percentSets.map {
            DataSet(it.currency.name, it.percentages, it.currency.color())
        })
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun LegendPortrait() {
    LazyVerticalGrid(GridCells.Fixed(2), Modifier.fillMaxWidth()) {
        items(Currency.entries) { currency ->
            Row(
                Modifier.fillMaxWidth().padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(currency.name)
                Spacer(Modifier.width(5.dp))
                Box(Modifier.weight(1f).height(2.dp).background(currency.color()))
            }
        }
    }
}

@Composable
private fun LegendLandscape(modifier: Modifier) {
    Row(modifier) {
        Currency.entries.forEach { currency ->
            Row(
                Modifier.weight(1f / 8).padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(currency.name)
                Spacer(Modifier.width(5.dp))
                Box(Modifier.weight(1f).height(2.dp).background(currency.color()))
            }
        }

    }
}

@Composable
private fun LastAlertPortrait(alert: Alert?) {
    alert?.let {
        Column {
            Text(stringResource(Res.string.chart_last_alert))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(alert.period.name)
                Spacer(Modifier.width(5.dp))
                Text(alert.sample.toString())
                Spacer(Modifier.width(5.dp))
                Text(alert.threshold.toString())
                Spacer(Modifier.width(5.dp))
                Text(alert.lastPair.asString())
                Spacer(Modifier.width(5.dp))
                Text(alert.lastAlert?.asString() ?: "")
            }
        }
    }
}

private fun Currency.color(): Color {
    return when (this) {
        Currency.USD -> Color.Green
        Currency.EUR -> Color.Cyan
        Currency.GBP -> Color.Red
        Currency.AUD -> Color.Blue
        Currency.NZD -> Color.Gray
        Currency.CAD -> Color.Magenta
        Currency.CHF -> Color.Yellow
        Currency.JPY -> Color.White
    }
}