package com.data.repositoryimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : DataStoreRepository {

    override suspend fun getAccessToken(): Flow<String> = flow {
        emit(dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY] ?: ""
        }.first())
    }

    override suspend fun setAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
        }
        // TokenProvider 관련 코드 제거
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("refreshToken")] = refreshToken
        }
        // TokenProvider 관련 코드 제거
    }

    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("accessToken")
    }
}