package com.example.careerconnect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText emailEditText, passwordEditText, companyEditText, nameEditText;
    private MaterialSwitch refererSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.signup_email);
        passwordEditText = findViewById(R.id.signup_password);
        companyEditText = findViewById(R.id.signup_company);
        nameEditText = findViewById(R.id.signup_name);
        refererSwitch = findViewById(R.id.opt_in_switch);
        androidx.appcompat.widget.AppCompatButton signupButton = findViewById(R.id.signup_button);

        signupButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            String company = companyEditText.getText().toString().trim();
            String name = nameEditText.getText().toString().trim();
            boolean isReferer = refererSwitch.isChecked();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || company.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                createAccount(email, password, name, company, isReferer);
            }
        });
    }

    private void createAccount(String email, String password, String name, String company, boolean isReferer) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        saveUserData(user.getUid(), name, company, email, isReferer);
                    } else {
                        Toast.makeText(SignupActivity.this, "Authentication failed: " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserData(String uid, String name, String company, String email, boolean isReferer) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("company", company);
        user.put("email", email);
        user.put("isReferer", isReferer); // Include the referer boolean in the database

        db.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignupActivity.this, "Sign-up successful! Please log in.", Toast.LENGTH_SHORT).show();
                    navigateToLoginScreen();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignupActivity.this, "Failed to save data: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToLoginScreen() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close the current activity to prevent users from going back to the signup screen
    }
}
