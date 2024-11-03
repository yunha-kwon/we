package com.data.api

import com.data.model.request.RequestAccountAuth
import com.data.model.request.RequestAuthCode
import com.data.model.request.RequestRegisterCoupleAccount
import com.data.model.request.RequestRegisterPriorAccount
import com.data.model.request.RequestTransactionHistory
import com.data.model.request.RequestTransfer
import com.data.model.response.ResponseAccountAuth
import com.data.model.response.ResponseAuthCode
import com.data.model.response.ResponseBank
import com.data.model.response.ResponseCoupleAccount
import com.data.model.response.ResponseRegisterCoupleAccount
import com.data.model.response.ResponseResigterPriorAccount
import com.data.model.response.ResponseTransactionHistory
import com.data.model.response.ResponseTransfer
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BankApi {
    @GET("bank/my-account-test")
    suspend fun getMyAccountTest(): ResponseBank

    @GET("bank/my-account")
    suspend fun getMyAccount(): ResponseBank


    @POST("bank/checkAuthCode")
    suspend fun checkAuthCode(@Body request: RequestAuthCode): ResponseAuthCode

    @POST("bank/accountAuth")
    suspend fun accountAuth(@Body request: RequestAccountAuth): ResponseAccountAuth

    @GET("bank/my-couple-account")
    suspend fun getMyCoupleAccount(): ResponseCoupleAccount


    @POST("bank/transfer")
    suspend fun postTransfer(@Body request: RequestTransfer): ResponseTransfer

    @POST("bank/register-prior-account")
    suspend fun postPriorAccount(@Body request: RequestRegisterPriorAccount):ResponseResigterPriorAccount

    @POST("bank/register-couple-account")
    suspend fun postCoupleAccount(@Body request: RequestRegisterCoupleAccount): ResponseRegisterCoupleAccount

    @POST("bank/transaction-history-list")
    suspend fun postTransactionHistoryList(@Body requestTransactionHistory: RequestTransactionHistory): ResponseTransactionHistory

}