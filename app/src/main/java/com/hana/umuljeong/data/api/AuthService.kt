package com.hana.umuljeong.data.api

import com.hana.umuljeong.data.model.request.JoinReq
import com.hana.umuljeong.data.model.request.LoginReq
import com.hana.umuljeong.data.model.response.JoinRes
import com.hana.umuljeong.data.model.response.LoginRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/login")
    suspend fun login(@Body loginReq: LoginReq): Response<LoginRes>

    @POST("/join")
    suspend fun join(@Body joinReq: JoinReq): Response<JoinRes>
}