package com.philblandford.currencystrength.common.log

enum class LogLevel {
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
    FATAL
}

interface CSLogger {
    fun log(message: String, logLevel: LogLevel = LogLevel.DEBUG)
}

expect val logger:CSLogger

fun log(message: String, logLevel: LogLevel = LogLevel.DEBUG) = logger.log(message, logLevel)
fun log(message: String, exception: Throwable) = logger.log(message, LogLevel.ERROR)