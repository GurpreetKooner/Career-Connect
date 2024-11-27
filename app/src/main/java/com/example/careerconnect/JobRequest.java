package com.example.careerconnect;

public class JobRequest {
    String keywords;
    String location;

    int page;
    int count;

    public JobRequest(String keywords, String location, int page, int count) {
        this.keywords = keywords;
        this.location = location;
        this.page = page;
        this.count = count;
    }
}