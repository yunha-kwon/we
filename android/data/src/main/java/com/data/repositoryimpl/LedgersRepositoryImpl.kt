package com.data.repositoryimpl

import com.data.datasource.LedgersDataSource
import com.data.mapper.toModel
import com.data.repository.LedgersRepository
import com.data.util.ApiResult
import com.data.util.safeApiCall
import com.we.model.LedgersData
import com.we.model.MyMealTicketData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LedgersRepositoryImpl @Inject constructor(
    private val ledgersDataSource: LedgersDataSource
) : LedgersRepository {
    override suspend fun postLedgers(): Flow<ApiResult<LedgersData>> {
        return flow{
            val result = safeApiCall {
                val qrCode = ledgersDataSource.postLedgers().data.toModel()
                qrCode
            }

            emit(result)
        }
    }

    override suspend fun getLedgers(): Flow<ApiResult<LedgersData>> {
        return flow {
            val result = safeApiCall {
                val qrCode = ledgersDataSource.getLedgers().data.ledgerInfo.toModel()
                qrCode
            }

            emit(result)
        }
    }

    override suspend fun getMyMealTicket(): Flow<ApiResult<MyMealTicketData>> {
        return flow {
            val result = safeApiCall {
                val qrCode = ledgersDataSource.getMyMealTicket().toModel()
                qrCode
            }
            emit(result)

        }
    }
}