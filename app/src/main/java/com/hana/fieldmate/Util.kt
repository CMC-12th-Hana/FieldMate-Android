package com.hana.fieldmate

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

object StringUtil {
    fun String.toColor(): Color {
        return Color(android.graphics.Color.parseColor("#${this}"))
    }

    fun Color.toShortenString(): String {
        return Integer.toHexString(this.toArgb()).substring(2)
    }

    fun String.toFormattedPhoneNum(): String {
        val list = this.split('-')
        val phoneNum = list.joinToString(separator = "", limit = 3)

        return "tel:$phoneNum"
    }


    fun isValidString(str: String, regEx: String): Boolean {
        return str.matches(regEx.toRegex())
    }
}
