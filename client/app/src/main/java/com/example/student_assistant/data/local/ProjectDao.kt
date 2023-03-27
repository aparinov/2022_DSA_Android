package com.example.student_assistant.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.student_assistant.data.local.entity.ProjectDB

@Dao
interface ProjectDao {

    @Query("SELECT * FROM project")
    suspend fun getAllProjects(): List<ProjectDB>

    @Insert
    suspend fun addProject(project: ProjectDB)

    @Query("SELECT * FROM project WHERE id = :id")
    suspend fun getProjectById(id: String): ProjectDB

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProject(project: ProjectDB)

    @Delete
    suspend fun deleteProject(project: ProjectDB)
}