package com.example.student_assistant.data.network.entity

data class UpdateUserRequest(
    val name: String,
    val surname: String,
    val bio: String,
    val tags: List<String>,
)
