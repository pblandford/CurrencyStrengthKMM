package com.philblandford.currencystrength.features.deletealert.usecase

import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.network.NetworkClient

class DeleteAlert(private val networkClient: NetworkClient) {
    suspend operator fun invoke(alert: Alert): Result<Unit> {
        return networkClient.post<Any>(
            "alert/delete",
            mapOf("id" to alert.id.toString())
        ).map { }
    }
}