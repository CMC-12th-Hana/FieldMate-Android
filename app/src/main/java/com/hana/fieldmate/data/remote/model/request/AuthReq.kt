package com.hana.fieldmate.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class UpdateTokenReq(
    @SerializedName("refreshToken")
    val refreshToken: String
)

data class LoginReq(
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("password")
    val password: String
)

data class JoinReq(
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("passwordCheck")
    val passwordCheck: String
)

data class VerifyMessageReq(
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("authenticationNumber")
    val authenticationNumber: String,
    @SerializedName("messageType")
    val messageType: String
)

data class SendMessageReq(
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("messageType")
    val messageType: String
)