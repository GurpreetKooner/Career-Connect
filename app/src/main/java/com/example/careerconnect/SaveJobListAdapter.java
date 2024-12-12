//package com.example.careerconnect;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//
//import androidx.annotation.NonNull;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//public class SearchJobListAdapter extends RecyclerView.Adapter<SearchJobViewHolder>{
//
//    Context context;
//    List<Job> jobItems;
//    private OnItemClickListener onItemClickListener;
//
//    public interface OnItemClickListener {
//        void onItemClick(Job job);
//    }
//
//    public SearchJobListAdapter(Context context, List<Job> jobItems) {
//        this.context = context;
//        this.jobItems = jobItems;
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.onItemClickListener = listener;
//    }
//
//    @NonNull
//    @Override
//    public SearchJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new SearchJobViewHolder(LayoutInflater.from(context).inflate(R.layout.search_job_item_view, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SearchJobViewHolder holder, int position) {
////        holder.jobTitle.setText(jobItems.get(position).getJobTitle());
////        holder.companyName.setText(jobItems.get(position).getCompanyName());
////        holder.location.setText(jobItems.get(position).getLocation());
////        holder.postedOn.setText(jobItems.get(position).getPostedOn());
//        Job job = jobItems.get(position);
//        holder.jobTitle.setText(job.title);
//        holder.companyName.setText(job.company);
//        holder.location.setText(job.location);
//
//        String dateString = job.updated;
//
//        // Parse the string to a LocalDateTime object, ignoring the fractional seconds
//        LocalDateTime dateTime = LocalDateTime.parse(dateString.split("\\.")[0]);
//
//        // Define the desired format
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//
//        // Format the date
//        String formattedDate = dateTime.format(formatter);
//        String postedOnText = "Posted on " + formattedDate;
//        holder.postedOn.setText(postedOnText);
//
//        holder.itemView.setOnClickListener(v -> {
//            if (onItemClickListener != null) {
//                onItemClickListener.onItemClick(job);
//            }
//        });
////        holder.postedOn.setText(job.snippet);
//
//
////        if (jobItems.get(position).isSaved()){
////            holder.searchJobItemLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.yellow_outline_box));
////
////            holder.saveJobIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.save_black_icon));
////            holder.saveJob.setTextColor(ContextCompat.getColor(context, R.color.black));
////            holder.saveJob.setText("Saved");
////            holder.searchSaveJobLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
////
////            holder.bottomDivider.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
////
////            holder.jobDetails.setTextColor(ContextCompat.getColor(context, R.color.black));
////            holder.searchJobDetailsLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
////        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return jobItems.size();
//    }
//}

package com.example.careerconnect;

import static com.google.android.material.internal.ViewUtils.getBackgroundColor;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SaveJobListAdapter extends RecyclerView.Adapter<SaveJobViewHolder> {

    Context context;
    List<Job> jobItems;
    private OnItemClickListener onItemClickListener;
    private OnSaveJobClickListener onSaveJobClickListener; // Added for save button

    // Interface for item click
    public interface OnItemClickListener {
        void onItemClick(Job job);
    }

    // Interface for save button click
    public interface OnSaveJobClickListener {
        void onSaveJobClick(int position);
    }

    public SaveJobListAdapter(Context context, List<Job> jobItems) {
        this.context = context;
        this.jobItems = jobItems;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnSaveJobClickListener(OnSaveJobClickListener listener) {
        this.onSaveJobClickListener = listener; // Added to set save button click listener
    }

    @NonNull
    @Override
    public SaveJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SaveJobViewHolder(LayoutInflater.from(context).inflate(R.layout.save_job_item_view, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull SaveJobViewHolder holder, int position) {
        Job job = jobItems.get(position);
        holder.jobTitle.setText(job.title);
        holder.companyName.setText(job.company);
        holder.location.setText(job.location);
        holder.jobStatus.setText(job.status);

       // Set background color based on status
        int backgroundColor = getBackgroundColor(job.status);
       holder.jobStatus.setBackgroundResource(backgroundColor);

        // Format the date for display
        String dateString = job.updated;
        LocalDateTime dateTime = LocalDateTime.parse(dateString.split("\\.")[0]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = dateTime.format(formatter);
        String postedOnText = "Posted on " + formattedDate;
        holder.postedOn.setText(postedOnText);

        // Set item click listener
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(job);
            }
        });

        // Add click listener for update job layout
        holder.updateJobLayout.setOnClickListener(v -> {
            createUpdateJobStatusDialog(context, holder, job).show();
        });

    }

    private AlertDialog createUpdateJobStatusDialog(Context context, SaveJobViewHolder holder, Job job) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_job_status, null);
        RadioGroup statusRadioGroup = dialogView.findViewById(R.id.status_radio_group);

        return builder.setTitle("Update Job Status")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    int selectedStatusId = statusRadioGroup.getCheckedRadioButtonId();
                    String selectedStatus = getSelectedStatus(selectedStatusId);

                    updateJobStatusUI(holder, selectedStatus);
                    updateJobStatusInFirestore(job, selectedStatus);
                })
                .setNegativeButton("Cancel", null)
                .create();
    }

    private String getSelectedStatus(int selectedStatusId) {
        if (selectedStatusId == R.id.status_applied) return "Applied";
        if (selectedStatusId == R.id.status_saved) return "Saved";
        if (selectedStatusId == R.id.status_contacted) return "Contacted";
        if (selectedStatusId == R.id.status_hired) return "Hired";
        if (selectedStatusId == R.id.status_rejected) return "Rejected";
        return "Saved";
    }

    private void updateJobStatusUI(SaveJobViewHolder holder, String status) {
        holder.jobStatus.setText(status);

        int backgroundColor = getBackgroundColor(status);
        holder.jobStatus.setBackgroundResource(backgroundColor);
    }

    private static int getBackgroundColor(String status) {
        switch (status) {
            case "Applied": return R.color.blue;
            case "Contacted": return R.color.orange;
            case "Hired": return R.color.green_light;
            case "Rejected": return R.color.red_light;
            default: return R.color.sky_blue;
        }
    }

    private void updateJobStatusInFirestore(Job job, String status) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("savedJobs")
                .whereEqualTo("title", job.title)
                .whereEqualTo("company", job.company)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        queryDocumentSnapshots.getDocuments().get(0).getReference()
                                .update("status", status)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Job status updated to " + status, Toast.LENGTH_SHORT).show();
                                });
                    }
                });
    }


    @Override
    public int getItemCount() {
        return jobItems.size();
    }
}



