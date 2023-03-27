package com.example.student_assistant.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    private val formatter = SimpleDateFormat("dd MMMM", Locale("ru"))

    fun getMillis(day: Int, month: Int, year: Int): Long {
        return Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DATE, day)
        }.time.time
    }

    fun getCurrentMillis(): Long {
        return Date().time
    }

    fun longToDateString(timestamp: Long?): String? {
        return if (timestamp == null) {
            null
        } else {
            return longToDateString(timestamp)
        }
    }

    fun longToDateString(timestamp: Long): String {
        return formatter.format(Date(timestamp))
    }

    fun stringToLong(string: String): Long {
        return formatter.parse(string)?.time ?: throw IllegalStateException("Wrong date format")
    }

    fun maxDateString(): String {
        return longToDateString(Long.MAX_VALUE)
    }

    fun minDateString(): String {
        return longToDateString(0L)
    }
}