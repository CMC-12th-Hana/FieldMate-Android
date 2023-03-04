package com.hana.umuljeong.data.remote.model.response

import com.google.gson.annotations.SerializedName
import com.hana.umuljeong.data.remote.model.request.SalesRepresentative
import java.time.LocalDateTime

data class ClientRes(
    @SerializedName("name")
    val name: String,
    @SerializedName("tel")
    val tel: String,
    @SerializedName("salesRepresentativeDto")
    val salesRepresentative: SalesRepresentative,
    @SerializedName("taskCount")
    val taskCount: Int,
    @SerializedName("businessCount")
    val businessCount: Int
)

data class ClientListRes(
    @SerializedName("clientCompanyDtoList")
    val clientList: List<ClientRes>
)

data class CreateClientRes(
    @SerializedName("clientCompanyId")
    val clientCompanyId: Long,
    @SerializedName("createdAt")
    val createdAt: LocalDateTime
)

data class UpdateClientRes(
    @SerializedName("clientCompanyId")
    val clientCompanyId: Long,
    @SerializedName("updatedAt")
    val updatedAt: LocalDateTime
)