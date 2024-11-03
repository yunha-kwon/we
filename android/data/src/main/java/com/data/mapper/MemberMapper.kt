package com.data.mapper

import com.data.model.response.ResponseSignIn
import com.we.model.MemberData

fun ResponseSignIn.toEntity(): MemberData {
    return MemberData(
        email = this.data.memberInfo.email,
        id = this.data.memberInfo.id,
        imageUrl = this.data.memberInfo.imageUrl,
        leaved = this.data.memberInfo.leaved,
        leavedDate = this.data.memberInfo.leavedDate,
        nickname = this.data.memberInfo.nickname,
        regDate = this.data.memberInfo.regDate,
        coupleJoined = this.data.memberInfo.coupleJoined,
        accessToken = this.data.tokens.accessToken,
        refreshToken = this.data.tokens.refreshToken,
        priorAccount = this.data.memberInfo.priorAccount
    )
}