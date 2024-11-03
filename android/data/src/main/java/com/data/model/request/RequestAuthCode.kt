package com.data.model.request

data class RequestAuthCode(
    val accountNo: String,
    val authCode: String,
    val authText: String
)