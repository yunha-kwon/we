package com.data.model.response

data class ResponseAccountAuth(
    val `data`: Data,
    val message: String
) {
    data class Data(
        val authCode: String
    )
}