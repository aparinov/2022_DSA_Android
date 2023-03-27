package com.example.student_assistant.domain.entity

data class UserInfo(
    val isStudent: Boolean,
    val name: String,
    val bio: String,
    val contacts: String,
    val tags: List<String>,
)
