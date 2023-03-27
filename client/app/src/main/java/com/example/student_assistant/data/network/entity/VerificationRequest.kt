package com.example.student_assistant.data.network.entity

import com.google.gson.annotations.SerializedName

data class VerificationRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("verification_code")
    val verificationCode: String,
)
