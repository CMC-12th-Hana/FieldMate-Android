package com.hana.fieldmate.data.remote.repository

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.datasource.BusinessDataSource
import com.hana.fieldmate.data.remote.model.request.BusinessPeriod
import com.hana.fieldmate.data.remote.model.request.CreateBusinessReq
import com.hana.fieldmate.data.remote.model.request.UpdateBusinessReq
import com.hana.fieldmate.data.remote.model.response.*
import com.hana.fieldmate.util.DateUtil.getFormattedTime
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
                name,
                BusinessPeriod(start.getFormattedTime(), finish.getFormattedTime()),
                memberIdList,
                revenue,
                description
            )
        )

    fun fetchBusinessById(businessId: Long): Flow<ResultWrapper<BusinessRes>> =
        businessDataSource.fetchBusinessById(businessId)

    fun fetchBusinessListByClientId(
        clientId: Long,
        name: String?,
        start: String?,
        finish: String?
    ): Flow<ResultWrapper<BusinessListRes>> =
        businessDataSource.fetchBusinessListByClientId(clientId, name, start, finish)

    fun fetchBusinessList(
        businessId: Long,
        name: String?,
        start: String?,
        finish: String?
    ): Flow<ResultWrapper<BusinessListRes>> =
        businessDataSource.fetchBusinessList(businessId, name, start, finish)

    fun deletedBusiness(businessId: Long): Flow<ResultWrapper<DeleteBusinessRes>> =
        businessDataSource.deletedBusiness(businessId)

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
                name,
                BusinessPeriod(start.getFormattedTime(), finish.getFormattedTime()),
                memberIdList,
                revenue,
                description
            )
        )
}