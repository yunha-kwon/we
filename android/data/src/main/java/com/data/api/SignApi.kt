package com.data.api

import com.data.model.request.RequestSignIn
import com.data.model.request.RequestSignUp
import com.data.model.response.ResponseSignIn
import com.data.model.response.ResponseSignUp
import retrofit2.http.Body
import retrofit2.http.POST

interface SignApi {

    @POST("members/register") //회원가입
    suspend fun postSignUp(
        @Body requestSignUp: RequestSignUp
    ): ResponseSignUp


    @POST("auth/login")
    suspend fun  postLogin(
        @Body requestSignIn: RequestSignIn
    ): ResponseSignIn

}