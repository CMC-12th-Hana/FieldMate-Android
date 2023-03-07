package com.hana.fieldmate.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class ProfileRes(
    @SerializedName("companyId")
    val companyId: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("companyName")
    val companyName: String,
    @SerializedName("staffRank")
    val staffRank: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("staffNumber")
    val staffNumber: String
)

data class ProfileListRes(
    @SerializedName("profileDtoList")
    val profileList: List<ProfileRes>
)

data class CreateMemberRes(
    @SerializedName("memberId")
    val memberId: Long,
    @SerializedName("createdAt")
    val createdAt: String
)

data class UpdateProfileRes(
    @SerializedName("memberId")
    val memberId: Long,
    @SerializedName("createdAt")
    val createdAt: String
)