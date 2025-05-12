package com.example.expensesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensesapp.R
import com.example.expensesapp.databinding.ItemCategoryBinding
import com.example.expensesapp.models.CategoryProgress

class CategoryAdapter(
    private val onEditClick: (CategoryProgress) -> Unit,
    private val onDeleteClick: (CategoryProgress) -> Unit
) : ListAdapter<CategoryProgress, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: CategoryProgress) {
            binding.apply {
                categoryNameTextView.text = category.name
                spendingTextView.text = "$${String.format("%.2f", category.spent)} / $${String.format("%.2f", category.budget)}"
                
                val progress = (category.spent / category.budget * 100).toInt()
                progressIndicator.progress = progress

                // Set color based on progress
                val colorRes = when {
                    progress < 70 -> R.color.progress_green
                    progress < 90 -> R.color.progress_yellow
                    else -> R.color.progress_red
                }
                colorIndicator.setBackgroundColor(
                    ContextCompat.getColor(root.context, colorRes)
                )

                // Set click listeners
                editButton.setOnClickListener { onEditClick(category) }
                deleteButton.setOnClickListener { onDeleteClick(category) }
            }
        }
    }

    private class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryProgress>() {
        override fun areItemsTheSame(oldItem: CategoryProgress, newItem: CategoryProgress): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CategoryProgress, newItem: CategoryProgress): Boolean {
            return oldItem == newItem
        }
    }
} 