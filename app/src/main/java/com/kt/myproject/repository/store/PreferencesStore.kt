package com.kt.myproject.repository.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kt.myproject.app.app
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.io.IOException

object PreferencesStore {

    private val KEY_VALUE_SAMPLE = stringPreferencesKey("save_data")
    private val KEY_TOKEN_MESSAGE = stringPreferencesKey("token_message")

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "store")

    suspend fun saveData(data: String) {
        app.dataStore.edit {
            it[KEY_VALUE_SAMPLE] = data
        }
    }

    suspend fun saveToken(token: String) {
        app.dataStore.edit {
            it[KEY_TOKEN_MESSAGE] = token
        }
    }

    val token = app.dataStore.data
        .catch { e ->
            emit(emptyPreferences())
        }
        .map {
            it[KEY_TOKEN_MESSAGE] ?: ""
        }.flowOn(Dispatchers.IO)

    val value: Flow<String> = app.dataStore.data
        .catch { exception ->
            when (exception is IOException) {
                true -> emit(emptyPreferences())
                else -> throw exception
            }
        }
        .map { preferences ->
            preferences[KEY_VALUE_SAMPLE] ?: ""
        }.flowOn(Dispatchers.IO)

}