package com.example.expensesapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.expensesapp.databinding.ActivityReportsBinding
import com.example.expensesapp.utils.CurrencyUtils
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ReportsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportsBinding
    private var selectedPeriod = TimePeriod.MONTHLY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupTimePeriodTabs()
        setupCharts()
        updateStatistics()
        setupExportButtons()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setupTimePeriodTabs() {
        binding.timePeriodTabs.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                selectedPeriod = when (tab?.position) {
                    0 -> TimePeriod.WEEKLY
                    1 -> TimePeriod.MONTHLY
                    2 -> TimePeriod.CUSTOM
                    else -> TimePeriod.MONTHLY
                }
                updateData()
            }

            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })
    }

    private fun setupCharts() {
        setupPieChart()
        setupBarChart()
    }

    private fun setupPieChart() {
        binding.categoryPieChart.apply {
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)
            holeRadius = 58f
            transparentCircleRadius = 61f
            setDrawCenterText(true)
            rotationAngle = 0f
            isRotationEnabled = true
            isHighlightPerTapEnabled = true
            legend.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
                xEntrySpace = 7f
                yEntrySpace = 0f
                yOffset = 0f
            }
            setEntryLabelColor(Color.WHITE)
            setEntryLabelTextSize(12f)
            animateY(1400, Easing.EaseInOutQuad)
        }

        updatePieChart()
    }

    private fun setupBarChart() {
        binding.spendingBarChart.apply {
            description.isEnabled = false
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            setDrawValueAboveBar(true)
            setPinchZoom(false)
            setScaleEnabled(false)
            legend.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }
            xAxis.apply {
                position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
            }
            axisLeft.apply {
                setDrawGridLines(true)
                axisMinimum = 0f
            }
            axisRight.isEnabled = false
            animateY(1400, Easing.EaseInOutQuad)
        }

        updateBarChart()
    }

    private fun updatePieChart() {
        // Sample data - replace with actual data from your database
        val entries = listOf(
            PieEntry(35f, "Groceries"),
            PieEntry(25f, "Transport"),
            PieEntry(20f, "Entertainment"),
            PieEntry(15f, "Bills"),
            PieEntry(5f, "Other")
        )

        val dataSet = PieDataSet(entries, "Categories").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
            valueTextSize = 12f
            valueFormatter = PercentFormatter(binding.categoryPieChart)
            valueTextColor = Color.WHITE
        }

        binding.categoryPieChart.data = PieData(dataSet)
        binding.categoryPieChart.invalidate()
    }

    private fun updateBarChart() {
        // Sample data - replace with actual data from your database
        val entries = listOf(
            BarEntry(0f, 120f),
            BarEntry(1f, 150f),
            BarEntry(2f, 90f),
            BarEntry(3f, 200f),
            BarEntry(4f, 180f),
            BarEntry(5f, 160f),
            BarEntry(6f, 140f)
        )

        val dataSet = BarDataSet(entries, "Daily Spending").apply {
            color = ContextCompat.getColor(this@ReportsActivity, R.color.colorPrimary)
            valueTextSize = 10f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return CurrencyUtils.formatAmount(value.toDouble())
                }
            }
        }

        binding.spendingBarChart.data = BarData(dataSet)
        binding.spendingBarChart.invalidate()
    }

    private fun updateStatistics() {
        // Sample data - replace with actual data from your database
        val totalSpent = 5000.0
        val dailyAverage = 250.0
        val biggestCategory = "Groceries"
        val biggestCategoryAmount = 2000.0

        binding.totalSpentTextView.text = CurrencyUtils.formatAmount(totalSpent)
        binding.dailyAverageTextView.text = CurrencyUtils.formatAmount(dailyAverage)
        binding.biggestCategoryTextView.text = biggestCategory
        binding.biggestCategoryAmountTextView.text = CurrencyUtils.formatAmount(biggestCategoryAmount)
    }

    private fun setupExportButtons() {
        binding.exportPdfButton.setOnClickListener {
            // TODO: Implement PDF export
        }

        binding.shareReportButton.setOnClickListener {
            // TODO: Implement report sharing
        }
    }

    private fun updateData() {
        updatePieChart()
        updateBarChart()
        updateStatistics()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_reports, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_custom_period -> {
                // TODO: Show date range picker
                true
            }
            R.id.action_refresh -> {
                updateData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private enum class TimePeriod {
        WEEKLY, MONTHLY, CUSTOM
    }
} 