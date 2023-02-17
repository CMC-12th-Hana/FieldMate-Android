package com.hana.umuljeong.ui.business

import com.hana.umuljeong.data.model.Business

data class BusinessListUiState(
    val businessList: List<Business> = listOf()
)
