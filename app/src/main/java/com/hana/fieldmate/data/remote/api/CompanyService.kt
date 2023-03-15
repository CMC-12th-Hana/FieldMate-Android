package com.hana.fieldmate.data.remote.api

import com.hana.fieldmate.data.remote.model.request.CreateCompanyReq
import com.hana.fieldmate.data.remote.model.response.CreateCompanyRes
import com.hana.fieldmate.data.remote.model.response.JoinCompanyRes
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface CompanyService {
    @POST("/company")
    suspend fun createCompany(@Body createCompanyReq: CreateCompanyReq): Result<CreateCompanyRes>

    @PATCH("/company/join")
    suspend fun joinCompany(): Result<JoinCompanyRes>
}