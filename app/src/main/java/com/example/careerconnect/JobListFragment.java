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
        jobItems.add(new JobItem("Software Engineer, Android", "Tech Innovators Inc.", "New York, NY", "Posted on 01/11/24"));
        jobItems.add(new JobItem("iOS Developer", "App Creators Ltd.", "San Francisco, CA", "Posted on 28/10/24"));
        jobItems.add(new JobItem("Senior Mobile Engineer", "Dev Solutions", "Austin, TX", "Posted on 25/10/24"));
        jobItems.add(new JobItem("Full Stack Engineer, Mobile", "NextGen Apps", "Seattle, WA", "Posted on 30/10/24"));
        jobItems.add(new JobItem("Junior Android Developer", "SmartTech Systems", "Chicago, IL", "Posted on 02/11/24"));
        jobItems.add(new JobItem("iOS Engineer", "Mobile First Corp.", "Los Angeles, CA", "Posted on 29/10/24"));
        jobItems.add(new JobItem("Mobile Software Engineer", "Fast Apps Co.", "Denver, CO", "Posted on 27/10/24"));
        jobItems.add(new JobItem("Lead Mobile Developer", "FutureTech", "Boston, MA", "Posted on 03/11/24"));
        jobItems.add(new JobItem("Android Developer", "App Wizards", "Miami, FL", "Posted on 26/10/24"));

        // Set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.job_list); // Use 'view' to find the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Use 'getContext()' in fragments
        recyclerView.setAdapter(new JobListAdapter(getContext(), jobItems)); // Pass context and jobItems to the adapter

        return view;
    }
}
