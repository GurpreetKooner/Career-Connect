package com.example.careerconnect;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class JobListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_list);
        View windowDecorView = getWindow().getDecorView();
        windowDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

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

        RecyclerView recyclerView = findViewById(R.id.job_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new JobListAdapter(this, jobItems));
    }
}