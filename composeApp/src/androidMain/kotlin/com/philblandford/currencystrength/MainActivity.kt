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

        setContent {
            App()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        (orientationManager as? OrientationManagerAndroid)?.updateConfig()
    }

}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}