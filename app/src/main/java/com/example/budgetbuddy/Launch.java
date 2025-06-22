package com.example.budgetbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Launch extends AppCompatActivity {

    private Button signInButton;
    private Button loginButton;
    private Button forgotPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_launch);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize buttons
        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        signInButton = findViewById(R.id.button);
        loginButton = findViewById(R.id.button2);
        forgotPasswordButton = findViewById(R.id.button3);
    }

    private void setupClickListeners() {
        // Navigate to Create Account page when Sign In button is clicked
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Launch.this, createacc.class);
                startActivity(intent);
            }
        });

        // Navigate to Login page when Login button is clicked
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Launch.this, Login.class);
                startActivity(intent);
            }
        });

        // Navigate to Forgot Password page when Forgot Password button is clicked
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Launch.this, forgotpassword.class);
                startActivity(intent);
            }
        });
    }
}