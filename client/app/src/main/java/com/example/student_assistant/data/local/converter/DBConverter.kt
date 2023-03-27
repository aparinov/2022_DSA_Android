package com.example.student_assistant.data.local.converter

import androidx.room.TypeConverter
import com.example.student_assistant.data.local.entity.InterestDB
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object DBConverter {
    @TypeConverter
    fun fromStringToInterestList(value: String): List<InterestDB> {
        return Gson().fromJson(value, object : TypeToken<List<InterestDB>>() {}.type)
    }

    @TypeConverter
    fun fromInterestListToString(list: List<InterestDB>): String {
        return Gson().toJson(list)
    }
}