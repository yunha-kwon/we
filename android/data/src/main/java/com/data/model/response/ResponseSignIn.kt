package com.data.model.response

data class ResponseSignIn(
    val `data`: Data,
    val message: String
) {
    data class Data(
        val memberInfo: MemberInfo,
        val tokens: Tokens
    ) {
        data class MemberInfo(
            val id: Int,
            val email: String,
            val nickname: String,
            val imageUrl: Any,
            val regDate: String,
            val leavedDate: Any,
            val coupleJoined: Boolean,
            val leaved: Boolean,
            val priorAccount : String?
        )
    }

    data class Tokens(
        val accessToken: String,
        val refreshToken: String
    )
}