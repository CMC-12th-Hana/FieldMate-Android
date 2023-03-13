package com.hana.fieldmate.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class CreateMemberReq(
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("staffRank")
    val staffRank: String,
    @SerializedName("staffNumber")
    val staffNumber: String
)

data class UpdateMemberProfileReq(
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("staffRank")
    val staffRank: String,
    @SerializedName("staffNumber")
    val staffNumber: String
)

data class UpdateMyProfileReq(
    @SerializedName("name")
    val name: String,
    @SerializedName("staffNumber")
    val staffNumber: String,
    @SerializedName("staffRank")
    val staffRank: String
)

data class UpdateMyPasswordReq(
    @SerializedName("password")
    val password: String,
    @SerializedName("passwordCheck")
    val passwordCheck: String
)