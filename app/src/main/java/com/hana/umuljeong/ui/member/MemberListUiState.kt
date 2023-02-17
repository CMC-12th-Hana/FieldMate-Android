package com.hana.umuljeong.ui.member

import com.hana.umuljeong.data.model.Member

data class MemberListUiState(
    val memberList: List<Member> = listOf()
)
