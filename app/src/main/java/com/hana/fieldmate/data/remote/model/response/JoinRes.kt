package com.hana.fieldmate.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class JoinRes(
    @SerializedName("memberId")
    val memberId: Long,
    @SerializedName("accessToken")
    val accessToken: String
)

data class VerifyMessageRes(
    @SerializedName("verifyMessageStatus")
    val verifyMessageStatus: String
)

data class SendMessageRes(
    @SerializedName("sendAt")
    val sendAt: String
)