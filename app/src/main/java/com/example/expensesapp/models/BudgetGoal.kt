package com.example.expensesapp.models

import com.example.expensesapp.utils.CurrencyUtils

data class BudgetGoal(
    val id: Long = 0,
    val category: String,
    val amount: Double,
    val period: String, // "monthly", "weekly", "yearly"
    val userId: Long
) {
    val formattedAmount: String
        get() = CurrencyUtils.formatAmount(amount)
} 