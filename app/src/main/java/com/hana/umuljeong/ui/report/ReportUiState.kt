package com.hana.umuljeong.ui.report

import com.hana.umuljeong.data.model.Report

data class ReportUiState(
    val report: Report = Report(0L, "", "", "", "", "")
)
