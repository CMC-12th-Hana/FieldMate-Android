package com.hana.fieldmate.data.remote.api

import com.hana.fieldmate.data.remote.model.response.MemberRes
import retrofit2.http.GET

interface UserInfoService {
    @GET("/company/member/profile")
    suspend fun fetchProfile(): Result<MemberRes>
}