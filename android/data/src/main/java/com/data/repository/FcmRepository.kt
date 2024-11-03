package com.data.repository

import com.data.model.request.RequestToken
import com.data.util.ApiResult
import com.we.model.FcmData
import kotlinx.coroutines.flow.Flow

interface FcmRepository {
    suspend fun postToken(requestToken: String) : Flow<ApiResult<FcmData>>
}