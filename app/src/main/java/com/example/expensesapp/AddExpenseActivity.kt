package com.example.expensesapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.expensesapp.databinding.ActivityAddExpenseBinding
import com.example.expensesapp.utils.CurrencyUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddExpenseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddExpenseBinding
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupDatePicker()
        setupCategoryDropdown()
        setupPhotoButtons()
        setupSaveButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupDatePicker() {
        // Set default date to today
        updateDateDisplay(calendar.time)

        binding.dateEditText.setOnClickListener {
            showDatePicker()
        }
    }

    private fun setupCategoryDropdown() {
        val categories = arrayOf(
            "Select a category",
            "Groceries",
            "Transport",
            "Dining",
            "Entertainment",
            "Utilities"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        binding.categoryAutoComplete.setAdapter(adapter)
    }

    private fun setupPhotoButtons() {
        binding.takePhotoButton.setOnClickListener {
            // TODO: Implement camera intent
            Toast.makeText(this, "Camera functionality not implemented", Toast.LENGTH_SHORT).show()
        }

        binding.choosePhotoButton.setOnClickListener {
            // TODO: Implement gallery intent
            Toast.makeText(this, "Gallery functionality not implemented", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSaveButton() {
        binding.saveFab.setOnClickListener {
            if (validateInputs()) {
                // TODO: Save expense to database
                Toast.makeText(this, "Expense saved successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                updateDateDisplay(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateDisplay(date: Date) {
        binding.dateEditText.setText(dateFormat.format(date))
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Validate amount
        val amountText = binding.amountEditText.text.toString()
        if (amountText.isEmpty() || amountText.toDoubleOrNull() == 0.0) {
            binding.amountInputLayout.error = "Please enter a valid amount"
            isValid = false
        } else {
            binding.amountInputLayout.error = null
        }

        // Validate category
        val category = binding.categoryAutoComplete.text.toString()
        if (category == "Select a category") {
            binding.categoryInputLayout.error = "Please select a category"
            isValid = false
        } else {
            binding.categoryInputLayout.error = null
        }

        return isValid
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
} 