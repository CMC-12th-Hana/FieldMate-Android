package com.hana.umuljeong.data.model

data class Member(
    val id: Long,
    val profileImg: Int,
    val name: String,
    val email: String,
    val phone: String,
    val grade: String,
    val memberNum: String
)
