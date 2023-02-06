package com.hana.umuljeong.data.model

import com.hana.umuljeong.getCurrentTime

data class Report(
    val id: Long,
    val customer: String,
    val name: String,
    val category: String,
    val date: String = getCurrentTime(),
    val content: String
)
