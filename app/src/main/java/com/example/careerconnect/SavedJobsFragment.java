
package com.example.careerconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SavedJobsFragment extends Fragment {
    private RecyclerView recyclerView;
    private SaveJobListAdapter jobAdapter;
    private List<Job> savedJobList = new ArrayList<>();
    private static final String TAG = "SavedJobsFragment";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Button clearButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Inflating the SavedJobsFragment layout");
        View view = inflater.inflate(R.layout.fragment_saved_jobs, container, false);

        // Initialize Firebase Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter
        jobAdapter = new SaveJobListAdapter(requireContext(), savedJobList);

        // Set item click listener to navigate to JobDetailActivity
        jobAdapter.setOnItemClickListener(job -> {
            Log.d(TAG, "Job clicked: " + job.getTitle());
            if (getContext() == null) {
                Log.e(TAG, "Context is null, cannot open JobDetailActivity");
                return;  // Avoid activity/fragment being null
            }
            // Navigate to JobDetailActivity when a job is clicked
            Intent intent = new Intent(requireContext(), JobDetailActivity.class);
            intent.putExtra("jobTitle", job.getTitle());
            intent.putExtra("jobCompany", job.getCompany());
            intent.putExtra("jobLocation", job.getLocation());
            intent.putExtra("jobUpdated", job.getUpdated());
            intent.putExtra("description", job.getSnippet());
            intent.putExtra("jobUrl", job.getLink());
            startActivity(intent);
        });

        // Set adapter to RecyclerView
        recyclerView.setAdapter(jobAdapter);
        Log.d(TAG, "onCreateView: RecyclerView setup complete");

        // Load saved jobs from Firebase
        loadSavedJobsFromFirebase();

        // Initialize the "Clear All Saved Jobs" button
        clearButton = view.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(v -> clearAllSavedJobs());

        return view;
    }

//    private void loadSavedJobsFromFirebase() {
//        Log.d(TAG, "loadSavedJobsFromFirebase: Loading saved jobs from Firestore");
//
//        // Retrieve saved jobs from Firestore based on the current user
//        db.collection("users")
//                .document(mAuth.getCurrentUser().getUid())
//                .collection("savedJobs")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        savedJobList.clear();  // Clear previous job list
//                        QuerySnapshot querySnapshot = task.getResult();
//                        for (QueryDocumentSnapshot document : querySnapshot) {
//                            // Extract job data from the document and create a Job object
//                            Job job = document.toObject(Job.class);  // Assuming Job class has a matching structure
//                            savedJobList.add(job);
//                            Log.d(TAG, "loadSavedJobsFromFirebase: Job added - " + job.getTitle());
//                        }
//                        jobAdapter.notifyDataSetChanged();  // Notify adapter that the data has changed
//                    } else {
//                        Log.e(TAG, "Error getting documents: ", task.getException());
//                    }
//                });
//    }

    private void loadSavedJobsFromFirebase() {
        Log.d(TAG, "loadSavedJobsFromFirebase: Loading saved jobs from Firestore");
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("savedJobs")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        savedJobList.clear();
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Job job = document.toObject(Job.class);

                            // Fetch status from Firestore document
                            String documentStatus = document.getString("status");
                            job.setStatus(documentStatus != null ? documentStatus : "Saved");

                            // Set Firebase document ID for future reference if needed
                            job.setFirebaseDocumentId(document.getId());

                            savedJobList.add(job);
                            Log.d(TAG, "loadSavedJobsFromFirebase: Job added - " + job.getTitle() + " (Status: " + job.getStatus() + ")");
                        }
                        jobAdapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    // Method to clear all saved jobs for the user
    private void clearAllSavedJobs() {
        Log.d(TAG, "clearAllSavedJobs: Removing all saved jobs from Firebase");

        // Remove all saved jobs from Firestore for the current user
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("savedJobs")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Delete each document
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Document successfully deleted"))
                                    .addOnFailureListener(e -> Log.e(TAG, "Error deleting document", e));
                        }
                        // After deleting, reload the list
                        savedJobList.clear();
                        jobAdapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Error getting documents for deletion: ", task.getException());
                    }
                });
    }
}

