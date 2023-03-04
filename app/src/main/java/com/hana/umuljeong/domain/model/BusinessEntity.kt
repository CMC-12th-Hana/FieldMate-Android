package com.hana.umuljeong.domain.model

data class BusinessEntity(
    val id: Long,
    val name: String,
    val startDate: String,
    val endDate: String,
    val memberEntities: List<MemberEntity>,
    val content: String,
    val profit: String
)
