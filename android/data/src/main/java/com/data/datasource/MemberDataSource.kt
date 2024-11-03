package com.data.datasource

import com.data.model.response.ResponseMember

interface MemberDataSource {
    suspend fun getMembers() : ResponseMember
}