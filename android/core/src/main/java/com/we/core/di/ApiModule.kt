package com.we.di

import com.data.api.BankApi
import com.data.api.CoupleApi
import com.data.api.FcmApi
import com.data.api.ScheduleApi
import com.data.api.LedgersApi
import com.data.api.MemberApi
import com.data.api.SignApi
import com.we.core.util.Qualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideSignApi(
        @Qualifier.NoInterceptorRetrofit
        retrofit: Retrofit
    ): SignApi = retrofit.create()


    @Singleton
    @Provides
    fun provideCoupleApi(
        @Qualifier.InterceptorRetrofit
        retrofit: Retrofit
    ): CoupleApi = retrofit.create()

    @Singleton
    @Provides
    fun provideBankApi(
        @Qualifier.InterceptorRetrofit
        retrofit: Retrofit
    ): BankApi = retrofit.create()

    @Singleton
    @Provides
    fun provideScheduleApi(
        @Qualifier.InterceptorRetrofit
        retrofit: Retrofit
    ): ScheduleApi = retrofit.create()

    @Singleton
    @Provides
    fun provideLedgersApi(
        @Qualifier.InterceptorRetrofit
        retrofit: Retrofit
    ): LedgersApi = retrofit.create()


    @Singleton
    @Provides
    fun provideFcmApi(
        @Qualifier.InterceptorRetrofit
        retrofit: Retrofit
    ): FcmApi = retrofit.create()

    @Singleton
    @Provides
    fun provideMemberApi(
        @Qualifier.InterceptorRetrofit
        retrofit: Retrofit
    ): MemberApi = retrofit.create()
}