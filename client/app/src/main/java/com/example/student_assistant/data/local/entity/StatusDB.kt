package com.example.student_assistant.data.local.entity

import androidx.room.Entity

@Entity(tableName = "status")
data class StatusDB(
    val id: String,
    val name: String,
)
