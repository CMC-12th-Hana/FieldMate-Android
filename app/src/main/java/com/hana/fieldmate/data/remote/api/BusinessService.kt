package com.hana.fieldmate.data.remote.api

import com.hana.fieldmate.data.remote.model.request.CreateBusinessReq
import com.hana.fieldmate.data.remote.model.request.UpdateBusinessReq
import com.hana.fieldmate.data.remote.model.response.*
import retrofit2.http.*

interface BusinessService {
    @POST("/company/client/{clientId}/business")
    suspend fun createBusiness(
        @Path("clientId") clientId: Long,
        @Body createBusinessReq: CreateBusinessReq
    ): Result<CreateBusinessRes>

    @GET("/company/client/business/{businessId}")
    suspend fun fetchBusinessById(@Path("businessId") businessId: Long): Result<BusinessRes>

    @GET("/company/client/{clientId}/businesses")
    suspend fun fetchBusinessListByClientId(
        @Path("clientId") companyId: Long,
        @Query("name") name: String?,
        @Query("start") start: String?,
        @Query("finish") finish: String?
    ): Result<BusinessListRes>

    @GET("/company/{companyId}/client/businesses")
    suspend fun fetchBusinessList(
        @Path("companyId") companyId: Long,
        @Query("name") name: String?,
        @Query("start") start: String?,
        @Query("finish") finish: String?
    ): Result<BusinessListRes>

    @DELETE("/company/client/business/{businessId}")
    suspend fun deleteBusiness(@Path("businessId") businessId: Long): Result<DeleteBusinessRes>

    @PATCH("/company/client/business/{businessId}")
    suspend fun updateBusiness(
        @Path("businessId") businessId: Long,
        @Body updateBusinessReq: UpdateBusinessReq
    ): Result<UpdateBusinessRes>
}