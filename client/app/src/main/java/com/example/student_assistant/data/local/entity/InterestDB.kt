package com.example.student_assistant.data.local.entity

import androidx.room.Entity

@Entity(tableName = "interest")
data class InterestDB(
    val id: String,
    val name: String,
)
