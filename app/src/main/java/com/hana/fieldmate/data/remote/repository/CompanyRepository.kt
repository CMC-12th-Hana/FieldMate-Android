package com.hana.fieldmate.data.remote.repository

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.datasource.CompanyDataSource
import com.hana.fieldmate.data.remote.model.request.CreateCompanyReq
import com.hana.fieldmate.data.remote.model.response.CreateCompanyRes
import com.hana.fieldmate.data.remote.model.response.JoinCompanyRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompanyRepository @Inject constructor(
    private val companyDataSource: CompanyDataSource
) {
    fun createCompany(name: String): Flow<ResultWrapper<CreateCompanyRes>> =
        companyDataSource.createCompany(CreateCompanyReq(name))

    fun joinCompany(): Flow<ResultWrapper<JoinCompanyRes>> =
        companyDataSource.joinCompany()
}