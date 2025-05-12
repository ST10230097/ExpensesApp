package com.example.expensesapp.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

    fun formatAmount(amount: Double): String {
        return currencyFormat.format(amount)
    }

    fun formatAmount(amount: Float): String {
        return currencyFormat.format(amount)
    }

    fun formatAmount(amount: Int): String {
        return currencyFormat.format(amount)
    }

    fun parseAmount(amountString: String): Double {
        return try {
            currencyFormat.parse(amountString)?.toDouble() ?: 0.0
        } catch (e: Exception) {
            0.0
        }
    }
} 