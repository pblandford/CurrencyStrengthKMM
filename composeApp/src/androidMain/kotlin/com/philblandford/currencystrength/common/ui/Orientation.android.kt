package com.philblandford.currencystrength.common.ui

import android.content.Context
import android.content.res.Configuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OrientationManagerAndroid(private val context: Context) : OrientationManager {
    override val orientationFlow: MutableStateFlow<Orientation> = MutableStateFlow(Orientation.PORTRAIT)
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun updateConfig() {
        coroutineScope.launch {
            orientationFlow.emit(getCurrentOrientation(context))
        }
    }

    override fun isPortrait(): Boolean {
        val orientation = context.resources.configuration.orientation
        return orientation == Configuration.ORIENTATION_PORTRAIT
    }

    private fun getCurrentOrientation(context: Context): Orientation {
        val orientation = context.resources.configuration.orientation
        return if (orientation == Configuration.ORIENTATION_PORTRAIT) Orientation.PORTRAIT else Orientation.LANDSCAPE
    }
}