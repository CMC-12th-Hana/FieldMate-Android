package com.hana.fieldmate.data.remote.repository

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.datasource.UserInfoDataSource
import com.hana.fieldmate.data.remote.model.response.MemberRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserInfoRepository @Inject constructor(
    private val userInfoDataSource: UserInfoDataSource
) {
    fun fetchProfile(): Flow<ResultWrapper<MemberRes>> =
        userInfoDataSource.fetchProfile()
}