package com.hana.fieldmate.domain.model

import java.time.LocalDate

data class BusinessEntity(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val memberEntities: List<MemberEntity>,
    val description: String,
    val revenue: String
)
