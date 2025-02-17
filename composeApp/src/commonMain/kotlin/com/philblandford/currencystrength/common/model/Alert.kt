package com.philblandford.currencystrength.common.model

import com.philblandford.currencystrength.common.network.CurrencyPairSerializer
import com.philblandford.currencystrength.common.network.KMMDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Alert(
    val period: Period,
    val sample: Int,
    val threshold: Float,
    val lastPair: CurrencyPair? = null,
    @Serializable(with = KMMDateTimeSerializer::class)
    val lastAlert: LocalDateTime? = null,
    val id: Int = 0
)
