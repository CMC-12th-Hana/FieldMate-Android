package com.hana.fieldmate.data.remote.api

import com.hana.fieldmate.data.remote.model.request.CreateMemberReq
import com.hana.fieldmate.data.remote.model.request.UpdateMemberProfileReq
import com.hana.fieldmate.data.remote.model.request.UpdateMyPasswordReq
import com.hana.fieldmate.data.remote.model.request.UpdateMyProfileReq
import com.hana.fieldmate.data.remote.model.response.*
import retrofit2.http.*

interface MemberService {
    @POST("/company/{companyId}/member")
    suspend fun createMember(
        @Path("companyId") companyId: Long,
        @Body createMemberReq: CreateMemberReq
    ): Result<CreateMemberRes>

    @PATCH("/company/member/{memberId}/role")
    suspend fun updateMemberToLeader(@Path("memberId") memberId: Long): Result<UpdateMemberToLeaderRes>

    @GET("/company/member/{memberId}/profile")
    suspend fun fetchProfileById(@Path("memberId") memberId: Long): Result<MemberRes>

    @PATCH("/company/member/{memberId}/profile")
    suspend fun updateMemberProfile(
        @Path("memberId") memberId: Long,
        @Body updateMemberProfileReq: UpdateMemberProfileReq
    ): Result<UpdateMemberProfileRes>

    @PATCH("/company/member/profile")
    suspend fun updateMyProfile(@Body updateMyProfileReq: UpdateMyProfileReq): Result<UpdateMyProfileRes>

    @PATCH("/company/member/password")
    suspend fun updateMyPassword(@Body updateMyPasswordReq: UpdateMyPasswordReq): Result<UpdateMyPasswordRes>

    @GET("/company/{companyId}/members")
    suspend fun fetchMemberList(
        @Path("companyId") companyId: Long,
        @Query("name") name: String?
    ): Result<MemberListRes>

    @DELETE("/company/member")
    suspend fun quitMember(): Result<DeleteMemberRes>

    @DELETE("/company/member/{memberId}")
    suspend fun deleteMember(@Path("memberId") memberId: Long): Result<DeleteMemberRes>
}