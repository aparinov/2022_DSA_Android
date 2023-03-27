package com.example.student_assistant.data.network.mapper

import com.example.student_assistant.data.network.entity.AddProjectRequest
import com.example.student_assistant.data.network.entity.AddProjectResponse
import com.example.student_assistant.data.network.entity.GetProjectResponse
import com.example.student_assistant.data.network.entity.GetProjectsByEmailResponse
import com.example.student_assistant.data.network.entity.SearchProjectsRequest
import com.example.student_assistant.data.network.entity.UpdateProjectRequest
import com.example.student_assistant.domain.entity.Card
import com.example.student_assistant.domain.entity.CreateProjectInfo
import com.example.student_assistant.domain.entity.Project
import javax.inject.Inject

class ProjectMapper @Inject constructor() {
    private fun mapStatusToEnum(status: String): String {
        return when (status) {
            "Не начат" -> "NOT_STARTED"
            "Начат" -> "IN_PROGRESS"
            "Завершен" -> "FINISHED"
            else -> throw IllegalStateException()
        }
    }

    private fun mapEnumToStatus(enum: String): String {
        return when (enum) {
            "NOT_STARTED" -> "Не начат"
            "IN_PROGRESS" -> "Начат"
            "FINISHED" -> "Завершен"
            else -> throw IllegalStateException()
        }
    }

    fun map(info: CreateProjectInfo, email: String): AddProjectRequest {
        return AddProjectRequest(
            email,
            info.title,
            info.description,
            info.maxNumberOfStudents,
            mapStatusToEnum(info.recruitingStatus),
            mapStatusToEnum(info.projectStatus),
            info.applicationsDeadline,
            info.plannedStartOfWork,
            info.plannedFinishOfWork,
            info.tags,
        )
    }

    fun map(response: AddProjectResponse): Int {
        return response.id
    }

    fun map(project: Project): UpdateProjectRequest {
        return UpdateProjectRequest(
            project.id,
            project.title,
            project.description,
            mapStatusToEnum(project.recruitingStatus),
            mapStatusToEnum(project.projectStatus),
            project.applicationsDeadline,
            project.plannedStartOfWork,
            project.plannedFinishOfWork,
        )
    }

    fun map(response: GetProjectResponse, id: Int): Project {
        return Project(
            id,
            response.authorEmail,
            response.author,
            response.title,
            response.description,
            response.maxNumberOfStudents,
            response.currentNumberOfStudents,
            mapEnumToStatus(response.recruitingStatus),
            mapEnumToStatus(response.projectStatus),
            response.applicationsDeadline,
            response.plannedStartOfWork,
            response.plannedFinishOfWork,
            response.tags,
        )
    }

    fun map(response: GetProjectsByEmailResponse): List<Card> {
        return response.projects.map { Card(it.id, it.title, it.description, it.status ) }
    }

    fun map(substring: String, projectStatus: String, recStatus: String): SearchProjectsRequest {
        return SearchProjectsRequest(
            substring,
            mapStatusToEnum(projectStatus),
            mapStatusToEnum(recStatus),
        )
    }
}