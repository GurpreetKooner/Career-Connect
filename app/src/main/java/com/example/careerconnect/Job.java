package com.example.careerconnect;

public class Job {
    String title;
    String location;
    String snippet;
    String company;
    String link;
    String updated;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String status; // Default status
    private boolean isSaved;

    private String firebaseDocumentId;

    private String jobDescription;

    // No-argument constructor (required for Firebase)
    public Job() {
    }

    // Parameterized constructor
    public Job(String jobTitle, String jobCompany, String jobLocation, String jobUpdated, String jobDescription, String jobUrl) {
        this.title = jobTitle;
        this.company = jobCompany;
        this.location = jobLocation;
        this.updated = jobUpdated;
        this.jobDescription = jobDescription;
        this.link = jobUrl;
    }
    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getCompany() {
        return company;
    }

    public String getLink() {
        return link;
    }

    public String getUpdated() {
        return updated;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getters and Setters
    public String getFirebaseDocumentId() {
        return firebaseDocumentId;
    }

    public void setFirebaseDocumentId(String firebaseDocumentId) {
        this.firebaseDocumentId = firebaseDocumentId;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }


}