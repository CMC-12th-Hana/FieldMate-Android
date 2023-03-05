package com.hana.fieldmate.data.remote.api

import com.hana.fieldmate.data.remote.model.request.CreateBusinessReq
import com.hana.fieldmate.data.remote.model.request.UpdateBusinessReq
import com.hana.fieldmate.data.remote.model.response.BusinessRes
import com.hana.fieldmate.data.remote.model.response.CreateBusinessRes
import com.hana.fieldmate.data.remote.model.response.UpdateBusinessRes
import retrofit2.http.*

interface BusinessService {
    @POST("/company/client/{clientId}/business")
    suspend fun createBusiness(
        @Path("clientId") clientId: Long,
        @Body createBusinessReq: CreateBusinessReq
    ): Result<CreateBusinessRes>

    @GET("/company/client/business/{businessId}")
    suspend fun fetchBusinessById(@Path("businessId") businessId: Long): Result<BusinessRes>

    @PATCH("/company/client/business/{businessId}")
    suspend fun updateBusiness(
        @Path("businessId") businessId: Long,
        updateBusinessReq: UpdateBusinessReq
    ): Result<UpdateBusinessRes>
}