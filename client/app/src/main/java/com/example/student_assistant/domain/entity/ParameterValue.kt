package com.example.student_assistant.domain.entity

data class ParameterValue(
    val id: Int,
    val value: String,
    val isSelected: Boolean,
    val page: Int,
)