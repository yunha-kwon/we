package com.data.repository

import com.data.model.response.ResponseSignUp
import com.data.util.ApiResult
import com.we.model.SignInParam
import com.we.model.MemberData
import com.we.model.SignUpParam
import kotlinx.coroutines.flow.Flow

interface SignRepository {

    fun postSignUp(signUpParam: SignUpParam) : Flow<ApiResult<ResponseSignUp>>

    fun postLogin(signInParam: SignInParam) : Flow<ApiResult<MemberData>>
}