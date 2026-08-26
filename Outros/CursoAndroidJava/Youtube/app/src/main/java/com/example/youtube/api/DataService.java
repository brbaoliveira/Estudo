package com.example.youtube.api;

import com.example.youtube.model.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DataService {

    @GET("youtube/v3/videos")
    Call<VideoResponse> recuperarVideo(
            @Query("part") String part,
            @Query("id") String id,
            @Query("key") String key
    );
}
