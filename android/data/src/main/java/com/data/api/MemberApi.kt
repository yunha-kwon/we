package com.data.api

import com.data.model.response.ResponseMember
import retrofit2.http.GET

interface MemberApi {
    @GET("members")
    suspend fun getMembers() : ResponseMember
}