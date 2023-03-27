package com.example.student_assistant.ui.project.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.student_assistant.databinding.ItemProjectParameterBinding
import com.example.student_assistant.domain.entity.Parameter
import javax.inject.Inject

class ProjectParametersAdapter @Inject constructor(
    diffCalculator: ProjectParametersDiffCalculator,
) : ListAdapter<Parameter, ProjectParametersAdapter.ProjectParametersViewHolder>(diffCalculator) {

    var onItemClick: ((Parameter) -> Unit)? = null

    class ProjectParametersViewHolder (
        private val binding: ItemProjectParameterBinding,
        private val adapter: TagAdapter,
    ) : ViewHolder(binding.root) {
        fun bind(parameter: Parameter, onItemClick: ((Parameter) -> Unit)?) {
            binding.apply {
                tvName.text = parameter.name
                adapter.submitList(parameter.chosen.map { parameter.values[it] })
                itemProjectParamRv.adapter = adapter
                itemProjectParamRv.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
                root.setOnClickListener {
                    onItemClick?.invoke(parameter)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectParametersViewHolder {
        val binding = ItemProjectParameterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val adapter = TagAdapter(TagDiffCalculator())
        return ProjectParametersAdapter.ProjectParametersViewHolder(binding, adapter)
    }

    override fun onBindViewHolder(holder: ProjectParametersViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }
}