package com.hana.fieldmate.domain.model

import java.time.LocalDate

data class BusinessEntity(
    val id: Long,
    val name: String,
    val clientName: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val memberEntities: List<MemberNameEntity>,
    val description: String,
    val revenue: Long
)
