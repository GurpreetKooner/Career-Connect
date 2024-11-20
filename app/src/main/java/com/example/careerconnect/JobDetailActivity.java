package com.example.careerconnect;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JobDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_job_detail);
        TextView jobTitle = findViewById(R.id.job_title);
        TextView companyName = findViewById(R.id.company_name);
        TextView postedOn = findViewById(R.id.posted_on);
        TextView jobDescription = findViewById(R.id.job_description);
        TextView jobLocation = findViewById(R.id.job_location);
        Button applyButton = findViewById(R.id.apply_button);

        String title = getIntent().getStringExtra("jobTitle");
        String company = getIntent().getStringExtra("jobCompany");
        String dateString = getIntent().getStringExtra("jobUpdated");
        String description = getIntent().getStringExtra("description");
        String location = getIntent().getStringExtra("jobLocation");

        description = formatString(description);

        // Parse the string to a LocalDateTime object, ignoring the fractional seconds
        LocalDateTime dateTime = LocalDateTime.parse(dateString.split("\\.")[0]);

        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        // Format the date
        String formattedDate = dateTime.format(formatter);
        String postedOnText = "Posted on " + formattedDate;


        Log.d("JobDetailActivity", "Job Title: " + title);

        applyButton.setOnClickListener(v -> {
            // Handle apply button click
        });

        jobTitle.setText(title);
        companyName.setText(company);
        postedOn.setText(postedOnText);
        jobLocation.setText(location);
        jobDescription.setText(description);

    }

    public static String formatString(String input) {
        // Replace HTML non-breaking spaces with regular spaces
        input = input.replaceAll("&nbsp;", " ");

        // Remove all HTML tags
        input = input.replaceAll("<[^>]+>", "");

        // Replace escape characters with line breaks or spaces
        input = input.replaceAll("\r\n", "\n"); // Replace with a newline for better formatting

        // Trim leading/trailing spaces and extra newlines
        input = input.trim();

        return input;
    }
}