package com.hana.fieldmate.data.remote.repository

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.datasource.BusinessDataSource
import com.hana.fieldmate.data.remote.model.request.BusinessPeriod
import com.hana.fieldmate.data.remote.model.request.CreateBusinessReq
import com.hana.fieldmate.data.remote.model.request.UpdateBusinessReq
import com.hana.fieldmate.data.remote.model.response.BusinessRes
import com.hana.fieldmate.data.remote.model.response.CreateBusinessRes
import com.hana.fieldmate.data.remote.model.response.UpdateBusinessRes
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class BusinessRepository @Inject constructor(
    private val businessDataSource: BusinessDataSource
) {
    fun createBusiness(
        clientId: Long,
        name: String,
        start: LocalDate,
        finish: LocalDate,
        memberIdList: List<Long>,
        revenue: Int,
        description: String
    ): Flow<ResultWrapper<CreateBusinessRes>> =
        businessDataSource.createBusiness(
            clientId, CreateBusinessReq(
                name, BusinessPeriod(start, finish), memberIdList, revenue, description
            )
        )

    fun fetchBusinessById(businessId: Long): Flow<ResultWrapper<BusinessRes>> =
        businessDataSource.fetchBusinessById(businessId)

    fun updateBusiness(
        businessId: Long,
        name: String,
        start: LocalDate,
        finish: LocalDate,
        memberIdList: List<Long>,
        revenue: Int,
        description: String
    ): Flow<ResultWrapper<UpdateBusinessRes>> =
        businessDataSource.updateBusiness(
            businessId, UpdateBusinessReq(
                name, BusinessPeriod(start, finish), memberIdList, revenue, description
            )
        )
}