package com.example.budgetbuddy;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.app.ProgressDialog;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class createacc extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button createAccountButton;
    private TextView alreadyHaveAccountTextView;
    private TextView loginLinkTextView;
    private ProgressDialog progressDialog;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_createacc);

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        initializeViews();
        setupHints();
        setupClickListeners();

        // Initialize progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating account...");
        progressDialog.setCancelable(false);
    }

    private void initializeViews() {
        nameEditText = findViewById(R.id.editTextText);
        emailEditText = findViewById(R.id.editTextTextEmailAddress2);
        passwordEditText = findViewById(R.id.editTextTextPassword2);
        confirmPasswordEditText = findViewById(R.id.editTextTextPassword3);
        createAccountButton = findViewById(R.id.createAccountButton);
        alreadyHaveAccountTextView = findViewById(R.id.textView9);
        loginLinkTextView = findViewById(R.id.loginLink);
    }

    private void setupHints() {
        // Set hints for all input fields
        nameEditText.setHint("Enter your name");
        emailEditText.setHint("Enter your email");
        passwordEditText.setHint("Enter password");
        confirmPasswordEditText.setHint("Confirm your password");
    }

    private void setupClickListeners() {
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

        loginLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to login activity
                Intent intent = new Intent(createacc.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createAccount() {
        // Get input values
        final String name = nameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            nameEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if (!isValidEmail(email)) {
            emailEditText.setError("Enter a valid email address");
            emailEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password should be at least 6 characters");
            passwordEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordEditText.setError("Confirm your password");
            confirmPasswordEditText.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords don't match");
            confirmPasswordEditText.requestFocus();
            return;
        }

        // Show progress dialog
        progressDialog.show();

        // Create user with Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, save user data to database
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                saveUserData(user.getUid(), name, email);
                            }
                        } else {
                            // If sign in fails, display a message to the user
                            progressDialog.dismiss();
                            Toast.makeText(createacc.this, "Registration failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserData(String userId, String name, String email) {
        // Create user data map
        HashMap<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("userId", userId);
        user.put("createdAt", System.currentTimeMillis());

        // Save to Firebase Database
        mDatabase.child("users").child(userId).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(createacc.this, "Account created successfully", Toast.LENGTH_SHORT).show();

                            // Navigate to login page
                            Intent intent = new Intent(createacc.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(createacc.this, "Failed to save user data: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}