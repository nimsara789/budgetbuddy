package com.example.budgetbuddy;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class income extends AppCompatActivity {

    private EditText amountInput, dateInput, notesInput;
    private Spinner categorySpinner;
    private Button saveButton;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        // Initialize views
        amountInput = findViewById(R.id.income_amount_input);
        categorySpinner = findViewById(R.id.income_category_spinner);
        dateInput = findViewById(R.id.income_date_input);
        notesInput = findViewById(R.id.income_notes_input);
        saveButton = findViewById(R.id.save_income_button);

        // Setup components
        setupCategorySpinner();
        setupDatePicker();
        setupSaveButton();
    }

    private void setupCategorySpinner() {
        String[] categories = {"Salary", "Freelance", "Investments", "Gifts",
                "Allowance", "Bonus", "Rental Income", "Refunds", "Business", "Other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void setupDatePicker() {
        calendar = Calendar.getInstance();
        updateDateLabel();

        dateInput.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(income.this,
                    (view, year, month, day) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        updateDateLabel();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });
    }

    private void updateDateLabel() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        dateInput.setText(dateFormat.format(calendar.getTime()));
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> saveIncome());
    }

    private void saveIncome() {
        if (amountInput.getText().toString().isEmpty()) {
            amountInput.setError("Please enter an amount");
            return;
        }

        try {
            double amount = Double.parseDouble(amountInput.getText().toString());
            if (amount <= 0) {
                amountInput.setError("Amount must be greater than zero");
                return;
            }

            String category = categorySpinner.getSelectedItem().toString();
            String date = dateInput.getText().toString();
            String notes = notesInput.getText().toString();

            // In a real app, you would save to database here
            String message = String.format(Locale.getDefault(),
                    "Income of Rs %.2f saved in %s category",
                    amount, category);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            finish();
        } catch (NumberFormatException e) {
            amountInput.setError("Please enter a valid amount");
        }
    }
}