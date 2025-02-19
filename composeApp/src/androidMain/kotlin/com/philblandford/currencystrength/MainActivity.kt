package com.philblandford.currencystrength

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationManagerCompat
import com.philblandford.currencystrength.common.app.App
import com.philblandford.currencystrength.common.log.log
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.model.CurrencyPair
import com.philblandford.currencystrength.common.model.Period
import com.philblandford.currencystrength.common.ui.OrientationManager
import com.philblandford.currencystrength.common.ui.OrientationManagerAndroid
import dev.icerock.moko.permissions.PermissionsController
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : ComponentActivity(), KoinComponent {
    private val orientationManager: OrientationManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissionsController: PermissionsController by inject()

        permissionsController.bind(this)

        val alert = getNotificationAlert()

        setContent {
            App(alert)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        (orientationManager as? OrientationManagerAndroid)?.updateConfig()
    }

    private fun getNotificationAlert(): Alert? {
        return intent.extras?.let { extras ->
            val period = extras.getString("period")
            val sample = extras.getString("sample")?.toIntOrNull() ?: 0
            val pair = extras.getString("pair")?.let {
                CurrencyPair.fromString(it)
            }
            val threshold = extras.getString("threshold")?.toFloatOrNull() ?: 0f
            log(
                "XXX Got alert: period=$period, sample=$sample, pair=$pair, threshold=$threshold"
            )
            if (period != null && pair != null) {
                Alert(Period.valueOf(period), sample, threshold, pair)
            } else {
                null
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}