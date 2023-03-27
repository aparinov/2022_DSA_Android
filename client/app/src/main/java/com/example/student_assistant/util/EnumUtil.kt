package com.example.student_assistant.util

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.student_assistant.domain.entity.Status

object EnumUtil {
    val query = MutableLiveData<String>()
    fun statusToInt(status: Status): Int = status.ordinal

    fun intToStatus(int: Int): Status = Status.values()[int]

    fun filter(query: String) {
        this.query.value = query
        Log.d("kek", query)
    }
}