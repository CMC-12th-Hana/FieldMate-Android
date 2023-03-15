package com.hana.fieldmate.data.remote.repository

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.datasource.MemberDataSource
import com.hana.fieldmate.data.remote.model.request.CreateMemberReq
import com.hana.fieldmate.data.remote.model.request.UpdateMemberProfileReq
import com.hana.fieldmate.data.remote.model.request.UpdateMyPasswordReq
import com.hana.fieldmate.data.remote.model.request.UpdateMyProfileReq
import com.hana.fieldmate.data.remote.model.response.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MemberRepository @Inject constructor(
    private val memberDataSource: MemberDataSource
) {
    fun createMember(
        companyId: Long,
        name: String,
        phoneNumber: String,
        staffRank: String,
        staffNumber: String
    ): Flow<ResultWrapper<CreateMemberRes>> =
        memberDataSource.createMember(
            companyId, CreateMemberReq(
                name, phoneNumber, staffRank, staffNumber
            )
        )

    fun updateMemberToLeader(memberId: Long): Flow<ResultWrapper<UpdateMemberToLeaderRes>> =
        memberDataSource.updateMemberToLeader(memberId)

    fun fetchProfileById(memberId: Long): Flow<ResultWrapper<MemberRes>> =
        memberDataSource.fetchProfileById(memberId)

    fun updateMemberProfile(
        memberId: Long,
        name: String,
        phoneNumber: String,
        staffRank: String,
        staffNumber: String
    ): Flow<ResultWrapper<UpdateMemberProfileRes>> =
        memberDataSource.updateMemberProfile(
            memberId, UpdateMemberProfileReq(
                name, phoneNumber, staffRank, staffNumber
            )
        )

    fun updateMyProfile(
        name: String,
        staffNumber: String,
        staffRank: String
    ): Flow<ResultWrapper<UpdateMyProfileRes>> =
        memberDataSource.updateMyProfile(UpdateMyProfileReq(name, staffNumber, staffRank))

    fun updateMyPassword(
        password: String,
        passwordCheck: String
    ): Flow<ResultWrapper<UpdateMyPasswordRes>> =
        memberDataSource.updateMyPassword(UpdateMyPasswordReq(password, passwordCheck))

    fun fetchMemberList(
        companyId: Long,
        name: String?
    ): Flow<ResultWrapper<MemberListRes>> =
        memberDataSource.fetchMemberList(companyId, name)

    fun deleteMember(memberId: Long): Flow<ResultWrapper<DeleteMemberRes>> =
        memberDataSource.deleteMember(memberId)
}