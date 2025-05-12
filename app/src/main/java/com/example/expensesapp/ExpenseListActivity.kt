package com.example.expensesapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensesapp.adapters.ExpenseAdapter
import com.example.expensesapp.databinding.ActivityExpenseListBinding
import com.example.expensesapp.models.Expense
import com.example.expensesapp.utils.CurrencyUtils
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class ExpenseListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpenseListBinding
    private lateinit var expenseAdapter: ExpenseAdapter
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
    private val dateRangeFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupPeriodSelector()
        setupSummaryCards()
        setupFab()
        setupBackPress()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { 
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        expenseAdapter = ExpenseAdapter { expense ->
            // Handle expense item click
        }
        binding.expensesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ExpenseListActivity)
            adapter = expenseAdapter
        }

        // Add sample data
        val sampleExpenses = listOf(
            Expense(
                id = 1,
                amount = 234.56,
                category = "Groceries",
                description = "Weekly grocery shopping at Pick n Pay",
                date = Date(),
                userId = 1L
            ),
            Expense(
                id = 2,
                amount = 1500.00,
                category = "Rent",
                description = "Monthly rent payment",
                date = Date(),
                userId = 1L
            )
        )
        expenseAdapter.submitList(sampleExpenses)
        updateEmptyState(sampleExpenses.isEmpty())
    }

    private fun setupPeriodSelector() {
        binding.periodChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            when (checkedIds.firstOrNull()) {
                R.id.todayChip -> setDateRange(Calendar.DAY_OF_MONTH, 0)
                R.id.weekChip -> setDateRange(Calendar.WEEK_OF_YEAR, -1)
                R.id.monthChip -> setDateRange(Calendar.MONTH, -1)
                R.id.customChip -> showDateRangePicker()
            }
        }

        // Set default selection
        binding.monthChip.isChecked = true
    }

    private fun setDateRange(calendarField: Int, amount: Int) {
        val endDate = Calendar.getInstance()
        val startDate = Calendar.getInstance()
        startDate.add(calendarField, amount)
        updateDateRangeText(startDate.time, endDate.time)
    }

    private fun showDateRangePicker() {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select Date Range")
            .build()

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = Date(selection.first)
            val endDate = Date(selection.second)
            updateDateRangeText(startDate, endDate)
        }

        dateRangePicker.show(supportFragmentManager, "date_range_picker")
    }

    private fun updateDateRangeText(startDate: Date, endDate: Date) {
        val dateRange = "${dateRangeFormat.format(startDate)} - ${dateRangeFormat.format(endDate)}"
        binding.dateRangeTextView.text = dateRange
    }

    private fun setupSummaryCards() {
        // Update total spent
        val totalSpent = expenseAdapter.currentList.sumOf { it.amount }
        binding.totalSpentTextView.text = CurrencyUtils.formatAmount(totalSpent)

        // Update comparison (placeholder)
        binding.comparisonTextView.text = "+12.5%"
    }

    private fun setupFab() {
        binding.addExpenseFab.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }
    }

    private fun setupBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.emptyStateLayout.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.expensesRecyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_expense_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_categories -> {
                startActivity(Intent(this, CategoriesActivity::class.java))
                true
            }
            R.id.action_filter -> {
                showFilterDialog()
                true
            }
            R.id.action_sort -> {
                showSortDialog()
                true
            }
            R.id.action_export -> {
                // TODO: Implement export functionality
                Snackbar.make(binding.root, "Export functionality coming soon", Snackbar.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showFilterDialog() {
        val categories = arrayOf("Groceries", "Rent", "Transport", "Entertainment", "Utilities")
        val checkedItems = booleanArrayOf(false, false, false, false, false)

        MaterialAlertDialogBuilder(this)
            .setTitle("Filter by Category")
            .setMultiChoiceItems(categories, checkedItems) { _, _, _ -> }
            .setPositiveButton("Apply") { dialog, _ ->
                // Apply filters
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showSortDialog() {
        val sortOptions = arrayOf("Date (Newest First)", "Date (Oldest First)", "Amount (High to Low)", "Amount (Low to High)", "Category (A-Z)")
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Sort by")
            .setItems(sortOptions) { dialog, _ ->
                // Apply sorting
                dialog.dismiss()
            }
            .show()
    }
} 