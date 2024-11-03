package com.data.repository

import com.data.model.request.RequestCouple
import com.data.util.ApiResult
import com.we.model.CoupleData
import com.we.model.CoupleSuccessData
import com.we.model.GetCoupleData
import com.we.model.InvitationData
import kotlinx.coroutines.flow.Flow

interface CoupleRepository {
    fun getCoupleCode() : Flow<ApiResult<CoupleData>>

    fun postCouple(requestCouple: RequestCouple) : Flow<ApiResult<CoupleSuccessData>>

    fun getCouples(): Flow<ApiResult<GetCoupleData>>

    fun getInvitation() : Flow<ApiResult<List<InvitationData>>>
}