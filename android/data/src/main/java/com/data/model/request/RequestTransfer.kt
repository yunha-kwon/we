package com.data.model.request

data class RequestTransfer(
    val depositAccountNo: String?,
    val isBride: Boolean?,
    val ledgerId: Int?,
    val pin: String,
    val transactionBalance: Int,
    val withdrawalAccountNo: String?
)