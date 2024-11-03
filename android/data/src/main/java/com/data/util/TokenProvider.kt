package com.data.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenProvider @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("accessToken")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refreshToken")
    }

    @Volatile
    private var cachedAccessToken: String? = null
    @Volatile
    private var cachedRefreshToken: String? = null

    init {
        runBlocking {
            saveAccessToken(""){}
            loadingToken()
        }
        // 앱 시작 시 토큰 로드

    }

    fun loadingToken() {
        runBlocking {
            cachedAccessToken = dataStore.data
                .map { preferences -> preferences[ACCESS_TOKEN_KEY] }
                .first()
            cachedRefreshToken = dataStore.data
                .map { preferences -> preferences[REFRESH_TOKEN_KEY] }
                .first()
        }
    }

    fun getAccessToken(): String? = cachedAccessToken
    fun getRefreshToken(): String? = cachedRefreshToken

    // 토큰 저장 메서드 추가
    suspend fun saveAccessToken(token: String, onResult: () -> Unit) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
        updateAccessTokenCache(token)
        onResult()
    }

    suspend fun saveRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = token
        }
        updateRefreshTokenCache(token)
    }

    private fun updateAccessTokenCache(token: String) {
        cachedAccessToken = token
    }

    private fun updateRefreshTokenCache(token: String) {
        cachedRefreshToken = token
    }
}