package com.philblandford.currencystrength.common.model

import kotlinx.serialization.Serializable

enum class Currency {
    USD, EUR, GBP, AUD, NZD, CAD, CHF, JPY;
    companion object {
        fun fromString(str:String):Currency? {
            return if (str == "-") null else Currency.valueOf(str)
        }
    }
}

@Serializable
data class CurrencyPair(val base: Currency? = null, val counter: Currency? = null) {
    override fun toString(): String {
        fun str(currency: Currency?): String = currency?.toString() ?: "-"
        return "${str(base)}/${str(counter)}"
    }

    companion object {
        fun fromString(str: String): CurrencyPair {
            val parts = str.split("/")
            return CurrencyPair(Currency.fromString(parts[0]), Currency.fromString(parts[1]))
        }
    }
}