package com.hana.fieldmate.data.remote.api

import com.hana.fieldmate.data.remote.model.request.*
import com.hana.fieldmate.data.remote.model.response.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/reissue")
    suspend fun updateToken(@Body updateTokenReq: UpdateTokenReq): Response<UpdateTokenRes>

    @POST("/login")
    suspend fun login(@Body loginReq: LoginReq): Result<LoginRes>

    @POST("/join")
    suspend fun join(@Body joinReq: JoinReq): Result<JoinRes>

    @POST("/message/verify")
    suspend fun verifyMessage(@Body verifyMessageReq: VerifyMessageReq): Result<VerifyMessageRes>

    @POST("/message/send")
    suspend fun sendMessage(@Body sendMessageReq: SendMessageReq): Result<SendMessageRes>
}