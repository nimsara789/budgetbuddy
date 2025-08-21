package com.example.budgetbuddy;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BudgetsetupActivity extends AppCompatActivity {

    private TextView totalBudget, foodAmount, transportAmount, housingAmount, entertainmentAmount, otherAmount;
    private Button deleteButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budgetsetup);

        // Initialize views
        totalBudget = findViewById(R.id.total_budget);
        foodAmount = findViewById(R.id.food_amount);
        transportAmount = findViewById(R.id.transport_amount);
        housingAmount = findViewById(R.id.housing_amount);
        entertainmentAmount = findViewById(R.id.entertainment_amount);
        otherAmount = findViewById(R.id.other_amount);
        deleteButton = findViewById(R.id.delete_button);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Load budget data
        loadBudgetData();

        // Set delete button click listener
        deleteButton.setOnClickListener(v -> confirmDelete());
    }

    private void loadBudgetData() {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(
                "budgets",
                null,
                null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                double totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("total_amount"));
                int foodPercent = cursor.getInt(cursor.getColumnIndexOrThrow("food_percent"));
                int transportPercent = cursor.getInt(cursor.getColumnIndexOrThrow("transport_percent"));
                int housingPercent = cursor.getInt(cursor.getColumnIndexOrThrow("housing_percent"));
                int entertainmentPercent = cursor.getInt(cursor.getColumnIndexOrThrow("entertainment_percent"));
                int otherPercent = cursor.getInt(cursor.getColumnIndexOrThrow("other_percent"));

                // Display total budget
                totalBudget.setText("Monthly Budget: ₹" + totalAmount);

                // Calculate and display category amounts
                foodAmount.setText("₹" + (totalAmount * foodPercent / 100) + " (" + foodPercent + "%)");
                transportAmount.setText("₹" + (totalAmount * transportPercent / 100) + " (" + transportPercent + "%)");
                housingAmount.setText("₹" + (totalAmount * housingPercent / 100) + " (" + housingPercent + "%)");
                entertainmentAmount.setText("₹" + (totalAmount * entertainmentPercent / 100) + " (" + entertainmentPercent + "%)");
                otherAmount.setText("₹" + (totalAmount * otherPercent / 100) + " (" + otherPercent + "%)");

                cursor.close();
            } else {
                Toast.makeText(this, "No budget data found", Toast.LENGTH_SHORT).show();
            }
            db.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
            .setTitle("Delete Budget")
            .setMessage("Are you sure you want to delete this budget?")
            .setPositiveButton("Yes", (dialog, which) -> deleteBudget())
            .setNegativeButton("No", null)
            .show();
    }

    private void deleteBudget() {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("budgets", null, null);
            db.close();
            Toast.makeText(this, "Budget deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Error deleting budget: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Database helper class
    public static class DatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {
        private static final String DATABASE_NAME = "budget_buddy.db";
        private static final int DATABASE_VERSION = 1;

        public DatabaseHelper(android.content.Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS budgets (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "total_amount REAL, " +
                "food_percent INTEGER, " +
                "transport_percent INTEGER, " +
                "housing_percent INTEGER, " +
                "entertainment_percent INTEGER, " +
                "other_percent INTEGER)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS budgets");
            onCreate(db);
        }
    }
}