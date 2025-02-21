package com.philblandford.currencystrength.common.ui

import kotlinx.coroutines.flow.StateFlow

enum class Orientation {
    PORTRAIT, LANDSCAPE
}

interface OrientationManager {
    fun isPortrait(): Boolean
    fun isTablet():Boolean
    val orientationFlow:StateFlow<Orientation>
}