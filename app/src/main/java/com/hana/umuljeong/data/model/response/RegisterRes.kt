package com.hana.umuljeong.data.model.response

import com.google.gson.annotations.SerializedName

data class RegisterRes(
    @SerializedName("memberId")
    val memberId: Long,
    @SerializedName("accessToken")
    val accessToken: String
)
