package com.example.careerconnect;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchJobViewHolder extends RecyclerView.ViewHolder {
    TextView jobTitle, companyName, location, postedOn, saveJob, jobDetails;
    ImageView saveJobIcon;
    LinearLayout searchJobItemLayout, searchSaveJobLayout, searchJobDetailsLayout;
    View bottomDivider;

    public SearchJobViewHolder(@NonNull View itemView) {
        super(itemView);
        jobTitle = itemView.findViewById(R.id.search_job_title);
        companyName = itemView.findViewById(R.id.search_company_name);
        location = itemView.findViewById(R.id.search_location);
        postedOn = itemView.findViewById(R.id.search_posted_on);
        saveJob = itemView.findViewById(R.id.search_save_job);
        jobDetails = itemView.findViewById(R.id.search_job_details);
        saveJobIcon = itemView.findViewById(R.id.search_save_job_icon);
        searchJobItemLayout = itemView.findViewById(R.id.search_job_item_layout);
        searchSaveJobLayout = itemView.findViewById(R.id.search_save_job_layout);
        searchJobDetailsLayout = itemView.findViewById(R.id.search_job_details_layout);
        bottomDivider = itemView.findViewById(R.id.search_bottom_divider);

    }
}
