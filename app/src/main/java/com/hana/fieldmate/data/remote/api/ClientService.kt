package com.hana.fieldmate.data.remote.api

import com.hana.fieldmate.data.remote.model.request.CreateClientReq
import com.hana.fieldmate.data.remote.model.request.UpdateClientReq
import com.hana.fieldmate.data.remote.model.response.ClientListRes
import com.hana.fieldmate.data.remote.model.response.ClientRes
import com.hana.fieldmate.data.remote.model.response.CreateClientRes
import com.hana.fieldmate.data.remote.model.response.UpdateClientRes
import retrofit2.http.*

interface ClientService {
    @POST("/company/{companyId}/client")
    suspend fun createClient(
        @Path("companyId") companyId: Long,
        @Body createClientReq: CreateClientReq
    ): Result<CreateClientRes>

    @GET("/company/client/{clientId}")
    suspend fun fetchClientById(@Path("clientId") clientId: Long): Result<ClientRes>

    @PATCH("/company/client/{clientId}")
    suspend fun updateClient(
        @Path("clientId") clientId: Long,
        @Body updateClientReq: UpdateClientReq
    ): Result<UpdateClientRes>

    @GET("/company/{companyId}/clients")
    suspend fun fetchClientList(@Path("companyId") companyId: Long): Result<ClientListRes>
}