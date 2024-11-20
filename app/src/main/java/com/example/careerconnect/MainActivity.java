package com.example.careerconnect;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser; // Variable to track login status

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null)
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // User is signed in, navigate to HomeActivity
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        } else {
            // No user is signed in, navigate to LoginActivity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        finish(); // Close MainActivity
    }
}
