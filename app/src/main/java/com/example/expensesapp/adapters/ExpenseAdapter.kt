package com.example.expensesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensesapp.databinding.ItemExpenseBinding
import com.example.expensesapp.models.Expense
import com.example.expensesapp.utils.CurrencyUtils
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapter(
    private val onItemClick: ((Expense) -> Unit)? = null
) : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ExpenseViewHolder(
        private val binding: ItemExpenseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(getItem(position))
                }
            }
        }

        fun bind(expense: Expense) {
            binding.apply {
                dateTextView.text = SimpleDateFormat("MMM dd", Locale.getDefault()).format(expense.date)
                categoryTextView.text = expense.category
                descriptionTextView.text = expense.description
                amountTextView.text = CurrencyUtils.formatAmount(expense.amount)
                
                // Set category color indicator
                categoryColorIndicator.setBackgroundResource(
                    when (expense.category.lowercase()) {
                        "groceries" -> android.R.color.holo_green_light
                        "rent" -> android.R.color.holo_red_light
                        "transport" -> android.R.color.holo_blue_light
                        "entertainment" -> android.R.color.holo_orange_light
                        "utilities" -> android.R.color.holo_purple
                        else -> android.R.color.darker_gray
                    }
                )

                // For now, hide the receipt icon since we don't have receipt functionality yet
                receiptIcon.visibility = android.view.View.GONE
            }
        }
    }

    private class ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }
} 