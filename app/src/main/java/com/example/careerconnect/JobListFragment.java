package com.example.careerconnect;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class JobListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job_list, container, false);

        // Create a list of job items
        List<JobItem> jobItems = new ArrayList<>();
        jobItems.add(new JobItem("Software Engineer, Android, IOS (All..", "Company", "Location", "Posted on dd/mm/yy"));
        jobItems.add(new JobItem("Software Engineer, Android, IOS (All..", "Company", "Location", "Posted on dd/mm/yy"));
        jobItems.add(new JobItem("Software Engineer, Android, IOS (All..", "Company", "Location", "Posted on dd/mm/yy"));
        jobItems.add(new JobItem("Software Engineer, Android, IOS (All..", "Company", "Location", "Posted on dd/mm/yy"));
        jobItems.add(new JobItem("Software Engineer, Android, IOS (All..", "Company", "Location", "Posted on dd/mm/yy"));
        jobItems.add(new JobItem("Software Engineer, Android, IOS (All..", "Company", "Location", "Posted on dd/mm/yy"));
        jobItems.add(new JobItem("Software Engineer, Android, IOS (All..", "Company", "Location", "Posted on dd/mm/yy"));
        jobItems.add(new JobItem("Software Engineer, Android, IOS (All..", "Company", "Location", "Posted on dd/mm/yy"));
        jobItems.add(new JobItem("Software Engineer, Android, IOS (All..", "Company", "Location", "Posted on dd/mm/yy"));

        // Set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.job_list); // Use 'view' to find the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Use 'getContext()' in fragments
        recyclerView.setAdapter(new JobListAdapter(getContext(), jobItems)); // Pass context and jobItems to the adapter

        return view;
    }
}
