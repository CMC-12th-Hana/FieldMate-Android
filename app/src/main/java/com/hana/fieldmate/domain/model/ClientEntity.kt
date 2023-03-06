package com.hana.fieldmate.domain.model

data class ClientEntity(
    val id: Long = -1L,
    val name: String = "",
    val phone: String = "",
    val salesRepresentativeDepartment: String = "",
    val salesRepresentativeName: String = "",
    val salesRepresentativePhone: String = "",
    val taskCount: Int = 0,
    val businessCount: Int = 0
)
