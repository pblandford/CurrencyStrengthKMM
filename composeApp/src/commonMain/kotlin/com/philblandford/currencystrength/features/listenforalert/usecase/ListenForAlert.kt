package com.philblandford.currencystrength.features.listenforalert.usecase

import com.philblandford.currencystrength.common.alert.AlertRepository

class ListenForAlert(
    private val alertRepository: AlertRepository
) {
    suspend operator fun invoke() {
        alertRepository.listenForAlerts()
    }
}