package com.philblandford.currencystrength.features.chart.usecase

import com.philblandford.currencystrength.common.model.PercentSet
import com.philblandford.currencystrength.common.model.Period
import com.philblandford.currencystrength.common.network.NetworkClient

class GetPercentages(private val networkClient: NetworkClient) {

    suspend operator fun invoke(period: Period, sample: Int): Result<List<PercentSet>> {
        return networkClient.getJson("percentages/${period.name}/$sample")
    }
}