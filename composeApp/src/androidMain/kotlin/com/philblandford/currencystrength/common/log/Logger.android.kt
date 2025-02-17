package com.philblandford.currencystrength.common.log

import org.lighthousegames.logging.logging

actual val logger: CSLogger = object : CSLogger {
    val logging = logging()

    override fun log(message: String, logLevel: LogLevel) {
        when (logLevel) {
            LogLevel.TRACE -> logging.debug { message }
            LogLevel.DEBUG -> logging.debug { message }
            LogLevel.INFO -> logging.info { message }
            LogLevel.WARN -> logging.warn { message }
            LogLevel.ERROR -> logging.error { message }
            LogLevel.FATAL -> logging.error { message }
        }
    }

}