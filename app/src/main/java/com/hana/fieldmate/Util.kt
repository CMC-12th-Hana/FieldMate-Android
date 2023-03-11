package com.hana.fieldmate

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


fun getCurrentTime(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Calendar.getInstance().time)
}

fun LocalDate.getFormattedTime(): String {
    return this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

fun LocalDate.getShortenFormattedTime(): String {
    return this.format(DateTimeFormatter.ofPattern("uu. MM. dd"))
}

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

fun String.toLocalDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

fun getRealPathFromURI(context: Context, uri: Uri): String {
    val buildName = Build.MANUFACTURER
    if (buildName.equals("Xiamoi")) {
        return uri.path!!
    }
    var columnIndex = 0
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(uri, proj, null, null, null)
    if (cursor!!.moveToFirst()) {
        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    }
    val result = cursor.getString(columnIndex)
    cursor.close()

    return result
}

fun isValidString(str: String, regEx: String): Boolean {
    return str.matches(regEx.toRegex())
}

fun getFormattedTime(seconds: Int): String {
    val minute = seconds / 60
    val second = seconds % 60

    return String.format("%02d : %02d", minute, second)
}