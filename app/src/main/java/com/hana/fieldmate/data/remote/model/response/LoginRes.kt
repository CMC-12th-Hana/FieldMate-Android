package com.hana.fieldmate.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class LoginRes(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)
