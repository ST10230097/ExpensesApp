package com.example.expensesapp.models

data class Category(
    val id: Long,
    val name: String,
    val color: Int,
    val budgetLimit: Double,
    val currentSpending: Double
) 