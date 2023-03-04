package com.hana.umuljeong.data.remote.api

import com.hana.umuljeong.data.remote.model.request.JoinReq
import com.hana.umuljeong.data.remote.model.request.LoginReq
import com.hana.umuljeong.data.remote.model.response.JoinRes
import com.hana.umuljeong.data.remote.model.response.LoginRes
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/login")
    suspend fun login(@Body loginReq: LoginReq): Result<LoginRes>

    @POST("/join")
    suspend fun join(@Body joinReq: JoinReq): Result<JoinRes>
}