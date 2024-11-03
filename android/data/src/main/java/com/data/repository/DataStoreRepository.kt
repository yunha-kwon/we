package com.data.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun getAccessToken() : Flow<String>

    suspend fun setAccessToken(accessToken: String)
    suspend fun setRefreshToken(refreshToken: String)
}