package com.philblandford.currencystrength.features.alerthistory.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.model.Currency
import com.philblandford.currencystrength.common.model.CurrencyPair
import com.philblandford.currencystrength.common.util.asString
import com.philblandford.currencystrength.features.chart.ui.ColoredPairString
import currencystrengthcmm.composeapp.generated.resources.Res
import currencystrengthcmm.composeapp.generated.resources.alerts_history_title
import currencystrengthcmm.composeapp.generated.resources.alerts_pair
import currencystrengthcmm.composeapp.generated.resources.alerts_period
import currencystrengthcmm.composeapp.generated.resources.alerts_sample
import currencystrengthcmm.composeapp.generated.resources.alerts_threshold
import currencystrengthcmm.composeapp.generated.resources.alerts_time
import currencystrengthcmm.composeapp.generated.resources.alerts_title
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AlertHistoryScreen() {
    val viewModel: AlertHistoryViewModel = koinViewModel()

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    when (val state = viewModel.state.collectAsState().value) {
        is AlertHistoryState.Loading -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }

        is AlertHistoryState.Loaded -> {
            AlertHistoryInternal(state)
        }
    }
}

@Composable
private fun AlertHistoryInternal(alertHistoryState: AlertHistoryState.Loaded) {
    Column(Modifier.fillMaxSize().padding(10.dp)) {
        Spacer(Modifier.height(20.dp))
        Text(stringResource(Res.string.alerts_history_title))
        Spacer(Modifier.height(20.dp))
        LazyColumn {
            items(alertHistoryState.alerts) { alert ->
                AlertRow(alert)
            }
        }
    }
}

@Composable
private fun AlertRow(alert: Alert) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        TableCell(0.17f) {
            alert.lastPair.ColoredPairString(style = MaterialTheme.typography.bodySmall)
        }
        TableCell(text = alert.lastAlert?.asString() ?: "", weight = .27f)
        TableCell(text = alert.period.name, weight = .08f)
        TableCell(text = alert.sample.toString(), weight = .08f)
        TableCell(text = alert.threshold.toString(), weight = .08f)
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text,
        Modifier
            .border(1.dp, MaterialTheme.colorScheme.onSurface)
            .weight(weight)
            .padding(8.dp),
        style = MaterialTheme.typography.bodySmall
    )
}
@Composable
fun RowScope.TableCell(
    weight: Float,
    content:@Composable ()->Unit,
) {
    Box(
        Modifier
            .border(1.dp, MaterialTheme.colorScheme.onSurface)
            .weight(weight)
            .padding(8.dp)) {
        content()
    }

}



