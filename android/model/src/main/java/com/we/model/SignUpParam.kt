package com.we.model

data class SignUpParam(
    val email: String = "",
    val emailName: String = "",
    val nickname: String = "",
    val password: String = "",
    val easyPassword: List<String> = mutableListOf(),
    val easyPasswordCheck: List<String> = mutableListOf(),
) {
    companion object {
        val DEFAULT = SignUpParam(
            "", "", "", "", mutableListOf(), mutableListOf()
        )
    }
}
