package com.example.student_assistant.ui.project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.student_assistant.databinding.ItemTagBinding
import javax.inject.Inject

class TagAdapter @Inject constructor(diffCalculator: TagDiffCalculator) : ListAdapter<String, TagAdapter.TagViewHolder>(diffCalculator) {
    class TagViewHolder(private val binding: ItemTagBinding): ViewHolder(binding.root) {
        fun bind(tag: String) {
            binding.tvTag.text = tag
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val binding = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}