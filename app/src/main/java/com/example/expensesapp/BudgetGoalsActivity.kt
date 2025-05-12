package com.example.expensesapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensesapp.adapters.CategoryAdapter
import com.example.expensesapp.databinding.ActivityBudgetGoalsBinding
import com.example.expensesapp.models.CategoryProgress
import com.example.expensesapp.utils.CurrencyUtils
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class BudgetGoalsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBudgetGoalsBinding
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBudgetGoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupCategoriesList()
        updateSummary()
        setupAddCategoryButton()

        // Handle back press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupCategoriesList() {
        categoryAdapter = CategoryAdapter(
            onEditClick = { category ->
                // TODO: Implement edit category
            },
            onDeleteClick = { category ->
                // TODO: Implement delete category
            }
        )
        binding.categoryGoalsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@BudgetGoalsActivity)
            adapter = categoryAdapter
        }

        // Sample data - replace with actual data from your database
        val categories = listOf(
            CategoryProgress("Groceries", 120.0, 200.0),
            CategoryProgress("Transport", 300.0, 500.0),
            CategoryProgress("Entertainment", 400.0, 600.0),
            CategoryProgress("Utilities", 250.0, 300.0),
            CategoryProgress("Shopping", 500.0, 800.0)
        )

        categoryAdapter.submitList(categories)
    }

    private fun updateSummary() {
        // Sample data - replace with actual calculations
        val totalSpent = 1570.0
        val totalBudget = 2400.0
        val percentage = (totalSpent / totalBudget * 100).toInt()

        binding.summaryTotalSpentTextView.text = "Total Spent: ${CurrencyUtils.formatAmount(totalSpent)}"
        binding.summaryTotalBudgetTextView.text = "Total Budget: ${CurrencyUtils.formatAmount(totalBudget)}"
        binding.summaryProgressIndicator.progress = percentage
    }

    private fun setupAddCategoryButton() {
        binding.addCategoryFab.setOnClickListener {
            // TODO: Implement add category
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
} 