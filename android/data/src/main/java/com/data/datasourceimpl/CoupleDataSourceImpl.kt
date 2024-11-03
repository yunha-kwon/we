package com.data.datasourceimpl

import com.data.api.CoupleApi
import com.data.datasource.CoupleDataSource
import com.data.model.request.RequestCouple
import com.data.model.response.ResponseCouples
import com.data.model.response.ResponseCouplesCode
import com.data.model.response.ResponseGetCouples
import com.data.model.response.ResponseInvitation
import javax.inject.Inject

class CoupleDataSourceImpl @Inject constructor(
    private val coupleApi: CoupleApi
): CoupleDataSource {
    override suspend fun getCoupleCode(): ResponseCouplesCode {
        return coupleApi.getCoupleCode()
    }

    override suspend fun postCouple(requestCouple: RequestCouple) : ResponseCouples {
        return coupleApi.postCouple(requestCouple)
    }

    override suspend fun getCouples(): ResponseGetCouples {
        return coupleApi.getCouples()
    }

    override suspend fun getInvitation(): List<ResponseInvitation> {
        return coupleApi.getInvitation()
    }
}