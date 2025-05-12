package com.example.expensesapp.models

import java.util.Date

data class Achievement(
    val id: Int,
    val name: String,
    val description: String,
    val progress: Int,
    val total: Int,
    val reward: String,
    val isUnlocked: Boolean,
    val unlockDate: Date?,
    val tips: List<String>
) 