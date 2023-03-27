package com.example.student_assistant.data.network

import com.example.student_assistant.data.network.entity.AddProjectRequest
import com.example.student_assistant.data.network.entity.AddProjectResponse
import com.example.student_assistant.data.network.entity.DeleteProjectRequest
import com.example.student_assistant.data.network.entity.GetProjectResponse
import com.example.student_assistant.data.network.entity.GetProjectsByEmailResponse
import com.example.student_assistant.data.network.entity.GetTagsResponse
import com.example.student_assistant.data.network.entity.GetUserResponse
import com.example.student_assistant.data.network.entity.JoinProjectRequest
import com.example.student_assistant.data.network.entity.LoginRequest
import com.example.student_assistant.data.network.entity.MessageResponse
import com.example.student_assistant.data.network.entity.RegistrationRequest
import com.example.student_assistant.data.network.entity.SearchProjectsRequest
import com.example.student_assistant.data.network.entity.UpdateProjectRequest
import com.example.student_assistant.data.network.entity.UpdateUserRequest
import com.example.student_assistant.data.network.entity.VerificationRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApi {

    @POST("/register")
    suspend fun register(@Body info: RegistrationRequest)

    @POST("/verify-email")
    suspend fun verifyEmail(@Body info: VerificationRequest)

    @POST("/login")
    suspend fun login(@Body info: LoginRequest)

    @GET("/users")
    suspend fun getUser(@Query("email") email: String): GetUserResponse

    @POST("/users")
    suspend fun updateUser(@Query("email") email: String, @Body request: UpdateUserRequest)

    @POST("/projects")
    suspend fun addProject(@Body request: AddProjectRequest): AddProjectResponse

    @PUT("/projects")
    suspend fun updateProject(@Body request: UpdateProjectRequest)

    @DELETE("/projects")
    suspend fun deleteProject(@Query("id") id: Int)

    @GET("/projects/{id}")
    suspend fun getProject(@Path("id") id: Int): GetProjectResponse

    @GET("/projects")
    suspend fun getProjectsByEmail(@Query("email") email: String): GetProjectsByEmailResponse

    @POST("/search-projects")
    suspend fun searchProjects(@Body request: SearchProjectsRequest): GetProjectsByEmailResponse

    @GET("/tags")
    suspend fun getTags(): GetTagsResponse

    @GET("/recommend")
    suspend fun getRecommendedProjects(@Query("email") email: String): GetProjectsByEmailResponse

    @POST("/applications/apply")
    suspend fun joinProject(@Body request: JoinProjectRequest)
}