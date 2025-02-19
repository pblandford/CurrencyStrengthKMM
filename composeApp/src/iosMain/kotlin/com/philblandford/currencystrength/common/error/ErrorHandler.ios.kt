package com.philblandford.currencystrength.common.error

import io.ktor.client.engine.darwin.DarwinHttpRequestException

actual fun Throwable.toUserFriendlyMessage(): String {
    return when (this) {
        is DarwinHttpRequestException -> {
            origin.localizedDescription
        }
        else -> this.message ?: "An unknown error occurred."
    }
}