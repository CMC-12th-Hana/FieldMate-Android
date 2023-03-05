package com.hana.fieldmate.domain.model

data class MemberEntity(
    val id: Long,
    val profileImg: Int,
    val name: String,
    val company: String,
    val phone: String,
    val grade: String,
    val memberNum: String
)
