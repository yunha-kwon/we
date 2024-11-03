package com.data.repositoryimpl

import com.data.datasource.SignDataSource
import com.data.mapper.toEntity
import com.data.mapper.toModel
import com.data.model.response.ResponseSignUp
import com.data.repository.DataStoreRepository
import com.data.repository.SignRepository
import com.data.util.ApiResult
import com.data.util.safeApiCall
import com.we.model.SignInParam
import com.we.model.MemberData
import com.we.model.SignUpParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor(
    private val signDataSource: SignDataSource,
    private val dataStoreRepository: DataStoreRepository

) : SignRepository {
    override fun postSignUp(signUpParam: SignUpParam): Flow<ApiResult<ResponseSignUp>> {
        return flow {
            emit(
                safeApiCall {
                    signDataSource.postSignUp(signUpParam.toModel())
                }
            )
        }
    }

    override fun postLogin(signInParam: SignInParam): Flow<ApiResult<MemberData>> {
        return flow {

            val apiResult = safeApiCall {
                signDataSource.postLogin(signInParam.toModel()).toEntity()
            }

            if (apiResult is ApiResult.Success) {
                dataStoreRepository.setAccessToken(apiResult.data.accessToken)
                dataStoreRepository.setRefreshToken(apiResult.data.refreshToken)
            }

            emit(apiResult)
        }
    }
}