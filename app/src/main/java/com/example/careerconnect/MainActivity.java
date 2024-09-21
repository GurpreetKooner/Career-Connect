package com.example.careerconnect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private boolean login = false; // Variable to track login status

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        ImageView appLogoImg = findViewById(R.id.app_logo_img);
//
//        ScaleAnimation scaler = new ScaleAnimation((float) 0.7, (float) 1.0, (float) 0.7, (float) 1.0);
//        scaler.setDuration(40);
//        appLogoImg.startAnimation(scaler);

        // Delay for 5 seconds before navigating
        new Handler().postDelayed(() -> {
            if (login) {
                // Navigate to JobListActivity
                startActivity(new Intent(MainActivity.this, JobListActivity.class));
            } else {
                // Navigate to LoginActivity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
            finish(); // Close MainActivity
        }, 5000); // 5000 milliseconds = 5 seconds
    }
}
