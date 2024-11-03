package com.we.di

import com.data.api.SignApi
import com.data.datasource.BankDataSource
import com.data.datasource.CoupleDataSource
import com.data.datasource.FcmDataSource
import com.data.datasource.LedgersDataSource
import com.data.datasource.MemberDataSource
import com.data.datasource.ScheduleDataSource
import com.data.datasource.SignDataSource
import com.data.datasourceimpl.BankDataSourceImpl
import com.data.datasourceimpl.CoupleDataSourceImpl
import com.data.datasourceimpl.FcmDataSourceImpl
import com.data.datasourceimpl.LedgersDataSourceImpl
import com.data.datasourceimpl.MemberDataSourceImpl
import com.data.datasourceimpl.ScheduleDataSourceImpl
import com.data.datasourceimpl.SignDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Singleton
    @Binds
    fun bindsSignDataSource(
        signDataSourceImpl: SignDataSourceImpl
    ): SignDataSource


    @Singleton
    @Binds
    fun bindsCoupleDataSource(
        coupleDataSourceImpl: CoupleDataSourceImpl
    ): CoupleDataSource

    @Singleton
    @Binds
    fun bindsBankDataSource(
        bankDataSourceImpl: BankDataSourceImpl
    ): BankDataSource

    @Singleton
    @Binds
    fun bindsFcmDataSource(
        fcmDataSourceImpl: FcmDataSourceImpl
    ) : FcmDataSource

    @Singleton
    @Binds
    fun bindsScheduleDataSource(
        scheduleDataSourceImpl: ScheduleDataSourceImpl
    ): ScheduleDataSource

    @Singleton
    @Binds
    fun bindsLedgersDataSource(
        ledgersDataSourceImpl: LedgersDataSourceImpl
    ): LedgersDataSource

    @Singleton
    @Binds
    fun bindsMemberDataSource(
        memberDataSourceImpl: MemberDataSourceImpl
    ): MemberDataSource
}