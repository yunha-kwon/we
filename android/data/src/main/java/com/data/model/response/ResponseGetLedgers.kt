package com.data.model.response

data class ResponseGetLedgers(
    val `data`: Data,
    val message: String
) {
    data class Data(
        val ledgerInfo: LedgerInfo
    ) {
        data class LedgerInfo(
            val coupleInfo: CoupleInfo,
            val id: Int,
            val qrCodeUrl: String?
        ) {
            data class CoupleInfo(
                val accountNumber: String,
                val bankbookCreated: Boolean,
                val id: Int,
                val ledgerCreated: Boolean,
                val member1: Member1,
                val member2: Member2
            ) {
                data class Member1(
                    val coupleJoined: Boolean,
                    val email: String,
                    val id: Int,
                    val imageUrl: Any?,
                    val leaved: Boolean,
                    val leavedDate: Any?,
                    val nickname: String,
                    val regDate: String
                )

                data class Member2(
                    val coupleJoined: Boolean,
                    val email: String,
                    val id: Int,
                    val imageUrl: Any?,
                    val leaved: Boolean,
                    val leavedDate: Any?,
                    val nickname: String,
                    val regDate: String
                )
            }
        }
    }
}