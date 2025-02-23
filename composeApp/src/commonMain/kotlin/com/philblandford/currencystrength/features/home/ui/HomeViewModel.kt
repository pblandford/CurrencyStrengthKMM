package com.philblandford.currencystrength.features.home.ui

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.philblandford.currencystrength.common.ui.OrientationManager
import com.philblandford.currencystrength.features.alerthistory.usecase.InitAlertRepository
import com.philblandford.currencystrength.features.listenforalert.usecase.GetAlertFlow

class HomeViewModel(
    private val orientationManager: OrientationManager,
    private val initAlertRepository: InitAlertRepository,
    getAlertFlow: GetAlertFlow
) : ViewModel(), DefaultLifecycleObserver {
    internal fun isTablet() = orientationManager.isTablet()
    internal val alertFlow = getAlertFlow()

    fun init(lifecycleOwner: LifecycleOwner) {
        initAlertRepository(lifecycleOwner)
    }
}