package com.example.careerconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JobListAdapter extends RecyclerView.Adapter<JobViewHolder>{

    Context context;
    List<JobItem> jobItems;

    public JobListAdapter(Context context, List<JobItem> jobItems) {
        this.context = context;
        this.jobItems = jobItems;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JobViewHolder(LayoutInflater.from(context).inflate(R.layout.job_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {

        holder.jobTitle.setText(jobItems.get(position).getJobTitle());
        holder.companyName.setText(jobItems.get(position).getCompanyName());
        holder.location.setText(jobItems.get(position).getLocation());
        holder.postedOn.setText(jobItems.get(position).getPostedOn());
        holder.bottomDivider.setVisibility(position == jobItems.size() - 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return jobItems.size();
    }
}
