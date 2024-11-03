package com.data.model.response

data class ResponseCouples(
    val `data`: Data,
    val message: String
) {
    data class Data(
        val coupleId: Int
    )
}