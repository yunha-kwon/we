package com.data.model.request

data class RequestSignUp(
    val email : String,
    val password : String,
    val nickname : String,
    val pin : String
)