package com.hana.fieldmate.data.remote.api

import com.hana.fieldmate.data.remote.model.request.JoinReq
import com.hana.fieldmate.data.remote.model.request.LoginReq
import com.hana.fieldmate.data.remote.model.request.SendMessageReq
import com.hana.fieldmate.data.remote.model.request.VerifyMessageReq
import com.hana.fieldmate.data.remote.model.response.JoinRes
import com.hana.fieldmate.data.remote.model.response.LoginRes
import com.hana.fieldmate.data.remote.model.response.SendMessageRes
import com.hana.fieldmate.data.remote.model.response.VerifyMessageRes
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/login")
    suspend fun login(@Body loginReq: LoginReq): Result<LoginRes>

    @POST("/join")
    suspend fun join(@Body joinReq: JoinReq): Result<JoinRes>

    @POST("/verify/message")
    suspend fun verifyMessage(@Body verifyMessageReq: VerifyMessageReq): Result<VerifyMessageRes>

    @POST("/send/message")
    suspend fun sendMessage(@Body sendMessageReq: SendMessageReq): Result<SendMessageRes>
}