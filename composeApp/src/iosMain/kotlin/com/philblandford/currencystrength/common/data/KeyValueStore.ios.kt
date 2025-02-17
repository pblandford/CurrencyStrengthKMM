package com.philblandford.currencystrength.common.data

class KeyValueStoreIos : KeyValueStore {
    private val values = mutableMapOf<String, String?>()

    override suspend fun store(key: String, value: String) {
        values[key] = value
    }

    override suspend fun get(key: String): String? {
        return values[key]
    }
}