package com.hana.fieldmate.data.remote.repository

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.datasource.MemberDataSource
import com.hana.fieldmate.data.remote.model.request.CreateMemberReq
import com.hana.fieldmate.data.remote.model.request.UpdateProfileReq
import com.hana.fieldmate.data.remote.model.response.CreateMemberRes
import com.hana.fieldmate.data.remote.model.response.MemberListRes
import com.hana.fieldmate.data.remote.model.response.MemberRes
import com.hana.fieldmate.data.remote.model.response.UpdateProfileRes
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

    fun fetchMember(): Flow<ResultWrapper<MemberRes>> =
        memberDataSource.fetchMember()

    fun updateProfile(
        name: String,
        staffNumber: String
    ): Flow<ResultWrapper<UpdateProfileRes>> =
        memberDataSource.updateProfile(UpdateProfileReq(name, staffNumber))

    fun fetchMemberList(companyId: Long): Flow<ResultWrapper<MemberListRes>> =
        memberDataSource.fetchMemberList(companyId)
}