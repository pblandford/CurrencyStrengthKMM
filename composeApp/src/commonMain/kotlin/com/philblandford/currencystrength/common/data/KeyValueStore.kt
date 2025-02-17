package com.philblandford.currencystrength.common.data



interface KeyValueStore {
    suspend fun store(key:String, value:String)
    suspend fun get(key:String): String?
}
