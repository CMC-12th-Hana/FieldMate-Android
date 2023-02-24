package com.hana.umuljeong.domain

data class Member(
    val id: Long,
    val profileImg: Int,
    val name: String,
    val company: String,
    val phone: String,
    val grade: String,
    val memberNum: String
)
