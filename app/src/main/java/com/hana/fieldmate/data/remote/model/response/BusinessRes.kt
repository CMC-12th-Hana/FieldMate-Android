package com.hana.fieldmate.data.remote.model.response

import com.google.gson.annotations.SerializedName
import com.hana.fieldmate.data.remote.model.request.BusinessPeriod
import java.time.LocalDateTime

data class CreateBusinessRes(
    @SerializedName("businessId")
    val businessId: Long,
    @SerializedName("createdAt")
    val createdAt: LocalDateTime
)

data class UpdateBusinessRes(
    @SerializedName("businessId")
    val businessId: Long,
    @SerializedName("createdAt")
    val createdAt: LocalDateTime
)

data class Member(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String
)

data class BusinessRes(
    @SerializedName("name")
    val name: String,
    @SerializedName("businessPeriodDto")
    val businessPeriod: BusinessPeriod,
    @SerializedName("revenue")
    val revenue: Int,
    @SerializedName("memberDtoList")
    val memberDtoList: List<Member>,
    @SerializedName("description")
    val description: String
)

data class BusinessListRes(
    @SerializedName("businessDtoList")
    val businessDtoList: List<BusinessRes>
)