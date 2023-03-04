package com.hana.umuljeong.data.remote.api

import com.hana.umuljeong.data.remote.model.request.CreateMemberReq
import com.hana.umuljeong.data.remote.model.request.UpdateProfileReq
import com.hana.umuljeong.data.remote.model.response.CreateMemberRes
import com.hana.umuljeong.data.remote.model.response.ProfileListRes
import com.hana.umuljeong.data.remote.model.response.ProfileRes
import com.hana.umuljeong.data.remote.model.response.UpdateProfileRes
import retrofit2.http.*

interface MemberService {
    @POST("/company/{companyId}/member")
    suspend fun createMember(
        @Path("companyId") companyId: Long,
        createMemberReq: CreateMemberReq
    ): Result<CreateMemberRes>

    @GET("/company/member/profile")
    suspend fun fetchProfile(): Result<ProfileRes>

    @PATCH("/company/member/profile")
    suspend fun updateProfile(@Body updateProfileReq: UpdateProfileReq): Result<UpdateProfileRes>

    @GET("/company/{companyId}/members")
    suspend fun fetchMemberList(@Path("companyId") companyId: Long): Result<ProfileListRes>
}