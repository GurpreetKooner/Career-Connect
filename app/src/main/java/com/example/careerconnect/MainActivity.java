package com.example.careerconnect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private boolean login = true; // Variable to track login status

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Delay for 5 seconds before navigating
        new Handler().postDelayed(() -> {
            if (login) {
                // Navigate to HomeActivity
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            } else {
                // Navigate to LoginActivity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
            finish(); // Close MainActivity
        }, 5000); // 5000 milliseconds = 5 seconds
    }
}
