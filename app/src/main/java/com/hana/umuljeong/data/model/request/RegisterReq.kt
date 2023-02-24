package com.hana.umuljeong.data.model.request

import com.google.gson.annotations.SerializedName

data class RegisterReq(
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("passwordCheck")
    val passwordCheck: String
)
