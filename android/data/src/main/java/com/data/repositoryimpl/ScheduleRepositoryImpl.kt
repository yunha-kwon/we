package com.data.repositoryimpl

import com.data.datasource.ScheduleDataSource
import com.data.mapper.toEntity
import com.data.mapper.toModel
import com.data.mapper.toScheduleEntity
import com.data.repository.ScheduleRepository
import com.data.util.ApiResult
import com.data.util.safeApiCall
import com.we.model.ScheduleData
import com.we.model.ScheduleParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleDataSource: ScheduleDataSource
) : ScheduleRepository {

    override fun getSchedule(
        year: Int,
        month: Int
    ): Flow<ApiResult<List<ScheduleData>>> {
        return flow {
            emit(safeApiCall {
                scheduleDataSource.getSchedule(year, month).toEntity()
            })
        }
    }

    override fun postSchedule(scheduleParam: ScheduleParam): Flow<ApiResult<ScheduleData>> {
        return flow {
            emit(safeApiCall {
                scheduleDataSource.postSchedule(scheduleParam.toModel()).toScheduleEntity()
            })
        }
    }

    override fun patchScheduleToggle(scheduleId: Int): Flow<ApiResult<ScheduleData>> {
        return flow {
            emit(safeApiCall {
                scheduleDataSource.patchScheduleToggle(scheduleId).toScheduleEntity()
            })
        }
    }

    override fun deleteSchedule(scheduleId: Int): Flow<ApiResult<Unit>> {
        return flow { emit(safeApiCall { scheduleDataSource.deleteSchedule(scheduleId) }) }
    }

    override fun updateSchedule(
        scheduleId: Int,
        scheduleParam: ScheduleParam
    ): Flow<ApiResult<ScheduleData>> {
        return flow {
            emit(safeApiCall {
                scheduleDataSource.patchSchedule(
                    scheduleId,
                    scheduleParam.toModel()
                ).toScheduleEntity()
            })
        }
    }
}