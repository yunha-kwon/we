package com.data.model.response

data class ResponseCouplesCode(
    val `data`: Data,
    val message: String
) {
    data class Data(
        val code: String
    )
}