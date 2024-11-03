package com.we.model

data class MemberData (
    val email: String = "",
    val id: Int = 0,
    val imageUrl: Any? = "",
    val leaved: Boolean = false,
    val leavedDate: Any? = "",
    val nickname: String = "",
    val regDate: String = "",
    val accessToken: String = "",
    val refreshToken: String = "",
    val coupleJoined : Boolean = false,
    val priorAccount : String ?= ""
)