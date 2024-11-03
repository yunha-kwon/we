package com.data.model.response

data class ResponseMember(
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
        val priorAccount: String,
        val regDate: String
    )
}