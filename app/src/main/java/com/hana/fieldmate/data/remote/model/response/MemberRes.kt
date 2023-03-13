package com.hana.fieldmate.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class MemberRes(
    @SerializedName("companyId")
    val companyId: Long,
    @SerializedName("memberId")
    val memberId: Long,
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

data class MemberListRes(
    @SerializedName("profileDtoList")
    val memberList: List<MemberRes>
)

data class UpdateMemberToLeaderRes(
    @SerializedName("memberId")
    val memberId: Long,
    @SerializedName("updatedAt")
    val updatedAt: String
)

data class UpdateMyPasswordRes(
    @SerializedName("memberId")
    val memberId: Long,
    @SerializedName("updatedAt")
    val updatedAt: String
)

data class CreateMemberRes(
    @SerializedName("memberId")
    val memberId: Long,
    @SerializedName("createdAt")
    val createdAt: String
)

data class UpdateMyProfileRes(
    @SerializedName("memberId")
    val memberId: Long,
    @SerializedName("updatedAt")
    val updatedAt: String
)

data class UpdateMemberProfileRes(
    @SerializedName("memberId")
    val memberId: Long,
    @SerializedName("updatedAt")
    val updatedAt: String
)

data class DeleteMemberRes(
    @SerializedName("deletedAt")
    val deletedAt: String
)