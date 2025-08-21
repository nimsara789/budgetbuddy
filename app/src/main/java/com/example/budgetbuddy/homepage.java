package com.example.budgetbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Calendar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class homepage extends AppCompatActivity {

    private TextView balanceTextView;
    private TextView nameTextView;
    private TextView greetingTextView;
    private CardView expenseCard, incomeCard, budgetCard, locationCard;
    private ImageView profileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupClickListeners();
        loadUserData();
        setGreeting();
    }

    private void initializeViews() {
        // Initialize TextViews
        balanceTextView = findViewById(R.id.balance_text);
        nameTextView = findViewById(R.id.name_text);
        greetingTextView = findViewById(R.id.greeting_text);
        profileIcon = findViewById(R.id.profile_icon);

        // Initialize CardViews for main features
        expenseCard = findViewById(R.id.expense_card);
        incomeCard = findViewById(R.id.income_card);
        budgetCard = findViewById(R.id.budget_card);
        locationCard = findViewById(R.id.location_card);
    }

    private void setupClickListeners() {
        // Profile icon click listener
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homepage.this, profile.class);
                startActivity(intent);
            }
        });

        expenseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to expense tracking page
                Intent intent = new Intent(homepage.this, expenses.class);
                startActivity(intent);
            }
        });

        incomeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to income tracking page
                Intent intent = new Intent(homepage.this, income.class);
                startActivity(intent);
            }
        });

        budgetCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to budget management page
                Intent intent = new Intent(homepage.this, budget.class);
                startActivity(intent);
            }
        });

        locationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to location page
                Intent intent = new Intent(homepage.this, location.class);
                startActivity(intent);
            }
        });
    }

    private void loadUserData() {
        // For demonstration purposes - in a real app, you would load this from a database or preferences
        nameTextView.setText("Hi, Welcome Back!");
        balanceTextView.setText("Rs 25,000.00");
    }

    private void setGreeting() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (hourOfDay >= 0 && hourOfDay < 12) {
            greeting = "Good Morning";
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            greeting = "Good Afternoon";
        } else {
            greeting = "Good Evening";
        }

        greetingTextView.setText(greeting);
    }
}