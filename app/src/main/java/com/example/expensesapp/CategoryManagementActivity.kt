package com.example.expensesapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensesapp.adapters.CategoryAdapter
import com.example.expensesapp.databinding.ActivityCategoryManagementBinding
import com.example.expensesapp.models.CategoryProgress
import com.example.expensesapp.utils.CurrencyUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.google.android.material.chip.Chip

class CategoryManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryManagementBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private val predefinedColors = listOf(
        Color.parseColor("#FF5722"), // Deep Orange
        Color.parseColor("#2196F3"), // Blue
        Color.parseColor("#4CAF50"), // Green
        Color.parseColor("#9C27B0"), // Purple
        Color.parseColor("#F44336"), // Red
        Color.parseColor("#FFC107"), // Amber
        Color.parseColor("#009688"), // Teal
        Color.parseColor("#795548")  // Brown
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryManagementBinding.inflate(layoutInflater)
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
                showEditCategoryDialog(category)
            },
            onDeleteClick = { category ->
                showDeleteConfirmationDialog(category)
            }
        )
        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CategoryManagementActivity)
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
            showAddCategoryDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.category_management_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAddCategoryDialog() {
        showCategoryDialog(null)
    }

    private fun showEditCategoryDialog(category: CategoryProgress) {
        showCategoryDialog(category)
    }

    private fun showCategoryDialog(category: CategoryProgress?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_category, null)
        val nameInput = dialogView.findViewById<TextInputEditText>(R.id.nameInput)
        val budgetInput = dialogView.findViewById<TextInputEditText>(R.id.budgetInput)
        val colorContainer = dialogView.findViewById<LinearLayout>(R.id.colorContainer)

        // Pre-fill data if editing
        category?.let {
            nameInput.setText(it.name)
            budgetInput.setText(it.budget.toString())
        }

        // Add color chips
        var selectedColor = predefinedColors[0]
        predefinedColors.forEach { color ->
            val chip = Chip(this).apply {
                text = ""
                chipBackgroundColor = android.content.res.ColorStateList.valueOf(color)
                isCheckable = true
                isChecked = false
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedColor = color
                        // Uncheck other chips
                        for (i in 0 until colorContainer.childCount) {
                            val child = colorContainer.getChildAt(i)
                            if (child is Chip && child != this) {
                                child.isChecked = false
                            }
                        }
                    }
                }
            }
            colorContainer.addView(chip)
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(if (category == null) "Add Category" else "Edit Category")
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                val name = nameInput.text.toString()
                val budget = budgetInput.text.toString().toDoubleOrNull() ?: 0.0

                if (name.isBlank()) {
                    Snackbar.make(binding.root, "Please enter a category name", Snackbar.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (budget <= 0) {
                    Snackbar.make(binding.root, "Please enter a valid budget amount", Snackbar.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Create new category or update existing one
                val newCategory = if (category == null) {
                    CategoryProgress(
                        name = name,
                        spent = 0.0,
                        budget = budget
                    )
                } else {
                    category.copy(
                        name = name,
                        budget = budget
                    )
                }

                // TODO: Save category to database
                Snackbar.make(binding.root, "Category saved successfully", Snackbar.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(category: CategoryProgress) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Category")
            .setMessage("Are you sure you want to delete ${category.name}? This action cannot be undone.")
            .setPositiveButton("Delete") { dialog, _ ->
                // TODO: Delete category from database
                Snackbar.make(binding.root, "Category deleted successfully", Snackbar.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
} 