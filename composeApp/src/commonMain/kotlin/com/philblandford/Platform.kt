package com.philblandford

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform