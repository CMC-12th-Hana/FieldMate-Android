package com.hana.fieldmate.data.remote.model.response

import com.google.gson.annotations.SerializedName
import com.hana.fieldmate.data.remote.model.request.BusinessPeriod

data class CreateBusinessRes(
    @SerializedName("businessId")
    val businessId: Long,
    @SerializedName("createdAt")
    val createdAt: String
)

data class UpdateBusinessRes(
    @SerializedName("businessId")
    val businessId: Long,
    @SerializedName("createdAt")
    val createdAt: String
)

data class DeleteBusinessRes(
    @SerializedName("deletedAt")
    val deletedAt: String
)

data class MemberNameRes(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String
)

data class BusinessRes(
    @SerializedName("businessId")
    val businessId: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("clientName")
    val clientName: String,
    @SerializedName("businessPeriodDto")
    val businessPeriod: BusinessPeriod,
    @SerializedName("revenue")
    val revenue: Long,
    @SerializedName("memberDtoList")
    val memberDtoList: List<MemberNameRes>,
    @SerializedName("description")
    val description: String
)

data class BusinessListRes(
    @SerializedName("businessDtoList")
    val businessDtoList: List<BusinessRes>
)