package com.example.budgetbuddy;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Locale;

public class budget extends AppCompatActivity {

    private EditText monthlyBudgetInput;
    private SeekBar foodSeekbar, transportSeekbar, housingSeekbar, entertainmentSeekbar, otherSeekbar;
    private TextView foodPercentage, transportPercentage, housingPercentage, entertainmentPercentage, otherPercentage;
    private TextView allocationTotal, remainingText;
    private Button saveBudgetButton;

    private int foodPercent = 30;
    private int transportPercent = 15;
    private int housingPercent = 25;
    private int entertainmentPercent = 10;
    private int otherPercent = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        initializeViews();
        setupSeekBars();
        setupSaveButton();
        updateAllocationSummary();
    }

    private void initializeViews() {
        // Initialize EditText
        monthlyBudgetInput = findViewById(R.id.monthly_budget_input);

        // Initialize SeekBars
        foodSeekbar = findViewById(R.id.food_seekbar);
        transportSeekbar = findViewById(R.id.transport_seekbar);
        housingSeekbar = findViewById(R.id.housing_seekbar);
        entertainmentSeekbar = findViewById(R.id.entertainment_seekbar);
        otherSeekbar = findViewById(R.id.other_seekbar);

        // Initialize TextViews
        foodPercentage = findViewById(R.id.food_percentage);
        transportPercentage = findViewById(R.id.transport_percentage);
        housingPercentage = findViewById(R.id.housing_percentage);
        entertainmentPercentage = findViewById(R.id.entertainment_percentage);
        otherPercentage = findViewById(R.id.other_percentage);
        allocationTotal = findViewById(R.id.allocation_total);
        remainingText = findViewById(R.id.remaining_text);

        // Initialize Button
        saveBudgetButton = findViewById(R.id.save_budget_button);
    }

    private void setupSeekBars() {
        // Set initial values
        foodSeekbar.setProgress(foodPercent);
        transportSeekbar.setProgress(transportPercent);
        housingSeekbar.setProgress(housingPercent);
        entertainmentSeekbar.setProgress(entertainmentPercent);
        otherSeekbar.setProgress(otherPercent);

        // Set up SeekBar listeners
        foodSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                foodPercent = progress;
                foodPercentage.setText(progress + "%");
                updateAllocationSummary();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        transportSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                transportPercent = progress;
                transportPercentage.setText(progress + "%");
                updateAllocationSummary();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        housingSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                housingPercent = progress;
                housingPercentage.setText(progress + "%");
                updateAllocationSummary();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        entertainmentSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                entertainmentPercent = progress;
                entertainmentPercentage.setText(progress + "%");
                updateAllocationSummary();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        otherSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                otherPercent = progress;
                otherPercentage.setText(progress + "%");
                updateAllocationSummary();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateAllocationSummary() {
        int totalAllocation = foodPercent + transportPercent + housingPercent + entertainmentPercent + otherPercent;
        int remaining = 100 - totalAllocation;

        allocationTotal.setText("Total Allocation: " + totalAllocation + "%");

        if (remaining < 0) {
            remainingText.setText("Overallocated by: " + Math.abs(remaining) + "%");
            remainingText.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        } else {
            remainingText.setText("Remaining: " + remaining + "%");
            remainingText.setTextColor(getResources().getColor(remaining == 0 ?
                    android.R.color.holo_green_dark : android.R.color.holo_orange_dark));
        }
    }

    private void setupSaveButton() {
        saveBudgetButton.setOnClickListener(v -> saveBudget());

        // Add text change listener to validate budget input
        monthlyBudgetInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validateBudgetInput();
            }
        });
    }

    private boolean validateBudgetInput() {
        String budgetText = monthlyBudgetInput.getText().toString().trim();
        if (budgetText.isEmpty()) {
            monthlyBudgetInput.setError("Please enter a monthly budget amount");
            return false;
        }

        try {
            double budgetAmount = Double.parseDouble(budgetText);
            if (budgetAmount <= 0) {
                monthlyBudgetInput.setError("Budget amount must be greater than zero");
                return false;
            }
        } catch (NumberFormatException e) {
            monthlyBudgetInput.setError("Please enter a valid amount");
            return false;
        }

        return true;
    }

    private void saveBudget() {
        // Validate input
        if (!validateBudgetInput()) {
            return;
        }

        // Check allocation
        int totalAllocation = foodPercent + transportPercent + housingPercent + entertainmentPercent + otherPercent;
        if (totalAllocation != 100) {
            Toast.makeText(this, "Total allocation must equal 100%", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get budget amount
        double budgetAmount = Double.parseDouble(monthlyBudgetInput.getText().toString());

        // Format currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        String formattedBudget = currencyFormat.format(budgetAmount).replace("INR", "Rs");

        // In a real app, you would save this data to a database
        // For demonstration, we'll just show a success message
        StringBuilder budgetDetails = new StringBuilder();
        budgetDetails.append("Monthly Budget: ").append(formattedBudget).append("\n\n");
        budgetDetails.append("Food: ").append(calculateAmount(budgetAmount, foodPercent)).append(" (").append(foodPercent).append("%)\n");
        budgetDetails.append("Transport: ").append(calculateAmount(budgetAmount, transportPercent)).append(" (").append(transportPercent).append("%)\n");
        budgetDetails.append("Housing: ").append(calculateAmount(budgetAmount, housingPercent)).append(" (").append(housingPercent).append("%)\n");
        budgetDetails.append("Entertainment: ").append(calculateAmount(budgetAmount, entertainmentPercent)).append(" (").append(entertainmentPercent).append("%)\n");
        budgetDetails.append("Other: ").append(calculateAmount(budgetAmount, otherPercent)).append(" (").append(otherPercent).append("%)");

        Toast.makeText(this, "Budget plan saved successfully!", Toast.LENGTH_SHORT).show();

        // Return to previous screen
        finish();
    }

    private String calculateAmount(double total, int percentage) {
        double amount = (total * percentage) / 100;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        return currencyFormat.format(amount).replace("INR", "Rs");
    }
}