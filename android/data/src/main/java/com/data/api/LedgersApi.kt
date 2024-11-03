package com.data.api

import com.data.model.response.ResponseGetLedgers
import com.data.model.response.ResponseMealTicket
import com.data.model.response.ResponsePostLedgers
import retrofit2.http.GET
import retrofit2.http.POST

interface LedgersApi {
    @POST("ledgers")
    suspend fun postLedgers(): ResponsePostLedgers

    @GET("ledgers")
    suspend fun getLedgers(): ResponseGetLedgers


    @GET("ledgers/myMealTicket")
    suspend fun getMyMealTicket(): ResponseMealTicket
}