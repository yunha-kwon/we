package com.we.model

data class TransactionHistoryData (
    val transactionTypeName : String,
    val transactionAccountNo : String,
    val transactionBalance : String,
    val transactionAfterBalance : String,
    val transactionType : String,
    val transactionDate : String,
    val transactionUserName : String
)