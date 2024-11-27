package com.example.careerconnect;

public class User {
    private String id; // Firestore document ID
    private String name;
    private String email;
    private String company;
    private boolean isReferer;

    // Empty constructor required for Firestore
    public User() {}

    // Constructor
    public User(String id, String name, String email, String company, boolean isReferer) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.company = company;
        this.isReferer = isReferer;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean isReferer() {
        return isReferer;
    }

    public void setReferer(boolean referer) {
        isReferer = referer;
    }
}
