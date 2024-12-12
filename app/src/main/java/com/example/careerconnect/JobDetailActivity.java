//package com.example.careerconnect;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.view.WindowCompat;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FieldValue;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class JobDetailActivity extends AppCompatActivity {
//
//    private FirebaseFirestore db;
//    private LinearLayout refererLayout;
//    private LinearLayout refererList;
//    private LinearLayout saveButton;
//    private boolean isJobSaved = false;
//    private String jobId;
//    private String currentUserId;
//
//    private TextView saveJob;
//
//    private ImageView saveJobIcon;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
//        setContentView(R.layout.activity_job_detail);
//
//        // Initialize Firestore
//        db = FirebaseFirestore.getInstance();
//        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        // Find views
//        TextView jobTitle = findViewById(R.id.job_title);
//        TextView companyName = findViewById(R.id.company_name);
//        TextView postedOn = findViewById(R.id.posted_on);
//        TextView jobDescription = findViewById(R.id.job_description);
//        LinearLayout applyButton = findViewById(R.id.apply_button);
//        saveJob = findViewById(R.id.search_save_job);
//        saveJobIcon = findViewById(R.id.search_save_job_icon);
//        saveButton = findViewById(R.id.save_button);
//        refererLayout = findViewById(R.id.referer_layout);
//        refererList = findViewById(R.id.referer_list);
//
//        // Get data from intent
//        jobId = getIntent().getStringExtra("jobId");
//        String title = getIntent().getStringExtra("jobTitle");
//        String company = getIntent().getStringExtra("jobCompany");
//        String description = getIntent().getStringExtra("description");
//        String link = getIntent().getStringExtra("jobUrl");
//
//        // Set data to views
//        jobTitle.setText(title);
//        companyName.setText(company);
//        jobDescription.setText(description);
//
//        // Check if job is already saved
//        checkIfJobIsSaved();
//
//        // Add click listener for Apply button
//        applyButton.setOnClickListener(v -> {
//            if (link != null && !link.isEmpty()) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
//                startActivity(intent);
//            } else {
//                Toast.makeText(this, "No link available for this job.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Add click listener for Save button
//        saveButton.setOnClickListener(v -> toggleJobSaveState());
//
//        // Load referrers for the company
//        loadReferrers(company);
//    }
//
//    private void checkIfJobIsSaved() {
//        DocumentReference userDocRef = db.collection("users").document(currentUserId);
//
//        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
//            if (documentSnapshot.exists()) {
//                List<String> savedJobs = (List<String>) documentSnapshot.get("savedJobs");
//                if (savedJobs != null && savedJobs.contains(jobId)) {
//                    isJobSaved = true;
//                }
//            }
//            updateSaveButtonState();
//        });
//    }
//
//    private void toggleJobSaveState() {
//        DocumentReference userDocRef = db.collection("users").document(currentUserId);
//
//        if (isJobSaved) {
//            userDocRef.update("savedJobs", FieldValue.arrayRemove(jobId))
//                    .addOnSuccessListener(aVoid -> {
//                        isJobSaved = false;
//                        updateSaveButtonState();
//                        Toast.makeText(this, "Job removed from saved list", Toast.LENGTH_SHORT).show();
//                    })
//                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to unsave job: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//        } else {
//            userDocRef.update("savedJobs", FieldValue.arrayUnion(jobId))
//                    .addOnSuccessListener(aVoid -> {
//                        isJobSaved = true;
//                        updateSaveButtonState();
//                        Toast.makeText(this, "Job saved successfully", Toast.LENGTH_SHORT).show();
//                    })
//                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to save job: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//        }
//    }
//
//    private void updateSaveButtonState() {
//        if (isJobSaved) {
//            saveJobIcon.setImageResource(R.drawable.save_white_icon);
//            saveJob.setText("saved");
//        } else {
//            saveJobIcon.setImageResource(R.drawable.save_black_icon);
//            saveJob.setText("Save");
//        }
//    }
//
//    private void loadReferrers(String company) {
//        db.collection("users")
//                .whereEqualTo("company", company)
//                .whereEqualTo("isReferer", true) // Filter users who opted in as referrers
//                .limit(10) // Limit results to 10
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    if (queryDocumentSnapshots.isEmpty()) {
//                        refererLayout.setVisibility(View.GONE);
//                    } else {
//                        refererLayout.setVisibility(View.VISIBLE);
//
//                        for (DocumentSnapshot document : queryDocumentSnapshots) {
//                            // Create User object with Firestore document ID
//                            User user = document.toObject(User.class);
//                            if (user != null) {
//                                user.setId(document.getId());
//                            }
//
//                            // Inflate and populate referer item view
//                            View referrerView = getLayoutInflater().inflate(R.layout.referer_item, refererList, false);
//                            ((TextView) referrerView.findViewById(R.id.referer_name)).setText(user.getName());
//                            ((TextView) referrerView.findViewById(R.id.referer_email)).setText(user.getEmail());
//
//                            // Set click listener to add the referer as a friend
//                            referrerView.setOnClickListener(v -> addRefererAsFriend(user));
//
//                            refererList.addView(referrerView);
//                        }
//                    }
//                })
//                .addOnFailureListener(e -> Toast.makeText(JobDetailActivity.this, "Failed to load referrers: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//    }
//
//    private void addRefererAsFriend(User user) {
//        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        // Update the 'friends' list field in the current user's document
//        db.collection("users")
//                .document(currentUserId)
//                .update("friends", FieldValue.arrayUnion(user.getId()))
//                .addOnSuccessListener(aVoid -> Toast.makeText(this, user.getName() + " added to your friend list!", Toast.LENGTH_SHORT).show())
//                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add friend: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//    }
//}

package com.example.careerconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobDetailActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private LinearLayout refererLayout;
    private LinearLayout refererList;
    private LinearLayout saveButton;
    private boolean isJobSaved = false;
    private TextView saveJob;
    private ImageView saveJobIcon;

    // Job details
    private String jobTitle;
    private String jobCompany;
    private String jobLocation;
    private String jobUpdated;
    private String jobDescription;
    private String jobUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_job_detail);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Find views
        TextView jobTitleView = findViewById(R.id.job_title);
        TextView companyName = findViewById(R.id.company_name);
        TextView job_Location = findViewById(R.id.job_location);
        TextView postedOn = findViewById(R.id.posted_on);
        TextView jobDescriptionView = findViewById(R.id.job_description);
        LinearLayout applyButton = findViewById(R.id.apply_button);
        saveJob = findViewById(R.id.search_save_job);
        saveJobIcon = findViewById(R.id.search_save_job_icon);
        saveButton = findViewById(R.id.save_button);
        refererLayout = findViewById(R.id.referer_layout);
        refererList = findViewById(R.id.referer_list);

        // Get data from intent
        jobTitle = getIntent().getStringExtra("jobTitle");
        jobCompany = getIntent().getStringExtra("jobCompany");
        jobLocation = getIntent().getStringExtra("jobLocation");
        jobUpdated = getIntent().getStringExtra("jobUpdated");
        jobDescription = getIntent().getStringExtra("description");
        jobUrl = getIntent().getStringExtra("jobUrl");
        isJobSaved = getIntent().getBooleanExtra("isSaved", false);


        LocalDateTime dateTime = LocalDateTime.parse(jobUpdated.split("\\.")[0]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = dateTime.format(formatter);
        String postedOnText = "Posted on " + formattedDate;

        // Set data to views
        jobTitleView.setText(jobTitle);
        companyName.setText(jobCompany);
        job_Location.setText(jobLocation);
        postedOn.setText(postedOnText);
        jobDescriptionView.setText("job description");

        // Check if user is logged in
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            // User is logged in - use Firestore
            checkJobSavedInFirestore();
        } else {
            // User is not logged in - use SharedPreferences
            checkJobSavedInSharedPreferences();
        }

        // Add click listener for Apply button
        applyButton.setOnClickListener(v -> {
            if (jobUrl != null && !jobUrl.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(jobUrl));
                startActivity(intent);
            } else {
                Toast.makeText(this, "No link available for this job.", Toast.LENGTH_SHORT).show();
            }
        });

        // Add click listener for Save button
        saveButton.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                saveJobInFirestore();
            } else {
                saveJobInSharedPreferences();
            }
        });

        // Load referrers for the company
        loadReferrers(jobCompany);
    }

    private void checkJobSavedInFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("savedJobs")
                .whereEqualTo("title", jobTitle)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    isJobSaved = !querySnapshot.isEmpty();
                    updateSaveButtonState();
                })
                .addOnFailureListener(e -> {
                    Log.e("JobDetailActivity", "Error checking saved job", e);
                });
    }

    private void saveJobInFirestore() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (isJobSaved) {
            // Remove job from saved jobs
            db.collection("users")
                    .document(mAuth.getCurrentUser().getUid())
                    .collection("savedJobs")
                    .whereEqualTo("title", jobTitle)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        for (DocumentSnapshot document : querySnapshot) {
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> {
                                        isJobSaved = false;
                                        updateSaveButtonState();
                                        Toast.makeText(this, "Job removed from saved", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to remove job", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    });
        } else {
            // Save job to Firestore
            Job jobToSave = new Job(jobTitle, jobCompany, jobLocation, jobUpdated, jobDescription, jobUrl);

            db.collection("users")
                    .document(mAuth.getCurrentUser().getUid())
                    .collection("savedJobs")
                    .add(jobToSave)
                    .addOnSuccessListener(documentReference -> {
                        isJobSaved = true;
                        updateSaveButtonState();
                        Toast.makeText(this, "Job saved successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to save job", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void checkJobSavedInSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("SavedJobs", Context.MODE_PRIVATE);
        isJobSaved = sharedPreferences.getBoolean(jobTitle, false);
        updateSaveButtonState();
    }

    private void saveJobInSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("SavedJobs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (isJobSaved) {
            // Remove job from saved jobs
            editor.remove(jobTitle);
            isJobSaved = false;
            Toast.makeText(this, "Job removed from saved", Toast.LENGTH_SHORT).show();
        } else {
            // Save job to SharedPreferences
            editor.putBoolean(jobTitle, true);
            isJobSaved = true;
            Toast.makeText(this, "Job saved", Toast.LENGTH_SHORT).show();
        }

        editor.apply();
        updateSaveButtonState();
    }

    private void updateSaveButtonState() {
        if (isJobSaved) {
            saveJobIcon.setImageResource(R.drawable.save_white_icon);
            saveJob.setText("saved");
        } else {
            saveJobIcon.setImageResource(R.drawable.save_black_icon);
            saveJob.setText("Save");
        }
    }

        private void loadReferrers(String company) {
        db.collection("users")
                .whereEqualTo("company", company)
                .whereEqualTo("isReferer", true) // Filter users who opted in as referrers
                .limit(10) // Limit results to 10
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        refererLayout.setVisibility(View.GONE);
                    } else {
                        refererLayout.setVisibility(View.VISIBLE);

                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            // Create User object with Firestore document ID
                            User user = document.toObject(User.class);
                            if (user != null) {
                                user.setId(document.getId());
                            }

                            // Inflate and populate referer item view
                            View referrerView = getLayoutInflater().inflate(R.layout.referer_item, refererList, false);
                            ((TextView) referrerView.findViewById(R.id.referer_name)).setText(user.getName());
                            ((TextView) referrerView.findViewById(R.id.referer_email)).setText(user.getEmail());

                            // Set click listener to add the referer as a friend
                            referrerView.setOnClickListener(v -> addRefererAsFriend(user));

                            refererList.addView(referrerView);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(JobDetailActivity.this, "Failed to load referrers: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void addRefererAsFriend(User user) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Update the 'friends' list field in the current user's document
        db.collection("users")
                .document(currentUserId)
                .update("friends", FieldValue.arrayUnion(user.getId()))
                .addOnSuccessListener(aVoid -> Toast.makeText(this, user.getName() + " added to your friend list!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add friend: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


}