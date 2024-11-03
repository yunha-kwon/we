package com.data.model.response

data class ResponseTransfer(
    val `data`: Data,
    val message: String
) {
    data class Data(
        val STATUS: String
    )
}