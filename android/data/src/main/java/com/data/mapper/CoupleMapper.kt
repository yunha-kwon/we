package com.data.mapper

import com.data.model.response.ResponseCouples
import com.data.model.response.ResponseCouplesCode
import com.data.model.response.ResponseGetCouples
import com.data.model.response.ResponseInvitation
import com.we.model.CoupleData
import com.we.model.CoupleSuccessData
import com.we.model.GetCoupleData
import com.we.model.InvitationData

fun ResponseCouplesCode.toEntity(): CoupleData {
    return CoupleData(
        code = this.data.code
    )
}

fun ResponseCouples.toModel(): CoupleSuccessData {
    return CoupleSuccessData(
        coupleId = this.data.coupleId
    )
}

fun ResponseGetCouples.Data.CoupleInfo.toModel(): GetCoupleData{
    return GetCoupleData(
        CoupeInfo = id,
        DDay = dday
    )
}

fun List<ResponseInvitation>.toEntity(): List<InvitationData> {
    return this.map {
        InvitationData(
            imageUrl = it.url,
            title = it.title,
            invitationId = it.invitationId
        )
    }
}