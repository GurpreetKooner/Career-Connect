package com.example.careerconnect;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JobViewHolder extends RecyclerView.ViewHolder {
    TextView jobTitle, companyName, location, postedOn;
    View bottomDivider;

    public JobViewHolder(@NonNull View itemView) {
        super(itemView);
        jobTitle = itemView.findViewById(R.id.job_title);
        companyName = itemView.findViewById(R.id.company_name);
        location = itemView.findViewById(R.id.location);
        postedOn = itemView.findViewById(R.id.posted_on);
        bottomDivider = itemView.findViewById(R.id.bottom_divider);
    }
}
