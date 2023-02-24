package com.hana.umuljeong.domain

data class Business(
    val id: Long,
    val name: String,
    val startDate: String,
    val endDate: String,
    val members: List<Member>,
    val profit: String
)
