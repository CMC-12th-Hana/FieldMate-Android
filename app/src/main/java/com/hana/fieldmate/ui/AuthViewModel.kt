package com.hana.fieldmate.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.repository.AuthRepository
import com.hana.fieldmate.data.remote.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

data class UserInfo(
    val companyId: Long = -1L,
    val companyName: String = "",
    val userName: String = "",
    val userRole: String = "",
    val isLoggedIn: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _userInfo = MutableStateFlow(UserInfo())
    val userInfo: StateFlow<UserInfo> = _userInfo.asStateFlow()

    init {
        runBlocking {
            _userInfo.update {
                it.copy(isLoggedIn = authRepository.getIsLoggedIn().first())
            }
        }
    }

    fun loadMyProfile() {
        viewModelScope.launch {
            memberRepository.fetchMember()
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { memberRes ->
                            _userInfo.update {
                                it.copy(
                                    companyId = memberRes.companyId,
                                    companyName = memberRes.companyName,
                                    userName = memberRes.name,
                                    userRole = memberRes.role
                                )
                            }
                        }
                    } else {
                        // TODO: 에러처리
                    }
                }
        }
    }
}