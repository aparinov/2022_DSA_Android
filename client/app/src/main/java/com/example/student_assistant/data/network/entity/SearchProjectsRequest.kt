package com.example.student_assistant.data.network.entity

import com.google.gson.annotations.SerializedName

data class SearchProjectsRequest(
    @SerializedName("search_string")
    val searchString: String,
    @SerializedName("project_status")
    val projectStatus: String,
    @SerializedName("recruiting_status")
    val recruitingStatus: String,
)
