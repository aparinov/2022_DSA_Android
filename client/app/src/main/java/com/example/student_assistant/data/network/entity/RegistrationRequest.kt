package com.example.student_assistant.data.network.entity

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("password")
    val password: String,
)
