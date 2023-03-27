package com.example.student_assistant.data.network.entity

import com.google.gson.annotations.SerializedName

data class GetTagsResponse(
    @SerializedName("tags")
    val tags: List<String>,
)