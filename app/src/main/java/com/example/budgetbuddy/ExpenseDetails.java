package com.example.budgetbuddy;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExpenseDetails extends AppCompatActivity {

    private TextView amountText, categoryText, dateText, notesText;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);

        // Initialize database helper
        dbHelper = new DbHelper(this);

        // Initialize UI components
        amountText = findViewById(R.id.expense_amount);
        categoryText = findViewById(R.id.expense_category);
        dateText = findViewById(R.id.expense_date);
        notesText = findViewById(R.id.expense_notes);

        // Get expense ID from intent
        String expenseId = getIntent().getStringExtra("EXPENSE_ID");

        // If no ID provided, load most recent expense
        if (expenseId == null) {
            loadMostRecentExpense();
        } else {
            loadExpenseDetails(expenseId);
        }
    }

    private void loadMostRecentExpense() {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query("expenses",
                    new String[]{"amount", "category", "date", "notes"},
                    null, null, null, null, "id DESC", "1");

            if (cursor != null && cursor.moveToFirst()) {
                displayExpenseData(cursor);
                cursor.close();
            } else {
                Toast.makeText(this, "No expenses found", Toast.LENGTH_SHORT).show();
            }
            db.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadExpenseDetails(String id) {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query("expenses",
                    new String[]{"amount", "category", "date", "notes"},
                    "id = ?", new String[]{id},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                displayExpenseData(cursor);
                cursor.close();
            } else {
                Toast.makeText(this, "Expense not found", Toast.LENGTH_SHORT).show();
                loadMostRecentExpense(); // Fall back to showing most recent
            }
            db.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void displayExpenseData(Cursor cursor) {
        String amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"));
        String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
        String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
        String notes = cursor.getString(cursor.getColumnIndexOrThrow("notes"));

        amountText.setText("₹" + amount);
        categoryText.setText(category);
        dateText.setText(date);
        notesText.setText(notes != null && !notes.isEmpty() ? notes : "No notes");
    }

    public static class DbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "budget_buddy.db";
        private static final int DATABASE_VERSION = 1;

        public DbHelper(android.content.Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS expenses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "amount TEXT, " +
                    "category TEXT, " +
                    "date TEXT, " +
                    "notes TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS expenses");
            onCreate(db);
        }
    }
}