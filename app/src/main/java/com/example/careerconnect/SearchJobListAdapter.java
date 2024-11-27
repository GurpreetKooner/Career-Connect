package com.example.careerconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SearchJobListAdapter extends RecyclerView.Adapter<SearchJobViewHolder>{

    Context context;
    List<Job> jobItems;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Job job);
    }

    public SearchJobListAdapter(Context context, List<Job> jobItems) {
        this.context = context;
        this.jobItems = jobItems;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public SearchJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchJobViewHolder(LayoutInflater.from(context).inflate(R.layout.search_job_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchJobViewHolder holder, int position) {
//        holder.jobTitle.setText(jobItems.get(position).getJobTitle());
//        holder.companyName.setText(jobItems.get(position).getCompanyName());
//        holder.location.setText(jobItems.get(position).getLocation());
//        holder.postedOn.setText(jobItems.get(position).getPostedOn());
        Job job = jobItems.get(position);
        holder.jobTitle.setText(job.title);
        holder.companyName.setText(job.company);
        holder.location.setText(job.location);

        String dateString = job.updated;

        // Parse the string to a LocalDateTime object, ignoring the fractional seconds
        LocalDateTime dateTime = LocalDateTime.parse(dateString.split("\\.")[0]);

        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        // Format the date
        String formattedDate = dateTime.format(formatter);
        String postedOnText = "Posted on " + formattedDate;
        holder.postedOn.setText(postedOnText);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(job);
            }
        });
//        holder.postedOn.setText(job.snippet);


//        if (jobItems.get(position).isSaved()){
//            holder.searchJobItemLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.yellow_outline_box));
//
//            holder.saveJobIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.save_black_icon));
//            holder.saveJob.setTextColor(ContextCompat.getColor(context, R.color.black));
//            holder.saveJob.setText("Saved");
//            holder.searchSaveJobLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
//
//            holder.bottomDivider.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
//
//            holder.jobDetails.setTextColor(ContextCompat.getColor(context, R.color.black));
//            holder.searchJobDetailsLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
//        }
    }

    @Override
    public int getItemCount() {
        return jobItems.size();
    }
}
