package com.example.student_assistant.data.network.entity

import com.google.gson.annotations.SerializedName

data class GetProjectsByEmailResponse(
    @SerializedName("projects")
    val projects: List<GetProjectsByEmailResponseItem>
)
