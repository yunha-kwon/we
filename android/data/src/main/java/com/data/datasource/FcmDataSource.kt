package com.data.datasource

import com.data.model.request.RequestToken
import com.data.model.response.ResponseToken

interface FcmDataSource {
    suspend fun postToken(requestToken: String): ResponseToken
}