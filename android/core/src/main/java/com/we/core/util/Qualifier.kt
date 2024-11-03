package com.we.core.util

import javax.inject.Qualifier

class Qualifier {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class InterceptorOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NoInterceptorOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class InterceptorRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NoInterceptorRetrofit
}