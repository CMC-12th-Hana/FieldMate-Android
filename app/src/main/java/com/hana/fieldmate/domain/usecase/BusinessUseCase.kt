package com.hana.fieldmate.domain.usecase

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.model.response.BusinessRes
import com.hana.fieldmate.data.remote.model.response.CreateBusinessRes
import com.hana.fieldmate.data.remote.model.response.UpdateBusinessRes
import com.hana.fieldmate.data.remote.repository.BusinessRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class CreateBusinessUseCase @Inject constructor(
    private val businessRepository: BusinessRepository
) {
    operator fun invoke(
        clientId: Long,
        name: String,
        start: LocalDate,
        finish: LocalDate,
        memberIdList: List<Long>,
        revenue: Int,
        description: String
    ): Flow<ResultWrapper<CreateBusinessRes>> =
        businessRepository.createBusiness(
            clientId,
            name,
            start,
            finish,
            memberIdList,
            revenue,
            description
        )
}

class FetchBusinessByIdUseCase @Inject constructor(
    private val businessRepository: BusinessRepository
) {
    operator fun invoke(
        businessId: Long
    ): Flow<ResultWrapper<BusinessRes>> =
        businessRepository.fetchBusinessById(businessId)
}

class UpdateBusinessUseCase @Inject constructor(
    private val businessRepository: BusinessRepository
) {
    operator fun invoke(
        businessId: Long,
        name: String,
        start: LocalDate,
        finish: LocalDate,
        memberIdList: List<Long>,
        revenue: Int,
        description: String
    ): Flow<ResultWrapper<UpdateBusinessRes>> =
        businessRepository.updateBusiness(
            businessId,
            name,
            start,
            finish,
            memberIdList,
            revenue,
            description
        )
}

