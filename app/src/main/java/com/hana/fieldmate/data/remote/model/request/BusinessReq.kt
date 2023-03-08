package com.hana.fieldmate.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class CreateBusinessReq(
    @SerializedName("name")
    val name: String,
    @SerializedName("businessPeriodDto")
    val businessPeriod: BusinessPeriod,
    @SerializedName("memberIdList")
    val memberIdList: List<Long>,
    @SerializedName("revenue")
    val revenue: Int,
    @SerializedName("description")
    val description: String
)

data class UpdateBusinessReq(
    @SerializedName("name")
    val name: String,
    @SerializedName("businessPeriodDto")
    val businessPeriod: BusinessPeriod,
    @SerializedName("memberIdList")
    val memberIdList: List<Long>,
    @SerializedName("revenue")
    val revenue: Int,
    @SerializedName("description")
    val description: String
)

data class BusinessPeriod(
    @SerializedName("start")
    val start: String,
    @SerializedName("finish")
    val finish: String
)
