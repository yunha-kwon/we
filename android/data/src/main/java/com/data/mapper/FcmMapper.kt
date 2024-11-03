package com.data.mapper

import com.data.model.response.ResponseToken
import com.we.model.FcmData

fun ResponseToken.toModel(): FcmData {
    return FcmData(
        token = token,
        userId = userId
    )
}