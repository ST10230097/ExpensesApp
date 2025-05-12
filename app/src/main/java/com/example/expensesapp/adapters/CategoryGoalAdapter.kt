package com.example.expensesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensesapp.R
import com.example.expensesapp.databinding.ItemCategoryGoalBinding
import com.example.expensesapp.models.Category
import com.example.expensesapp.utils.CurrencyUtils

class CategoryGoalAdapter(
    private val onEditClick: (Category) -> Unit
) : ListAdapter<Category, CategoryGoalAdapter.CategoryGoalViewHolder>(CategoryGoalDiffCallback()) {

    private var isEditMode = false

    fun setEditMode(editMode: Boolean) {
        isEditMode = editMode
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryGoalViewHolder {
        val binding = ItemCategoryGoalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryGoalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryGoalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CategoryGoalViewHolder(
        private val binding: ItemCategoryGoalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.apply {
                categoryColorIndicator.setBackgroundColor(category.color)
                categoryNameTextView.text = category.name
                spendingTextView.text = "${CurrencyUtils.formatAmount(category.currentSpending)} / ${CurrencyUtils.formatAmount(category.budgetLimit)}"
                
                // Calculate progress and set color
                val progress = (category.currentSpending / category.budgetLimit * 100).toInt()
                progressIndicator.progress = progress
                
                // Set progress color based on percentage
                val progressColor = when {
                    progress >= 90 -> ContextCompat.getColor(root.context, R.color.progress_red)
                    progress >= 70 -> ContextCompat.getColor(root.context, R.color.progress_yellow)
                    else -> ContextCompat.getColor(root.context, R.color.progress_green)
                }
                progressIndicator.setIndicatorColor(progressColor)

                // Show/hide edit button based on edit mode
                editButton.visibility = if (isEditMode) ViewGroup.VISIBLE else ViewGroup.GONE
                editButton.setOnClickListener { onEditClick(category) }
            }
        }
    }

    private class CategoryGoalDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
} 