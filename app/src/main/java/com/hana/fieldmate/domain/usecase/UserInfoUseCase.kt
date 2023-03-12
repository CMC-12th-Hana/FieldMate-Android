package com.hana.fieldmate.domain.usecase

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.model.response.MemberRes
import com.hana.fieldmate.data.remote.repository.UserInfoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchUserInfoUseCase @Inject constructor(
    private val userInfoRepository: UserInfoRepository
) {
    operator fun invoke(): Flow<ResultWrapper<MemberRes>> =
        userInfoRepository.fetchUserInfo()
}