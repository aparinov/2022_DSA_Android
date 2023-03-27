package com.example.student_assistant.ui.project.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.student_assistant.domain.entity.Parameter
import javax.inject.Inject

class ProjectParametersDiffCalculator @Inject constructor() : DiffUtil.ItemCallback<Parameter>() {
    override fun areItemsTheSame(oldItem: Parameter, newItem: Parameter): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Parameter, newItem: Parameter): Boolean {
        return oldItem == newItem
    }
}