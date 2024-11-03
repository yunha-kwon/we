package com.data.datasource

import com.data.model.request.RequestSignIn
import com.data.model.request.RequestSignUp
import com.data.model.response.ResponseSignIn
import com.data.model.response.ResponseSignUp

interface SignDataSource {

    suspend fun postSignUp(requestSignUp: RequestSignUp): ResponseSignUp

    suspend fun postLogin(requestSignIn: RequestSignIn): ResponseSignIn
}