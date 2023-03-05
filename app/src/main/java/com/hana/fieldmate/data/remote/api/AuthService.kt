package com.hana.fieldmate.data.remote.api

import com.hana.fieldmate.data.remote.model.request.JoinReq
import com.hana.fieldmate.data.remote.model.request.LoginReq
import com.hana.fieldmate.data.remote.model.response.JoinRes
import com.hana.fieldmate.data.remote.model.response.LoginRes
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/login")
    suspend fun login(@Body loginReq: LoginReq): Result<LoginRes>

    @POST("/join")
    suspend fun join(@Body joinReq: JoinReq): Result<JoinRes>
}