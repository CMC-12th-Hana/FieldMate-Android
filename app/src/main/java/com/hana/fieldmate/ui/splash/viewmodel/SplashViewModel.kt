package com.hana.fieldmate.ui.splash.viewmodel

import androidx.lifecycle.ViewModel
import com.hana.fieldmate.App
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.usecase.FetchUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val fetchUserInfoUseCase: FetchUserInfoUseCase
) : ViewModel() {

    fun fetchUserInfo() {
        runBlocking {
            fetchUserInfoUseCase()
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { user ->
                            App.getInstance().getDataStore().saveUserInfo(
                                user.companyId,
                                user.memberId,
                                user.companyName,
                                user.joinCompanyStatus,
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