package com.data.datasource

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

interface BankDataSource {
    suspend fun getMyAccountTest(): ResponseBank

    suspend fun getMyAccount(): ResponseBank

    suspend fun checkAuthCode(requestAuthCode: RequestAuthCode): ResponseAuthCode

    suspend fun accountAuth(requestAccountAuth: RequestAccountAuth): ResponseAccountAuth

    suspend fun getMyCoupleAccount(): ResponseCoupleAccount

    suspend fun postTransfer(requestTransfer: RequestTransfer): ResponseTransfer

    suspend fun postPriorAccount(request: RequestRegisterPriorAccount): ResponseResigterPriorAccount

    suspend fun postCoupleAccount(request: RequestRegisterCoupleAccount) : ResponseRegisterCoupleAccount

    suspend fun postTransactionHistoryList(request: RequestTransactionHistory): ResponseTransactionHistory
}