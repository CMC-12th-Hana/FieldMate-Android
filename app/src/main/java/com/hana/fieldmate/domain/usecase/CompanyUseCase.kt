package com.hana.fieldmate.domain.usecase

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.model.response.CreateCompanyRes
import com.hana.fieldmate.data.remote.model.response.JoinCompanyRes
import com.hana.fieldmate.data.remote.repository.CompanyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateCompanyUseCase @Inject constructor(
    private val companyRepository: CompanyRepository
) {
    operator fun invoke(name: String): Flow<ResultWrapper<CreateCompanyRes>> =
        companyRepository.createCompany(name)
}

class JoinCompanyUseCase @Inject constructor(
    private val companyRepository: CompanyRepository
) {
    operator fun invoke(): Flow<ResultWrapper<JoinCompanyRes>> =
        companyRepository.joinCompany()
}