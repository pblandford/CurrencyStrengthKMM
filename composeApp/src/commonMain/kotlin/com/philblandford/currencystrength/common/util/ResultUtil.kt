package com.philblandford.currencystrength.common.util

suspend fun <T, R> Result<T>.flatMap(func: suspend (T) -> Result<R>): Result<R> {
    return if (isSuccess) {
        func(this.getOrThrow())
    } else {
        Result.failure((exceptionOrNull() as Exception))
    }
}