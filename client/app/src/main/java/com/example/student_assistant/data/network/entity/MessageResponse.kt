package com.example.student_assistant.data.network.entity

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("detail")
    val detail: String,
)
