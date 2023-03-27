package com.example.student_assistant.data.network.entity

import com.google.gson.annotations.SerializedName

data class JoinProjectRequest(
    @SerializedName("project_id")
    val projectId: Int,
    @SerializedName("email")
    val email: String,
)