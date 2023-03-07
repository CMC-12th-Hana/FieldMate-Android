package com.hana.fieldmate.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class CreateCompanyRes(
    @SerializedName("companyId")
    val companyId: Long,
    @SerializedName("createdAt")
    val createdAt: String
)

data class JoinCompanyRes(
    @SerializedName("memberId")
    val memberId: Long,
    @SerializedName("joinCompanyStatus")
    val joinCompanyStatus: String,
    @SerializedName("joinedAt")
    val joinedAt: String
)