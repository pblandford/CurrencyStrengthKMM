package com.philblandford.currencystrength.common.error

actual fun Throwable.toUserFriendlyMessage(): String {
    return message ?: "An unknown error occurred."
}