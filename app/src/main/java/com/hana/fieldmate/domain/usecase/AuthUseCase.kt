package com.hana.fieldmate.domain.usecase

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.model.request.MessageType
import com.hana.fieldmate.data.remote.model.response.JoinRes
import com.hana.fieldmate.data.remote.model.response.LoginRes
import com.hana.fieldmate.data.remote.model.response.SendMessageRes
import com.hana.fieldmate.data.remote.model.response.VerifyMessageRes
import com.hana.fieldmate.data.remote.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(phoneNumber: String, password: String): Flow<ResultWrapper<LoginRes>> =
        authRepository.login(phoneNumber, password)
}

class JoinUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(
        name: String,
        phoneNumber: String,
        password: String,
        passwordCheck: String
    ): Flow<ResultWrapper<JoinRes>> =
        authRepository.join(name, phoneNumber, password, passwordCheck)
}

class VerifyMessageUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(
        phoneNumber: String,
        authenticationNumber: String,
        messageType: MessageType
    ): Flow<ResultWrapper<VerifyMessageRes>> =
        authRepository.verifyMessage(phoneNumber, authenticationNumber, messageType)
}

class SendMessageUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(
        phoneNumber: String,
        messageType: MessageType
    ): Flow<ResultWrapper<SendMessageRes>> =
        authRepository.sendMessage(phoneNumber, messageType)
}