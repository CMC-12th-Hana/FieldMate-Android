package com.hana.fieldmate.data.remote.api

import com.hana.fieldmate.data.remote.model.request.CreateMemberReq
import com.hana.fieldmate.data.remote.model.request.UpdateProfileReq
import com.hana.fieldmate.data.remote.model.response.CreateMemberRes
import com.hana.fieldmate.data.remote.model.response.MemberListRes
import com.hana.fieldmate.data.remote.model.response.MemberRes
import com.hana.fieldmate.data.remote.model.response.UpdateProfileRes
import retrofit2.http.*

interface MemberService {
    @POST("/company/{companyId}/member")
    suspend fun createMember(
        @Path("companyId") companyId: Long,
        @Body createMemberReq: CreateMemberReq
    ): Result<CreateMemberRes>

    @GET("/company/member/profile")
    suspend fun fetchProfile(): Result<MemberRes>

    @GET("/company/member/{memberId}/profile")
    suspend fun fetchProfileById(@Path("memberId") memberId: Long): Result<MemberRes>

    @PATCH("/company/member/profile")
    suspend fun updateProfile(@Body updateProfileReq: UpdateProfileReq): Result<UpdateProfileRes>

    @GET("/company/{companyId}/members")
    suspend fun fetchMemberList(@Path("companyId") companyId: Long): Result<MemberListRes>
}