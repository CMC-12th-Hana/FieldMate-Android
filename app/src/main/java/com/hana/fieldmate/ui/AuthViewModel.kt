package com.hana.fieldmate.ui

import androidx.lifecycle.ViewModel
import com.hana.fieldmate.App
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {
    fun fetchUserInfo() {
        runBlocking {
            userInfoRepository.fetchProfile()
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { user ->
                            App.getInstance().getDataStore().saveUserInfo(
                                user.companyId,
                                user.memberId,
                                user.companyName,
                                user.name,
                                user.role
                            )
                            App.getInstance().updateUserInfo()
                        }
                    }
                }
        }
    }
}