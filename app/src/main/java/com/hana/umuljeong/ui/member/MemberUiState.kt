package com.hana.umuljeong.ui.member

import com.hana.umuljeong.R
import com.hana.umuljeong.data.model.Member

data class MemberUiState(
    val member: Member = Member(0L, R.drawable.ic_member_profile, "", "", "", "", "")
)