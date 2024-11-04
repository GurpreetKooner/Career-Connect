package com.example.careerconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobSearchFragment extends Fragment {

    SearchJobListAdapter jobAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Button loadMoreButton;
    List<Job> jobList = new ArrayList<>();
    int currentPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job_search, container, false);

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.search_job_list); // Use 'view' to find the RecyclerView
        progressBar = view.findViewById(R.id.loading_progress_bar);
        loadMoreButton = view.findViewById(R.id.load_more_button);

        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Use 'getContext()' in fragments

        fetchJobs(currentPage);
        loadMoreButton.setOnClickListener(v -> fetchJobs(++currentPage));
        return view;
    }

    private void fetchJobs(int page) {

        JobRequest request = new JobRequest("it", "US", page, 10);
        JobService jobService = RetrofitInstance.getRetrofitInstance().create(JobService.class);

        jobService.getJobs(request).enqueue(new Callback<JobResponse>() {
            @Override
            public void onResponse(Call<JobResponse> call, Response<JobResponse> response) {
                progressBar.setVisibility(View.GONE); // Hide loading bar

                if (response.isSuccessful() && response.body() != null) {
                    List<Job> newJobs = response.body().jobs;
                    if (newJobs.isEmpty()) {
                        loadMoreButton.setVisibility(View.GONE); // Hide if no more jobs
                    } else {
                        jobList.addAll(newJobs);
                        if (jobAdapter == null) {
                            jobAdapter = new SearchJobListAdapter(getContext(), jobList);
                            jobAdapter.setOnItemClickListener(job -> {
                                Intent intent = new Intent(getContext(), JobDetailActivity.class);
                                intent.putExtra("jobTitle", job.title);
//                                Log.d("JobDetailActivity", "Job Title: " + job.title);
                                intent.putExtra("jobCompany", job.company);
                                intent.putExtra("jobLocation", job.location);
                                intent.putExtra("jobUpdated", job.updated);
                                intent.putExtra("description", job.snippet);
                                startActivity(intent);
                            });
                            recyclerView.setAdapter(jobAdapter);
                        } else {
                            jobAdapter.notifyItemRangeInserted(jobList.size() - newJobs.size(), newJobs.size());
                        }
                        loadMoreButton.setVisibility(View.VISIBLE); // Show if more jobs are available
                    }
                }
            }

            @Override
            public void onFailure(Call<JobResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("API Error", "Failed to fetch jobs: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to fetch jobs", Toast.LENGTH_SHORT).show();
            }
        });
    }
}