package com.hana.umuljeong.data.remote.api

import com.hana.umuljeong.data.remote.model.request.CreateCompanyReq
import com.hana.umuljeong.data.remote.model.response.CreateCompanyRes
import com.hana.umuljeong.data.remote.model.response.JoinCompanyRes
import retrofit2.http.Body
import retrofit2.http.POST

interface CompanyService {
    @POST("/company")
    suspend fun createCompany(@Body createCompanyReq: CreateCompanyReq): Result<CreateCompanyRes>

    @POST("/company/join")
    suspend fun joinCompany(): Result<JoinCompanyRes>
}