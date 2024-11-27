package com.example.careerconnect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobDetailActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private LinearLayout refererLayout;
    private LinearLayout refererList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_job_detail);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Find views
        TextView jobTitle = findViewById(R.id.job_title);
        TextView companyName = findViewById(R.id.company_name);
        TextView postedOn = findViewById(R.id.posted_on);
        TextView jobDescription = findViewById(R.id.job_description);
        refererLayout = findViewById(R.id.referer_layout);
        refererList = findViewById(R.id.referer_list);

        // Get data from intent
        String title = getIntent().getStringExtra("jobTitle");
        String company = getIntent().getStringExtra("jobCompany");
        String description = getIntent().getStringExtra("description");

        // Set data to views
        jobTitle.setText(title);
        companyName.setText(company);
        jobDescription.setText(description);

        // Load referrers for the company
        loadReferrers(company);
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
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, user.getName() + " added to your friend list!", Toast.LENGTH_SHORT).show();
                    // Optionally, add a message or take other actions
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add friend: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        db.collection("users")
                .document(user.getId())
                .update("friends", FieldValue.arrayUnion(currentUserId))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, user.getName() + " added to your friend list!", Toast.LENGTH_SHORT).show();
                    // Optionally, add a message or take other actions
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add friend: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void addStartingMessage(String currentUserId, String friendId) {
        Map<String, Object> message = new HashMap<>();
        message.put("sender", currentUserId);
        message.put("receiver", friendId);
        message.put("message", "Hi! Let's start chatting.");
        message.put("timestamp", System.currentTimeMillis());

        db.collection("users")
                .document(currentUserId)
                .collection("friends")
                .document(friendId)
                .collection("messages")
                .add(message)
                .addOnSuccessListener(aVoid -> Log.d("ChatSystem", "Starting message sent successfully"))
                .addOnFailureListener(e -> Log.e("ChatSystem", "Failed to send starting message: " + e.getMessage()));
    }
}
