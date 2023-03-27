package com.example.student_assistant.ui.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.student_assistant.domain.entity.Card
import com.example.student_assistant.domain.entity.Project
import javax.inject.Inject

class CardDiffCalculator @Inject constructor() : DiffUtil.ItemCallback<Card>() {
    override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem == newItem
    }
}