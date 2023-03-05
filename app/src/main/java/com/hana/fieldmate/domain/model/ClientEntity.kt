package com.hana.fieldmate.domain.model

data class ClientEntity(
    val id: Long,
    val name: String,
    val phone: String,
    val department: String,
    val managerNm: String,
    val managerPhone: String,
    val visitNum: Int,
    val businessNum: Int
)
