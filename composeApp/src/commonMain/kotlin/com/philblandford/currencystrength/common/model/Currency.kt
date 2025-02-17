package com.philblandford.currencystrength.common.model

import kotlinx.serialization.Serializable

enum class Currency {
    USD, EUR, GBP, AUD, NZD, CAD, CHF, JPY
}

@Serializable
data class CurrencyPair(val base: Currency? = null, val counter: Currency? = null) {
    override fun toString(): String {
        fun str(currency: Currency?): String = currency?.toString() ?: "-"
        return "${str(base)}/${str(counter)}"
    }
}