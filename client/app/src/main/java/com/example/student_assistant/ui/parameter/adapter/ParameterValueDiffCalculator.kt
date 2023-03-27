package com.example.student_assistant.ui.parameter.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.student_assistant.domain.entity.ParameterValue
import javax.inject.Inject

class ParameterValueDiffCalculator @Inject constructor() : DiffUtil.ItemCallback<ParameterValue>() {
    override fun areItemsTheSame(oldItem: ParameterValue, newItem: ParameterValue): Boolean {
        return oldItem.id == newItem.id && oldItem.page == newItem.page
    }

    override fun areContentsTheSame(oldItem: ParameterValue, newItem: ParameterValue): Boolean {
        return oldItem == newItem
    }
}