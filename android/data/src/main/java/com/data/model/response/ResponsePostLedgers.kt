package com.data.model.response

data class ResponsePostLedgers(
    val `data`: Data,
    val message: String
) {
    data class Data(
        val LedgerId: Int
    )
}