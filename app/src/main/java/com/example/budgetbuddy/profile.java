package com.example.budgetbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class profile extends AppCompatActivity {

    private TextView usernameTextView;
    private TextView emailTextView;
    private EditText nameEditText;
    private EditText phoneEditText;
    private Button saveButton;
    private Button logoutButton;
    private Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        initViews();

        // Display sample data
        displayUserInfo();

        // Set up button listeners
        setupListeners();
    }

    private void initViews() {
        usernameTextView = findViewById(R.id.username_text);
        emailTextView = findViewById(R.id.email_text);
        nameEditText = findViewById(R.id.name_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        saveButton = findViewById(R.id.save_button);
        logoutButton = findViewById(R.id.logout_button);
        homeButton = findViewById(R.id.home_button);
    }

    private void displayUserInfo() {
        // Sample data - in a real app, this would come from a database or preferences
        usernameTextView.setText("Nimsara Wijayasekara");
        emailTextView.setText("user@example.com");
        nameEditText.setText("Nimsara Wijayasekara");
        phoneEditText.setText("0771234567");
    }

    private void setupListeners() {
        // Save button listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        // Logout button listener
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // Home button listener
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome();
            }
        });
    }

    private void saveProfile() {
        // Get input values
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        // Simple validation
        if (name.isEmpty()) {
            nameEditText.setError("Name cannot be empty");
            return;
        }

        // Update display (in a real app, you would save to database)
        usernameTextView.setText(name);

        // Show success message
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }

    private void logout() {
        // Navigate to login screen
        Intent intent = new Intent(profile.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void goToHome() {
        // Navigate back to homepage
        Intent intent = new Intent(profile.this, homepage.class);
        startActivity(intent);
        finish();
    }
}