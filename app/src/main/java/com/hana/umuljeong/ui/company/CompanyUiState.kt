package com.hana.umuljeong.ui.company

import com.hana.umuljeong.data.model.Company

data class CompanyUiState(
    val company: Company = Company(0L, "", "", "", "", "", 0, 0)
)