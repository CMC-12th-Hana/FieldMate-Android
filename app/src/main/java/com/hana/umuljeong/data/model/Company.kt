package com.hana.umuljeong.data.model

data class Company(
    val id: Long,
    val name: String,
    val phone: String,
    val department: String,
    val managerNm: String,
    val managerPhone: String,
    val visitNum: Int,
    val businessNum: Int
)
