package com.hana.umuljeong.data.model.response

import com.google.gson.annotations.SerializedName

data class JoinRes(
    @SerializedName("memberId")
    val memberId: Long,
    @SerializedName("accessToken")
    val accessToken: String
)
