package com.philblandford.currencystrength.common.ui

import android.content.Context
import android.content.res.Configuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OrientationManagerAndroid(private val context: Context) : OrientationManager {
    override val orientationFlow: MutableStateFlow<Orientation> = MutableStateFlow(
        getCurrentOrientation(context)
    )
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

    override fun isTablet(): Boolean {
        val screenLayout = context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        return screenLayout >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    private fun getCurrentOrientation(context: Context): Orientation {
        val orientation = context.resources.configuration.orientation
        return if (orientation == Configuration.ORIENTATION_PORTRAIT) Orientation.PORTRAIT else Orientation.LANDSCAPE
    }
}