package com.hana.umuljeong.data.remote.repository

import com.hana.umuljeong.data.ResultWrapper
import com.hana.umuljeong.data.remote.datasource.MemberDataSource
import com.hana.umuljeong.data.remote.model.request.CreateMemberReq
import com.hana.umuljeong.data.remote.model.request.UpdateProfileReq
import com.hana.umuljeong.data.remote.model.response.CreateMemberRes
import com.hana.umuljeong.data.remote.model.response.ProfileListRes
import com.hana.umuljeong.data.remote.model.response.ProfileRes
import com.hana.umuljeong.data.remote.model.response.UpdateProfileRes
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

    fun fetchProfile(): Flow<ResultWrapper<ProfileRes>> =
        memberDataSource.fetchProfile()

    fun updateProfile(
        name: String,
        staffNumber: String
    ): Flow<ResultWrapper<UpdateProfileRes>> =
        memberDataSource.updateProfile(UpdateProfileReq(name, staffNumber))

    fun fetchMemberList(companyId: Long): Flow<ResultWrapper<ProfileListRes>> =
        memberDataSource.fetchMemberList(companyId)
}