package com.data.model.response

data class ResponseResigterPriorAccount(
    val `data`: Data,
    val message: String
) {
    data class Data(
        val coupleJoined: Boolean,
        val email: String,
        val id: Int,
        val imageUrl: Any,
        val leaved: Boolean,
        val leavedDate: Any,
        val nickname: String,
        val regDate: String
    )
}