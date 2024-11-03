package com.data.model.response

data class ResponseTransactionHistory(
    val `data`: List<Data>,
    val message: String
) {
    data class Data(
        val transactionAccountNo: String,
        val transactionAfterBalance: String,
        val transactionBalance: String,
        val transactionDate: String,
        val transactionMemo: String,
        val transactionSummary: String,
        val transactionTime: String,
        val transactionType: String,
        val transactionTypeName: String,
        val transactionUniqueNo: String,
        val transactionUserName : String
    )
}