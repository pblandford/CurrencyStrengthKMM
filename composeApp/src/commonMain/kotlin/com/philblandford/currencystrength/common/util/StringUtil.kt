package com.philblandford.currencystrength.common.util

import com.philblandford.currencystrength.common.model.Currency
import com.philblandford.currencystrength.common.model.CurrencyPair
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

fun CurrencyPair?.asString(): String {
    return this?.let { "${base.asString()}/${counter.asString()}" } ?: "-/-"
}

 fun Currency?.asString(): String {
    return this?.name ?: "-"
}

fun LocalDateTime.asString(): String {
    return format(dateFormat)
}

val dateFormat = LocalDateTime.Format {
    dayOfMonth(padding = Padding.ZERO)
    char('/')
    monthNumber(padding = Padding.ZERO)
    char('/')
    year()
    char(' ')
    hour(padding = Padding.ZERO)
    char(':')
    minute(padding = Padding.ZERO)
}