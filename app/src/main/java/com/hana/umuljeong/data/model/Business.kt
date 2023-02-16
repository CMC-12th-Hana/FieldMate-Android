package com.hana.umuljeong.data.model

data class Business(
    val id: Long,
    val name: String,
    val startDate: String,
    val endDate: String,
    val members: List<Member>,
    val profit: Int
)
