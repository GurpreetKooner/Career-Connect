package com.example.careerconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.SharedPreferences;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupText = findViewById(R.id.signup_text);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else {
                signIn(email, password);
            }
        });

        signupText.setOnClickListener(v -> {
            // Navigate to SignupActivity
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }

//    private void signIn(String email, String password) {
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        // Sign-in success, update UI with the signed-in user's information
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        Toast.makeText(LoginActivity.this, "Authentication successful.",
//                                Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                        finish();
//                    } else {
//                        // If sign-in fails, display a message to the user.
//                        Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }



    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, "Authentication successful.",
                                Toast.LENGTH_SHORT).show();

                        // Store password in SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", email);
                        editor.putString("password", password);  // Storing password (in plain text)
                        editor.apply();

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        // If sign-in fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
