package com.data.datasourceimpl

import com.data.api.LedgersApi
import com.data.datasource.LedgersDataSource
import com.data.model.response.ResponseGetLedgers
import com.data.model.response.ResponseMealTicket
import com.data.model.response.ResponsePostLedgers
import javax.inject.Inject

class LedgersDataSourceImpl @Inject constructor(
    private val ledgersApi: LedgersApi
) : LedgersDataSource {
    override suspend fun postLedgers(): ResponsePostLedgers {
        return ledgersApi.postLedgers()
    }

    override suspend fun getLedgers(): ResponseGetLedgers {
        return ledgersApi.getLedgers()
    }

    override suspend fun getMyMealTicket(): ResponseMealTicket {
        return ledgersApi.getMyMealTicket()
    }
}