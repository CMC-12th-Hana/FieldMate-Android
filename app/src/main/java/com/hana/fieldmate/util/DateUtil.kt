package com.hana.fieldmate.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtil {
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

    fun String.toLocalDate(): LocalDate {
        return LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    fun getFormattedTime(seconds: Int): String {
        val minute = seconds / 60
        val second = seconds % 60

        return String.format("%02d : %02d", minute, second)
    }
}