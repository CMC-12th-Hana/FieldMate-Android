package com.hana.fieldmate.domain.usecase

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.model.response.CreateMemberRes
import com.hana.fieldmate.data.remote.model.response.MemberListRes
import com.hana.fieldmate.data.remote.model.response.MemberRes
import com.hana.fieldmate.data.remote.model.response.UpdateProfileRes
import com.hana.fieldmate.data.remote.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    operator fun invoke(
        companyId: Long,
        name: String,
        phoneNumber: String,
        staffRank: String,
        staffNumber: String
    ): Flow<ResultWrapper<CreateMemberRes>> =
        memberRepository.createMember(companyId, name, phoneNumber, staffRank, staffNumber)
}

class FetchProfileByIdUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    operator fun invoke(memberId: Long): Flow<ResultWrapper<MemberRes>> =
        memberRepository.fetchProfileById(memberId)
}

class UpdateProfileUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    operator fun invoke(
        name: String,
        staffNumber: String
    ): Flow<ResultWrapper<UpdateProfileRes>> =
        memberRepository.updateProfile(name, staffNumber)
}

class FetchMemberListUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    operator fun invoke(companyId: Long): Flow<ResultWrapper<MemberListRes>> =
        memberRepository.fetchMemberList(companyId)
}