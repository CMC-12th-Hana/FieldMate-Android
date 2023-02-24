package com.hana.umuljeong.data.model.request

import com.google.gson.annotations.SerializedName

data class SalesRepresentative(
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("department")
    val department: String
)

data class CreateClientReq(
    @SerializedName("name")
    val name: String,
    @SerializedName("tel")
    val tel: String,
    @SerializedName("salesRepresentativeDto")
    val salesRepresentative: SalesRepresentative
)

data class UpdateClientReq(
    @SerializedName("name")
    val name: String,
    @SerializedName("tel")
    val tel: String,
    @SerializedName("salesRepresentativeDto")
    val salesRepresentative: SalesRepresentative
)
