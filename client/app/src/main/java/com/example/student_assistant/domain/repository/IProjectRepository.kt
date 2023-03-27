package com.example.student_assistant.domain.repository

import com.example.student_assistant.domain.entity.Card
import com.example.student_assistant.domain.entity.CreateProjectInfo
import com.example.student_assistant.domain.entity.Project

interface IProjectRepository {

    suspend fun addProject(info: CreateProjectInfo): Result<Int>

    suspend fun updateProject(project: Project): Result<Unit>

    suspend fun deleteProject(id: Int): Result<Unit>

    suspend fun getProject(id: Int): Result<Project>

    suspend fun searchProject(substring: String, projectStatus: String, recStatus: String): Result<List<Card>>

    suspend fun getProjects(): Result<List<Card>>

    suspend fun getTags(): Result<List<String>>

    suspend fun getRecommendedProjects(): Result<List<Card>>

    suspend fun joinProject(projectId: Int): Result<Unit>

    suspend fun isUserTheAuthor(project: Project): Result<Boolean>
}