package com.philblandford.currencystrength.common.model

import kotlinx.serialization.Serializable

@Serializable
data class PercentSet(val currency: Currency, val percentages:List<Float>)
