package com.example.budgetbuddy;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class IncomedetailsActivity extends AppCompatActivity {

    private TextView amountText, categoryText, dateText, notesText;
    private Button deleteButton;
    private DatabaseHelper dbHelper;
    private int incomeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incomedetails);

        // Initialize views
        amountText = findViewById(R.id.income_amount);
        categoryText = findViewById(R.id.income_category);
        dateText = findViewById(R.id.income_date);
        notesText = findViewById(R.id.income_notes);
        deleteButton = findViewById(R.id.delete_button);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Get income ID from intent
        if (getIntent().hasExtra("INCOME_ID")) {
            incomeId = getIntent().getIntExtra("INCOME_ID", -1);
        }

        // Load income data
        loadIncomeData();

        // Set up delete button
        deleteButton.setOnClickListener(v -> confirmDelete());
    }

    private void loadIncomeData() {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = null;

            if (incomeId != -1) {
                // Query for specific income by ID
                cursor = db.query(
                        "incomes",
                        new String[]{"amount", "category", "date", "notes"},
                        "id = ?",
                        new String[]{String.valueOf(incomeId)},
                        null, null, null
                );
            } else {
                // If no ID provided, get the most recent entry
                cursor = db.query(
                        "incomes",
                        new String[]{"id", "amount", "category", "date", "notes"},
                        null, null, null, null,
                        "id DESC", "1"
                );

                if (cursor != null && cursor.moveToFirst()) {
                    incomeId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                }
            }

            if (cursor != null && cursor.moveToFirst()) {
                // Display the income details
                String amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String notes = cursor.getString(cursor.getColumnIndexOrThrow("notes"));

                amountText.setText(amount);
                categoryText.setText(category);
                dateText.setText(date);
                notesText.setText(notes.isEmpty() ? "None" : notes);
                cursor.close();
            } else {
                Toast.makeText(this, "No income data found", Toast.LENGTH_SHORT).show();
                finish();
            }

            db.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading income: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Income")
                .setMessage("Are you sure you want to delete this income record?")
                .setPositiveButton("Delete", (dialog, which) -> deleteIncome())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteIncome() {
        if (incomeId == -1) {
            Toast.makeText(this, "Invalid income record", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int rowsDeleted = db.delete(
                    "incomes",
                    "id = ?",
                    new String[]{String.valueOf(incomeId)}
            );
            db.close();

            if (rowsDeleted > 0) {
                Toast.makeText(this, "Income deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to delete income", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error deleting income: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static class DatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {
        private static final String DATABASE_NAME = "budget_buddy.db";
        private static final int DATABASE_VERSION = 1;

        public DatabaseHelper(android.content.Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS incomes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "amount TEXT, " +
                    "category TEXT, " +
                    "date TEXT, " +
                    "notes TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS incomes");
            onCreate(db);
        }
    }
}