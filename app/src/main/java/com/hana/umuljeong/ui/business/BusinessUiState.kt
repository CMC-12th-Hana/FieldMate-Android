package com.hana.umuljeong.ui.business

import com.hana.umuljeong.data.model.Business

data class BusinessUiState(
    val business: Business = Business(0L, "", "", "", emptyList(), "")
)
