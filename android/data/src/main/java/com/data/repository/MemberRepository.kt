package com.data.repository

import com.data.util.ApiResult
import com.we.model.GetMemberData
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    suspend fun getMembers(): Flow<ApiResult<GetMemberData>>
}