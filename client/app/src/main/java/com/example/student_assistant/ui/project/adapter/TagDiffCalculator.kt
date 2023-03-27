package com.example.student_assistant.ui.project.adapter

import androidx.recyclerview.widget.DiffUtil
import javax.inject.Inject

class TagDiffCalculator @Inject constructor() : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}