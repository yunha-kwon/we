package com.we.model

data class BankData(
    val accountBalance: String= "",
    val accountCreatedDate: String = "",
    val accountExpiryDate: String = "",
    val accountName: String = "",
    val accountNo: String = "",
    val accountTypeCode: String="",
    val accountTypeName: String="",
    val bankCode: String="",
    val bankName: String="",
    val currency: String="",
    val dailyTransferLimit: String="",
    val lastTransactionDate: String="",
    val oneTimeTransferLimit: String="",
    val userName: String="",
    val accountInfo : String? = ""
)