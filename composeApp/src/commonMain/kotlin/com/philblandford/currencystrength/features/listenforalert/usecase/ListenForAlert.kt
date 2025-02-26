package com.philblandford.currencystrength.features.listenforalert.usecase

import com.philblandford.currencystrength.common.alert.AlertRepository
import com.philblandford.currencystrength.common.audio.AudioPlayer
import currencystrengthcmm.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

class ListenForAlert(
    private val alertRepository: AlertRepository,
) {
    @OptIn(ExperimentalResourceApi::class)
    suspend operator fun invoke():Result<Unit> {
        return alertRepository.listenForAlerts()

    }
}