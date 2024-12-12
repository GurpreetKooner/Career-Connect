package com.example.careerconnect;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SaveJobViewHolder extends RecyclerView.ViewHolder {
    TextView jobTitle, companyName, location, postedOn, saveJob, jobDetails,jobStatus;
    ImageView saveJobIcon;
    LinearLayout searchJobDetailsLayout, updateJobLayout;
    View bottomDivider;

    public SaveJobViewHolder(@NonNull View itemView) {
        super(itemView);
        jobTitle = itemView.findViewById(R.id.search_job_title);
        companyName = itemView.findViewById(R.id.search_company_name);
        location = itemView.findViewById(R.id.search_location);
        postedOn = itemView.findViewById(R.id.search_posted_on);
        saveJob = itemView.findViewById(R.id.search_save_job);
        jobDetails = itemView.findViewById(R.id.search_job_details);
        updateJobLayout = itemView.findViewById(R.id.update_save_job_layout);
        jobStatus = itemView.findViewById(R.id.job_status);
        searchJobDetailsLayout = itemView.findViewById(R.id.search_job_details_layout);
        bottomDivider = itemView.findViewById(R.id.search_bottom_divider);

    }
}
