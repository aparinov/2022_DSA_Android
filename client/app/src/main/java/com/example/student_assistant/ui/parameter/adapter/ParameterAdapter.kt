package com.example.student_assistant.ui.parameter.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.student_assistant.databinding.ItemParameterBinding
import com.example.student_assistant.domain.entity.Parameter
import com.example.student_assistant.domain.entity.ParameterValue
import javax.inject.Inject

class ParameterAdapter @Inject constructor(diffCalculator: ParameterValueDiffCalculator) : ListAdapter<ParameterValue, ParameterAdapter.ParameterViewHolder>(diffCalculator) {

    var onItemClick: ((Parameter) -> Unit)? = null
    private var parameter: Parameter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParameterAdapter.ParameterViewHolder {
        val binding = ItemParameterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParameterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParameterViewHolder, position: Int) {
        return holder.bind(getItem(position), parameter, onItemClick)
    }

    fun setParameter(parameter: Parameter) {
        this.parameter = parameter
        val items = parameter.values.mapIndexed { index, value ->
            ParameterValue(
                index,
                value,
                parameter.chosen.contains(index),
                parameter.page
            )
        }
        submitList(items)
    }


    inner class ParameterViewHolder(private val binding: ItemParameterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ParameterValue, parameter: Parameter?, onClick: ((Parameter) -> Unit)?) {
            if (parameter == null) {
                return
            }
            binding.apply {
                itemFilterTvItem.text = item.value
                if (parameter.name.contains("Статус")) {
                    itemFilterCb.visibility = View.GONE
                    itemFilterRb.visibility = View.VISIBLE
                    itemFilterRb.isChecked = item.isSelected
                    itemFilterRb.setOnCheckedChangeListener { _, b ->
                        if (b) {
                            parameter.chosen.clear()
                            parameter.chosen.add(adapterPosition)
                            onClick?.invoke(parameter)
                        }
                    }
                } else {
                    itemFilterCb.visibility = View.VISIBLE
                    itemFilterRb.visibility = View.GONE
                    itemFilterCb.isChecked = item.isSelected
                    itemFilterCb.setOnCheckedChangeListener { _, b ->
                        if (b) {
                            parameter.chosen.add(adapterPosition)
                        } else {
                            parameter.chosen.remove(adapterPosition)
                        }
                        onClick?.invoke(parameter)
                    }
                }
            }
        }
    }
}