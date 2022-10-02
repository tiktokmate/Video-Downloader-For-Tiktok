package com.leetlab.videodownloaderfortiktok.api;
import com.leetlab.videodownloaderfortiktok.utils.Downloadtiktokvideos;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;


public interface ApiService {

    // Fetch Config
    @POST
    @FormUrlEncoded
    Call<Downloadtiktokvideos> getAPI(@Url String url, @Field("url") String tiktokurl);

}