package com.hana.fieldmate.domain.usecase

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.model.response.*
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

class UpdateMemberToLeaderUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    operator fun invoke(memberId: Long): Flow<ResultWrapper<UpdateMemberToLeaderRes>> =
        memberRepository.updateMemberToLeader(memberId)
}

class FetchProfileByIdUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    operator fun invoke(memberId: Long): Flow<ResultWrapper<MemberRes>> =
        memberRepository.fetchProfileById(memberId)
}

class UpdateMemberProfileUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    operator fun invoke(
        memberId: Long,
        name: String,
        phoneNumber: String,
        staffRank: String,
        staffNumber: String
    ): Flow<ResultWrapper<UpdateMemberProfileRes>> =
        memberRepository.updateMemberProfile(memberId, name, phoneNumber, staffRank, staffNumber)
}

class UpdateMyProfileUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    operator fun invoke(
        name: String,
        staffNumber: String,
        staffRank: String
    ): Flow<ResultWrapper<UpdateMyProfileRes>> =
        memberRepository.updateMyProfile(name, staffNumber, staffRank)
}

class UpdateMyPasswordUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    operator fun invoke(
        password: String,
        passwordCheck: String
    ): Flow<ResultWrapper<UpdateMyPasswordRes>> =
        memberRepository.updateMyPassword(password, passwordCheck)
}

class FetchMemberListUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    operator fun invoke(
        companyId: Long,
        name: String?
    ): Flow<ResultWrapper<MemberListRes>> =
        memberRepository.fetchMemberList(companyId, name)
}

class QuitMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    operator fun invoke(): Flow<ResultWrapper<DeleteMemberRes>> =
        memberRepository.quitMember()
}

class DeleteMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    operator fun invoke(memberId: Long): Flow<ResultWrapper<DeleteMemberRes>> =
        memberRepository.deleteMember(memberId)
}