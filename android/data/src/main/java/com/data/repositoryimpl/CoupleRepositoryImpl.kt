package com.data.repositoryimpl

import com.data.datasource.CoupleDataSource
import com.data.mapper.toEntity
import com.data.mapper.toModel
import com.data.model.request.RequestCouple
import com.data.repository.CoupleRepository
import com.data.util.ApiResult
import com.data.util.safeApiCall
import com.we.model.CoupleData
import com.we.model.CoupleSuccessData
import com.we.model.GetCoupleData
import com.we.model.InvitationData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CoupleRepositoryImpl @Inject constructor(
    private val coupleDataSource: CoupleDataSource
) : CoupleRepository {
    override fun getCoupleCode(): Flow<ApiResult<CoupleData>> {
        return flow {
            emit(
                safeApiCall {
                    coupleDataSource.getCoupleCode().toEntity()
                }
            )
        }
    }

    override fun postCouple(requestCouple: RequestCouple): Flow<ApiResult<CoupleSuccessData>> {
        return flow {
            emit(
                safeApiCall {
                    coupleDataSource.postCouple(requestCouple).toModel()
                }
            )
        }
    }

    override fun getCouples(): Flow<ApiResult<GetCoupleData>> {
        return flow{
            emit(safeApiCall {
                coupleDataSource.getCouples().data.coupleInfo.toModel()
            })
        }
    }

    override fun getInvitation(): Flow<ApiResult<List<InvitationData>>> {
        return flow {
            emit(
                safeApiCall {
                    coupleDataSource.getInvitation().toEntity()
                }
            )
        }
    }
}