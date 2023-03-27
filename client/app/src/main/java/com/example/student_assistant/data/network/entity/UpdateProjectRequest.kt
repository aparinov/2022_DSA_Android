package com.example.student_assistant.data.network.entity

import com.google.gson.annotations.SerializedName

data class UpdateProjectRequest(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("recruiting_status")
    val recruitingStatus: String,
    @SerializedName("project_status")
    val projectStatus: String,
    @SerializedName("applications_deadline")
    val applicationsDeadline: String,
    @SerializedName("planned_start_of_work")
    val plannedStartOfWork: String,
    @SerializedName("planned_finish_of_work")
    val plannedFinishOfWork: String,
)
