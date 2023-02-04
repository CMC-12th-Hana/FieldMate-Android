package com.hana.umuljeong

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentTime(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Calendar.getInstance().time)
}