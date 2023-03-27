package com.example.student_assistant.data.network.entity

import com.google.gson.annotations.SerializedName

data class GetUserResponse(
    @SerializedName("is_student")
    val isStudent: Boolean,
    @SerializedName("first_name")
    val name: String,
    @SerializedName("last_name")
    val surname: String,
    @SerializedName("contacts")
    val contacts: String,
    @SerializedName("tags")
    val tags: List<String>,
)
