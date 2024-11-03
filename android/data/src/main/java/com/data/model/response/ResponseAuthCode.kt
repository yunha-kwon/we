package com.data.model.response

data class ResponseAuthCode(
    val `data`: Data,
    val message: String
) {
    data class Data(
        val Status: String
    )
}