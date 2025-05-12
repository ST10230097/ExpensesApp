package com.example.expensesapp

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensesapp.adapters.CategoryAdapter
import com.example.expensesapp.databinding.ActivityCategoriesBinding
import com.example.expensesapp.models.CategoryProgress
import com.example.expensesapp.utils.CurrencyUtils

class CategoriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
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
        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CategoriesActivity)
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

        binding.totalSpentTextView.text = "Total Spent: ${CurrencyUtils.formatAmount(totalSpent)}"
        binding.totalBudgetTextView.text = "Total Budget: ${CurrencyUtils.formatAmount(totalBudget)}"
        binding.overallProgressIndicator.progress = percentage
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