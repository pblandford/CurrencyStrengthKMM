package com.philblandford.currencystrength.features.showalerts.ui

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.philblandford.currencystrength.common.log.log
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.features.addalert.ui.AddAlertDialog
import com.philblandford.currencystrength.features.deletealert.usecase.DeleteAlert
import currencystrengthcmm.composeapp.generated.resources.Res
import currencystrengthcmm.composeapp.generated.resources.alerts_period
import currencystrengthcmm.composeapp.generated.resources.alerts_sample
import currencystrengthcmm.composeapp.generated.resources.alerts_threshold
import currencystrengthcmm.composeapp.generated.resources.alerts_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AlertsScreen() {
    val viewModel: AlertsViewModel = koinViewModel()

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    when (val state = viewModel.state.collectAsState().value) {
        is AlertsState.Loading -> {}
        is AlertsState.Loaded -> {
            AlertsScreenInternal(state, {
                viewModel.init()
            }) {
                viewModel.deleteAlert(it)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AlertsScreenInternal(
    alertsState: AlertsState.Loaded,
    onAdded: () -> Unit,
    deleteAlert: (Alert) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }
    log("showDialog $showDialog")

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(10.dp)) {
            Spacer(Modifier.height(20.dp))
            Text(stringResource(Res.string.alerts_title))
            Spacer(Modifier.height(20.dp))
            HeaderRow()
            LazyColumn {
                items(alertsState.alerts) { alert ->
                    AlertRow(alert) {
                        deleteAlert(alert)
                    }
                }
            }
        }
        FloatingActionButton(
            { showDialog = true },
            Modifier.padding(10.dp).align(Alignment.BottomEnd).offset(-20.dp, -20.dp)
        ) {
            Text("+", fontSize = 30.sp)
        }
    }

    if (showDialog) {
        BasicAlertDialog({
            showDialog = false
            log("showDialog dismiss $showDialog")
        }) {
            AddAlertDialog({
                showDialog = false
                log("showDialog cancel $showDialog")
            }) {
                onAdded()
                showDialog = false
                log("showDialog added $showDialog")
            }
        }
    }
}

@Composable
private fun HeaderRow() {
    Row(Modifier.fillMaxWidth()) {
        TableCell(text = stringResource(Res.string.alerts_period), weight = .2f)
        TableCell(text = stringResource(Res.string.alerts_sample), weight = .2f)
        TableCell(text = stringResource(Res.string.alerts_threshold), weight = .2f)
        Spacer(Modifier.weight(0.1f))
    }
}

@Composable
private fun AlertRow(alert: Alert, deleteAlert: ()->Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        TableCell(text = alert.period.name, weight = .2f)
        TableCell(text = alert.sample.toString(), weight = .2f)
        TableCell(text = alert.threshold.toString(), weight = .2f)
        Icon(Icons.Default.Delete, "",
            Modifier.clickable { deleteAlert() }.weight(0.1f))
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, MaterialTheme.colorScheme.onSurface)
            .weight(weight)
            .padding(8.dp)
    )
}

