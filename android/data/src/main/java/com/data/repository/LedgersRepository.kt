package com.data.repository

import com.data.model.response.ResponsePostLedgers
import com.data.util.ApiResult
import com.we.model.LedgersData
import com.we.model.MyMealTicketData
import kotlinx.coroutines.flow.Flow

interface LedgersRepository {
    suspend fun postLedgers(): Flow<ApiResult<LedgersData>>
    suspend fun getLedgers(): Flow<ApiResult<LedgersData>>
    suspend fun getMyMealTicket(): Flow<ApiResult<MyMealTicketData>>
}