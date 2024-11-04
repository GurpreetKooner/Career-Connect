package com.example.careerconnect;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface JobService {
    @Headers("Content-Type: application/json")
    @POST("https://jooble.org/api/fe4ea013-08e1-4ddc-b521-1a79fc3591b7")
    Call<JobResponse> getJobs(@Body JobRequest request);
}
