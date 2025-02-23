package com.philblandford.currencystrength.features.alerthistory.usecase

import com.philblandford.currencystrength.common.alert.AlertRepository

class RefreshAlertHistory(private val alertRepository: AlertRepository) {

    suspend operator fun invoke() {
        alertRepository.refreshAlertHistory()

    }
}