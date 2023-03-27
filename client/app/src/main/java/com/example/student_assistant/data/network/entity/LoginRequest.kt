package com.example.student_assistant.data.network.entity

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val login: String,
    @SerializedName("password")
    val password: String,
)
