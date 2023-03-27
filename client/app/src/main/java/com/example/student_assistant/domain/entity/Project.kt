package com.example.student_assistant.domain.entity

import com.google.gson.annotations.SerializedName

data class Project(
    val id: Int,
    val authorEmail: String,
    val author: String,
    val title: String,
    val description: String,
    val maxNumberOfStudents: Int,
    val currentNumberOfStudents: Int,
    val recruitingStatus: String,
    val projectStatus: String,
    val applicationsDeadline: String,
    val plannedStartOfWork: String,
    val plannedFinishOfWork: String,
    val tags: List<String>,
)