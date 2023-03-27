package com.example.student_assistant.domain.entity

data class Parameter(
    val name: String,
    val values: List<String>,
    val chosen: MutableSet<Int>,
    val page: Int,
)
