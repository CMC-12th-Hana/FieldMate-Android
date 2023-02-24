package com.hana.umuljeong.data.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class CreateCompanyRes(
    @SerializedName("companyId")
    val companyId: Long,
    @SerializedName("createdAt")
    val createdAt: LocalDateTime
)