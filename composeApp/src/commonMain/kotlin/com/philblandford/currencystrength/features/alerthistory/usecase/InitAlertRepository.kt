package com.philblandford.currencystrength.features.alerthistory.usecase

import androidx.lifecycle.LifecycleOwner
import com.philblandford.currencystrength.common.alert.AlertRepository

class InitAlertRepository(private val alertRepository: AlertRepository) {
    operator fun invoke(lifecycleOwner: LifecycleOwner) {
        alertRepository.registerLifecycleObserver(lifecycleOwner)
    }
}