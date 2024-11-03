package com.data.api

import com.data.model.request.RequestToken
import com.data.model.response.ResponseToken
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface FcmApi {
    @POST("notification/token")
    suspend fun postToken(@Query("token") requestToken: String): ResponseToken
}