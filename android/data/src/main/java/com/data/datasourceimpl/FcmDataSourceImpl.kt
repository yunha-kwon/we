package com.data.datasourceimpl

import com.data.api.FcmApi
import com.data.datasource.FcmDataSource
import com.data.model.request.RequestToken
import com.data.model.response.ResponseToken
import javax.inject.Inject

class FcmDataSourceImpl @Inject constructor(
    private val fcmApi: FcmApi
) : FcmDataSource {
    override suspend fun postToken(requestToken: String): ResponseToken {
        return fcmApi.postToken(requestToken)
    }
}