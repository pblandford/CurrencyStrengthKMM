package com.philblandford.currencystrength.features.listenforalert.usecase

import com.philblandford.currencystrength.common.alert.AlertRepository

class GetAlertFlow(private val alertRepository: AlertRepository) {
    operator fun invoke() = alertRepository.alertFlow
}