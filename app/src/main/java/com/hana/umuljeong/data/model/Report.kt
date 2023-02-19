package com.hana.umuljeong.data.model

import com.hana.umuljeong.getCurrentTime
import com.hana.umuljeong.ui.component.imagepicker.ImageInfo

data class Report(
    val id: Long,
    val customer: String,
    val name: String,
    val category: String,
    val date: String = getCurrentTime(),
    val content: String,
    val images: List<ImageInfo> = emptyList()
)
