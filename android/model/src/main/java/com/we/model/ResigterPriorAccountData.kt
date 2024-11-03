package com.we.model

data class ResigterPriorAccountData (
    val id : Int,
    val email : String,
    val nickname : String,
    val imageUrl : Any?,
    val regDate : String,
    val leavedDate : Any?,
    val coupleJoined : Boolean,
    val leaved : Boolean
)