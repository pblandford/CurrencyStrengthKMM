package com.philblandford.currencystrength.common.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import okio.Path.Companion.toPath
import org.koin.java.KoinJavaComponent.get

private const val dataStoreFileName = "currencystrength.preferences_pb"


class KeyValueStoreAndroid(context: Context) : KeyValueStore {
    private val dataStore = PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = null,
        migrations = emptyList(),
        produceFile = { context.filesDir.resolve(dataStoreFileName).absolutePath.toPath() },
    )

    override suspend fun store(key: String, value: String) {
        dataStore.edit { settings ->
            settings[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun get(key: String): String? {
        return dataStore.data.first()[stringPreferencesKey(key)]
    }

}