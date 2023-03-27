package com.example.student_assistant.data.network.entity

import com.google.gson.annotations.SerializedName

data class DeleteProjectRequest(
    @SerializedName("id")
    val id: Int,
)