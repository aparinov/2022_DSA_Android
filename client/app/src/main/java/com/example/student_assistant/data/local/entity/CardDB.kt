package com.example.student_assistant.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "card",
    foreignKeys = [
        ForeignKey(
            entity = ProjectDB::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("project_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CardDB(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "project_id") val projectId: String,
    @ColumnInfo(name = "creator_id") val creatorId: String,
    val link: String
)
