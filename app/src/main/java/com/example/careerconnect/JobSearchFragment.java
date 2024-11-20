package com.example.careerconnect;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Import necessary for Drawable
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

public class JobSearchFragment extends Fragment {

    private SearchJobListAdapter jobAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Button loadMoreButton;
    private List<Job> jobList = new ArrayList<>();
    private int currentPage = 1;

    private EditText searchJobInput;
    private String currentKeyword = "IT"; // Default keyword

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job_search, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.search_job_list);
        progressBar = view.findViewById(R.id.loading_progress_bar);
        loadMoreButton = view.findViewById(R.id.load_more_button);
        searchJobInput = view.findViewById(R.id.search_job_input);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up the initial job list with default keyword
        fetchJobs(currentPage, currentKeyword);

        // Set up the touch listener for the search icon in the EditText
        searchJobInput.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Drawable drawableEnd = searchJobInput.getCompoundDrawables()[2];
                if (drawableEnd != null) {
                    int drawableWidth = drawableEnd.getBounds().width();
                    if (event.getRawX() >= (searchJobInput.getRight() - drawableWidth - searchJobInput.getPaddingRight())) {
                        // User tapped on the drawableEnd (search icon)
                        performSearch();
                        return true;
                    }
                }
            }
            return false;
        });

        // Load more button click listener
        loadMoreButton.setOnClickListener(v -> fetchJobs(++currentPage, currentKeyword));

        return view;
    }

    private void performSearch() {
        Log.d("JobSearchFragment", "Search icon clicked");
        // Reset the job list and current page
        jobList.clear();
        currentPage = 1;

        // Get the keyword from the search input
        String keyword = searchJobInput.getText().toString().trim();

        // If the search input is empty, use default keyword "IT"
        if (TextUtils.isEmpty(keyword)) {
            currentKeyword = "IT";
        } else {
            currentKeyword = keyword;
        }

        // Clear the adapter and notify data set changed
        if (jobAdapter != null) {
            jobAdapter.notifyDataSetChanged();
        }

        // Fetch jobs with the new keyword
        fetchJobs(currentPage, currentKeyword);
    }

    private void fetchJobs(int page, String keyword) {
        Log.d("JobSearchFragment", "Fetching jobs for keyword: " + keyword + ", page: " + page);
        progressBar.setVisibility(View.VISIBLE);

        JobRequest request = new JobRequest(keyword, "US", page, 10);
        JobService jobService = RetrofitInstance.getRetrofitInstance().create(JobService.class);

        jobService.getJobs(request).enqueue(new Callback<JobResponse>() {
            @Override
            public void onResponse(Call<JobResponse> call, Response<JobResponse> response) {
                progressBar.setVisibility(View.GONE); // Hide loading bar

                if (response.isSuccessful() && response.body() != null) {
                    List<Job> newJobs = response.body().jobs;
                    if (newJobs == null || newJobs.isEmpty()) {
                        if (currentPage == 1) {
                            // No jobs found for the keyword
                            Toast.makeText(getContext(), "No jobs found for '" + keyword + "'", Toast.LENGTH_SHORT).show();
                        }
                        loadMoreButton.setVisibility(View.GONE); // Hide if no more jobs
                    } else {
                        jobList.addAll(newJobs);
                        if (jobAdapter == null) {
                            jobAdapter = new SearchJobListAdapter(getContext(), jobList);
                            jobAdapter.setOnItemClickListener(job -> {
                                Intent intent = new Intent(getContext(), JobDetailActivity.class);
                                intent.putExtra("jobTitle", job.title);
                                intent.putExtra("jobCompany", job.company);
                                intent.putExtra("jobLocation", job.location);
                                intent.putExtra("jobUpdated", job.updated);
                                intent.putExtra("description", job.snippet);
                                startActivity(intent);
                            });
                            recyclerView.setAdapter(jobAdapter);
                        } else {
                            jobAdapter.notifyDataSetChanged();
                        }
                        loadMoreButton.setVisibility(View.VISIBLE); // Show if more jobs are available
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to fetch jobs", Toast.LENGTH_SHORT).show();
                    Log.e("JobSearchFragment", "Response unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<JobResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("JobSearchFragment", "Failed to fetch jobs: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to fetch jobs", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
