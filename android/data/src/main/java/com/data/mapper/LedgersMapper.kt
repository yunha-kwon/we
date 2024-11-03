package com.data.mapper

import com.data.model.response.ResponseGetLedgers
import com.data.model.response.ResponseMealTicket
import com.data.model.response.ResponsePostLedgers
import com.we.model.LedgersData
import com.we.model.MyMealTicketData


fun ResponsePostLedgers.Data.toModel(): LedgersData {
    return LedgersData(
        url = LedgerId.toString()
    )
}

fun ResponseGetLedgers.Data.LedgerInfo.toModel(): LedgersData {
    return LedgersData(
        url = qrCodeUrl ?: ""
    )
}

fun ResponseMealTicket.toModel(): MyMealTicketData {
    return MyMealTicketData(
        url = data[0]
    )

}