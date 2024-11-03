package com.data.repositoryimpl

import com.data.datasource.MemberDataSource
import com.data.mapper.toModel
import com.data.repository.MemberRepository
import com.data.util.ApiResult
import com.data.util.safeApiCall
import com.we.model.GetMemberData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val memberDataSource: MemberDataSource
) : MemberRepository {
    override suspend fun getMembers(): Flow<ApiResult<GetMemberData>> {
        return flow {
            val result = safeApiCall {
                val members = memberDataSource.getMembers().data.toModel()
                members
            }
            emit(result)
        }
    }
}