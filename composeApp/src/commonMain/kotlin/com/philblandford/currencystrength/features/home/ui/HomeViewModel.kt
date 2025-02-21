package com.philblandford.currencystrength.features.home.ui

import androidx.lifecycle.ViewModel
import com.philblandford.currencystrength.common.ui.BaseViewModel
import com.philblandford.currencystrength.common.ui.OrientationManager

class HomeViewModel(private val orientationManager: OrientationManager) : ViewModel() {
    fun isTablet() = orientationManager.isTablet()
}