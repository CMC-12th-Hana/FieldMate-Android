package com.hana.fieldmate.domain.model

data class MemberEntity(
    val id: Long,
    val profileImg: Int,
    val name: String,
    val role: String,
    val company: String,
    val phoneNumber: String,
    val staffRank: String,
    val staffNumber: String
)
