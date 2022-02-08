package com.example.upbeatproject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiInterface {
    @GET("everything")
    Call<NewsApiResponse> getNewsItem(@Query("q") String q,@Query("excludeDomains") String excludeDomains,@Query("apiKey") String apiKey);
}
