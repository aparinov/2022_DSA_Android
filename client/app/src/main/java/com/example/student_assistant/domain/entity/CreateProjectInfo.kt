package com.example.student_assistant.domain.entity

data class CreateProjectInfo(
    val title: String,
    val description: String,
    val maxNumberOfStudents: Int,
    val recruitingStatus: String,
    val projectStatus: String,
    val applicationsDeadline: String,
    val plannedStartOfWork: String,
    val plannedFinishOfWork: String,
    val tags: List<String>,
)