package com.philblandford.currencystrength.common.log

import org.lighthousegames.logging.logging

actual val logger = object : CSLogger {
    private val logger = logging()
    override fun log(message: String, logLevel: LogLevel) {
        when (logLevel) {
            LogLevel.TRACE -> logger.debug { message }
            LogLevel.DEBUG -> logger.debug { message }
            LogLevel.INFO -> logger.info { message }
            LogLevel.WARN -> logger.warn { message }
            LogLevel.ERROR -> logger.error { message }
            LogLevel.FATAL -> logger.error { message }
        }
    }
}