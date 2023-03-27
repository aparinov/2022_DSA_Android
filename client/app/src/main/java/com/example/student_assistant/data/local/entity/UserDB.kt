package com.example.student_assistant.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserDB(
    @PrimaryKey
    val email: String,
    @ColumnInfo(name="is_student")
    val isStudent: Boolean?,
    val name: String?,
    val bio: String?,
    val contacts: String?,
)
