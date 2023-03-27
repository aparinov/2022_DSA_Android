package com.example.student_assistant.ui.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.student_assistant.domain.entity.Card
import javax.inject.Inject
import com.example.student_assistant.databinding.ItemCardBinding
import com.example.student_assistant.domain.entity.Project

class CardAdapter @Inject constructor(diffCalculator: CardDiffCalculator) : ListAdapter<Card, CardAdapter.CardViewHolder>(diffCalculator) {

    var onItemClick: ((Card) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    class CardViewHolder(private val binding: ItemCardBinding) : ViewHolder(binding.root) {
        fun bind(item: Card, onItemClick: ((Card) -> Unit)?) {
            binding.apply {
                iCardTvTitle.text = item.title
                iCardTvText.text = item.description
                val statuses = item.status.split(", ")
                if (statuses.size == 2) {
                    iCardTvProjectStatus.text = statuses[1]
                    iCardTvRecStatus.text = statuses[0].toLowerCase()
                } else if (item.status.isNotBlank() && statuses.size == 1) {
                    iCardTvProjectStatus.text = statuses[0]
                    iCardTvRecStatus.visibility = View.GONE
                } else {
                    iCardTvProjectStatus.visibility = View.GONE
                    iCardTvRecStatus.visibility = View.GONE
                }
                root.setOnClickListener {
                    onItemClick?.invoke(item)
                }
            }
        }
    }
}