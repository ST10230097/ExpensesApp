package com.example.expensesapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensesapp.adapters.ExpenseAdapter
import com.example.expensesapp.databinding.ActivityDashboardBinding
import com.example.expensesapp.models.Expense
import com.example.expensesapp.utils.CurrencyUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupHeader()
        setupBudgetOverview()
        setupCategoryBreakdown()
        setupRecentTransactions()
        setupAchievements()
        setupQuickActions()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
            title = "Dashboard"
        }
    }

    private fun setupHeader() {
        val calendar = Calendar.getInstance()
        val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        binding.monthYearTextView.text = monthYearFormat.format(calendar.time)
        
        // TODO: Replace with actual user name from preferences/database
        binding.greetingTextView.text = "Hello, Tatenda!"
        
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val daysLeft = daysInMonth - currentDay
        binding.daysLeftTextView.text = "You have $daysLeft days left in your budget period"
    }

    private fun setupBudgetOverview() {
        val totalBudget = 10000.0
        val spent = 6500.0
        val percentage = (spent / totalBudget * 100).toInt()
        
        binding.budgetProgressIndicator.progress = percentage
        binding.budgetSpentTextView.text = "Spent: ${CurrencyUtils.formatAmount(spent)} of ${CurrencyUtils.formatAmount(totalBudget)}"
        binding.budgetPercentageTextView.text = "$percentage% used"
        
        // Update progress color based on percentage
        val progressColor = when {
            percentage < 70 -> R.color.progress_green
            percentage < 90 -> R.color.progress_yellow
            else -> R.color.progress_red
        }
        binding.budgetProgressIndicator.setIndicatorColor(getColor(progressColor))
        binding.budgetPercentageTextView.setTextColor(getColor(progressColor))
    }

    private fun setupCategoryBreakdown() {
        // Sample category data
        val categories = listOf(
            CategoryProgress("Groceries", 120.0, 200.0),
            CategoryProgress("Transport", 300.0, 500.0),
            CategoryProgress("Entertainment", 400.0, 600.0)
        )

        // Update category items
        categories.forEachIndexed { index, category ->
            val itemBinding = when (index) {
                0 -> binding.categoryItem1
                1 -> binding.categoryItem2
                2 -> binding.categoryItem3
                else -> return@forEachIndexed
            }

            itemBinding.categoryNameTextView.text = category.name
            itemBinding.categoryAmountTextView.text = "${CurrencyUtils.formatAmount(category.spent)} / ${CurrencyUtils.formatAmount(category.limit)}"
            
            val percentage = (category.spent / category.limit * 100).toInt()
            itemBinding.categoryProgressIndicator.progress = percentage
            
            // Update progress color based on percentage
            val progressColor = when {
                percentage < 70 -> R.color.progress_green
                percentage < 90 -> R.color.progress_yellow
                else -> R.color.progress_red
            }
            itemBinding.categoryProgressIndicator.setIndicatorColor(getColor(progressColor))
        }

        binding.viewAllCategoriesButton.setOnClickListener {
            startActivity(Intent(this, CategoriesActivity::class.java))
        }
    }

    private fun setupRecentTransactions() {
        // Sample transaction data
        val transactions = listOf(
            Expense(1, 120.0, "Grocery shopping", "Groceries", Date(), 1),
            Expense(2, 45.0, "Movie tickets", "Entertainment", Date(), 1),
            Expense(3, 30.0, "Bus fare", "Transport", Date(), 1)
        )

        expenseAdapter = ExpenseAdapter()
        binding.recentTransactionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            adapter = expenseAdapter
        }
        expenseAdapter.submitList(transactions)

        binding.viewAllTransactionsButton.setOnClickListener {
            startActivity(Intent(this, ExpenseListActivity::class.java))
        }
    }

    private fun setupAchievements() {
        // TODO: Implement achievements adapter and data
        binding.achievementsRecyclerView.visibility = View.GONE
        binding.viewAllAchievementsButton.setOnClickListener {
            // TODO: Navigate to achievements screen
        }
    }

    private fun setupQuickActions() {
        binding.addExpenseFab.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        binding.addCategoryFab.setOnClickListener {
            // TODO: Navigate to add category screen
        }

        binding.setBudgetFab.setOnClickListener {
            // TODO: Navigate to set budget screen
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_reports -> {
                startActivity(Intent(this, ReportsActivity::class.java))
                true
            }
            R.id.action_achievements -> {
                startActivity(Intent(this, AchievementsActivity::class.java))
                true
            }
            R.id.action_budget_goals -> {
                startActivity(Intent(this, BudgetGoalsActivity::class.java))
                true
            }
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private data class CategoryProgress(
        val name: String,
        val spent: Double,
        val limit: Double
    )
} 