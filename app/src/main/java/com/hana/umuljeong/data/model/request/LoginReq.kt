package com.hana.umuljeong.data.model.request

import com.google.gson.annotations.SerializedName

data class LoginReq(
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("password")
    val password: String
)