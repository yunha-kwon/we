package com.we.core.util

import com.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject


class TokenInterceptor @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val requestBuilder = originRequest.newBuilder()
        val accessToken: String? = runBlocking {
            var token: String? = ""
            dataStoreRepository.getAccessToken().collectLatest {
                token = it
            }
            token
        }
        Timber.d("액세스 토큰 확인용 $accessToken")
        val request = requestBuilder.addHeader("Authorization", "Bearer $accessToken").build()
        return chain.proceed(request)
    }
}