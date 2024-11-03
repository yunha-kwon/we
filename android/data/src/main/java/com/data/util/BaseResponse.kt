package com.data.util

data class BaseResponse<T>(
    val message : String,
    val data : T?
)
