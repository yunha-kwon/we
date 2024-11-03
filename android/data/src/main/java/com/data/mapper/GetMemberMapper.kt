package com.data.mapper

import com.data.model.response.ResponseMember
import com.we.model.GetMemberData

fun ResponseMember.Data.toModel() : GetMemberData{
    return GetMemberData(
        name = nickname
    )
}