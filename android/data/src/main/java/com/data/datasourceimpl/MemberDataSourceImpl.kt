package com.data.datasourceimpl

import com.data.api.MemberApi
import com.data.datasource.MemberDataSource
import com.data.model.response.ResponseMember
import javax.inject.Inject

class MemberDataSourceImpl @Inject constructor(
    private val memberApi: MemberApi
) : MemberDataSource{
    override suspend fun getMembers(): ResponseMember {
        return memberApi.getMembers()
    }
}