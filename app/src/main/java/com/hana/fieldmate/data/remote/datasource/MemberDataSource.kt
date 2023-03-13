package com.hana.fieldmate.data.remote.datasource

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.api.MemberService
import com.hana.fieldmate.data.remote.model.request.CreateMemberReq
import com.hana.fieldmate.data.remote.model.request.UpdateMemberProfileReq
import com.hana.fieldmate.data.remote.model.request.UpdateMyPasswordReq
import com.hana.fieldmate.data.remote.model.request.UpdateMyProfileReq
import com.hana.fieldmate.data.remote.model.response.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MemberDataSource @Inject constructor(
    private val memberService: MemberService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun createMember(
        companyId: Long, createMemberReq: CreateMemberReq
    ): Flow<ResultWrapper<CreateMemberRes>> = flow {
        memberService.createMember(companyId, createMemberReq).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun updateMemberToLeader(memberId: Long): Flow<ResultWrapper<UpdateMemberToLeaderRes>> = flow {
        memberService.updateMemberToLeader(memberId).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun fetchProfileById(memberId: Long): Flow<ResultWrapper<MemberRes>> = flow {
        memberService.fetchProfileById(memberId).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun updateMemberProfile(
        memberId: Long,
        updateMemberProfileReq: UpdateMemberProfileReq
    ): Flow<ResultWrapper<UpdateMemberProfileRes>> = flow {
        memberService.updateMemberProfile(memberId, updateMemberProfileReq).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun updateMyProfile(updateMyProfileReq: UpdateMyProfileReq): Flow<ResultWrapper<UpdateMyProfileRes>> =
        flow {
            memberService.updateMyProfile(updateMyProfileReq).onSuccess {
                emit(ResultWrapper.Success(it))
            }.onFailure {
                emit(ResultWrapper.Error(it.message!!))
            }
        }.flowOn(ioDispatcher)

    fun updateMyPassword(updateMyPasswordReq: UpdateMyPasswordReq): Flow<ResultWrapper<UpdateMyPasswordRes>> =
        flow {
            memberService.updateMyPassword(updateMyPasswordReq).onSuccess {
                emit(ResultWrapper.Success(it))
            }.onFailure {
                emit(ResultWrapper.Error(it.message!!))
            }
        }.flowOn(ioDispatcher)

    fun fetchMemberList(companyId: Long): Flow<ResultWrapper<MemberListRes>> = flow {
        memberService.fetchMemberList(companyId).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun deleteMember(memberId: Long): Flow<ResultWrapper<DeleteMemberRes>> = flow {
        memberService.deleteMember(memberId).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)
}