package com.we.presentation.account.util

import com.we.presentation.R

data class BankList(
    val bankIcList: Int = 0,
    val bankName: String = ""
) {
    companion object {
        val DEFAULT = BankList(
            0,""
        )
        val bankLs = arrayListOf<BankList>(
            BankList(bankIcList = R.drawable.ic_nh_bank, bankName = "농협은행"),
            BankList(bankIcList = R.drawable.ic_kb_bank, bankName = "국민은행"),
            BankList(bankIcList = R.drawable.ic_sinhan_bank, bankName = "신한은행"),
            BankList(bankIcList = R.drawable.ic_woori_bank, bankName = "우리은행"),
            BankList(bankIcList = R.drawable.ic_hana_bank, bankName = "하나은행"),
            BankList(bankIcList = R.drawable.ic_ibk_bank, bankName = "IBK기업"),
            BankList(bankIcList = R.drawable.ic_sc_bank, bankName = "SC제일"),
            BankList(bankIcList = R.drawable.ic_kakao_bank, bankName = "카카오뱅크"),
            BankList(bankIcList = R.drawable.ic_jb_bank, bankName = "전북은행"),
            BankList(bankIcList = R.drawable.ic_woo_bank, bankName = "우체국"),
            BankList(bankIcList = R.drawable.ic_busan_bank, bankName = "부산은행"),
            BankList(bankIcList = R.drawable.ic_toss_bank, bankName = "토스"),
            BankList(bankIcList = R.drawable.ic_korea_bank, bankName = "한국은행"),
            BankList(bankIcList = R.drawable.ic_daegu_bank, bankName = "대구은행"),
        )
    }
}

