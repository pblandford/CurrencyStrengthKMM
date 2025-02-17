package com.philblandford.currencystrength.common.error

import com.philblandford.currencystrength.common.log.LogLevel
import com.philblandford.currencystrength.common.log.logger
import kotlinx.coroutines.flow.MutableStateFlow

class ErrorHandler {
    val errorFlow = MutableStateFlow<Throwable?>(null)

    fun handleError(exception: Throwable, displayErrorToUser: Boolean = true) {
        logger.log(exception.message ?: "Unknown error", LogLevel.ERROR)
        if (displayErrorToUser) {
            errorFlow.tryEmit(exception)
        }
    }

    fun clearError() {
        errorFlow.tryEmit(null)
    }
}