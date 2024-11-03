package com.we.presentation.util

import java.text.DecimalFormat

fun Long.addComma(): String {
    val decimalFormat = DecimalFormat("#,###")
    return decimalFormat.format(this)
}