package com.hana.umuljeong.data.model.request

import com.google.gson.annotations.SerializedName

data class CreateMember(
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("staffRank")
    val staffRank: String,
    @SerializedName("staffNumber")
    val staffNumber: String
)