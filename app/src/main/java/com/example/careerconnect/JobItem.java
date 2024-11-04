package com.example.careerconnect;

public class JobItem {
    String jobTitle;
    String companyName;
    String location;
    String postedOn;
    boolean isSaved;

    public JobItem(String jobTitle, String companyName, String location, String postedOn) {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.location = location;
        this.postedOn = postedOn;
    }

    public JobItem(String jobTitle, String companyName, String location, String postedOn, boolean isSaved) {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.location = location;
        this.postedOn = postedOn;
        this.isSaved = isSaved;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }

    public boolean isSaved() {
        return isSaved;
    }
}
