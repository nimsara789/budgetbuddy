package com.example.budgetbuddy;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class expenses extends AppCompatActivity {

    private EditText amountInput;
    private Spinner categorySpinner;
    private EditText dateInput;
    private EditText notesInput;
    private Button saveExpenseButton;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        // Initialize UI components
        initializeViews();
        setupCategorySpinner();
        setupDatePicker();
        setupSaveButton();
    }

    private void initializeViews() {
        amountInput = findViewById(R.id.expense_amount_input);
        categorySpinner = findViewById(R.id.expense_category_spinner);
        dateInput = findViewById(R.id.expense_date_input);
        notesInput = findViewById(R.id.expense_notes_input);
        saveExpenseButton = findViewById(R.id.save_expense_button);

        // Initialize calendar with current date
        calendar = Calendar.getInstance();
        updateDateLabel();
    }

    private void setupCategorySpinner() {
        // Create an ArrayAdapter using a simple spinner layout and expense categories
        String[] categories = {"Food", "Transportation", "Housing", "Entertainment",
                "Shopping", "Utilities", "Healthcare", "Education", "Travel", "Other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);
    }

    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };

        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(expenses.this, date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateDateLabel() {
        String format = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        dateInput.setText(dateFormat.format(calendar.getTime()));
    }

    private void setupSaveButton() {
        saveExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });
    }

    private void saveExpense() {
        // Validate input
        if (!validateInput()) {
            return;
        }

        // Get expense details
        double amount = Double.parseDouble(amountInput.getText().toString());
        String category = categorySpinner.getSelectedItem().toString();
        String date = dateInput.getText().toString();
        String notes = notesInput.getText().toString();

        // In a real app, you would save this data to a database
        // For now, just show a success message
        String message = String.format(Locale.getDefault(),
                "Expense of Rs %.2f saved in %s category",
                amount, category);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // Return to previous screen
        finish();
    }

    private boolean validateInput() {
        if (amountInput.getText().toString().isEmpty()) {
            amountInput.setError("Please enter an amount");
            return false;
        }

        try {
            double amount = Double.parseDouble(amountInput.getText().toString());
            if (amount <= 0) {
                amountInput.setError("Amount must be greater than zero");
                return false;
            }
        } catch (NumberFormatException e) {
            amountInput.setError("Please enter a valid amount");
            return false;
        }

        return true;
    }
}