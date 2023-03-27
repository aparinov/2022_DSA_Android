package com.example.student_assistant.data.network.entity

import com.google.gson.annotations.SerializedName

data class GetProjectsByEmailResponseItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("status_string")
    val status: String,
)