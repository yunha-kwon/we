package com.data.repositoryimpl

import com.data.datasource.FcmDataSource
import com.data.mapper.toModel
import com.data.model.request.RequestToken
import com.data.repository.FcmRepository
import com.data.util.ApiResult
import com.data.util.safeApiCall
import com.we.model.FcmData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FcmRepositoryImpl @Inject constructor(
    private val fcmDataSource: FcmDataSource
) : FcmRepository {
    override suspend fun postToken(requestToken: String): Flow<ApiResult<FcmData>> {
        return flow {

            val result = safeApiCall {
                val authCode = fcmDataSource.postToken(requestToken).toModel()
                authCode
            }

            emit(result)
        }
    }
}