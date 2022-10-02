package com.leetlab.videodownloaderfortiktok.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    public static Retrofit getClient(Context context, boolean followRedirects) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://your.api.url/")
                .client(getOkHttp(context,followRedirects))
                // .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }

    private static OkHttpClient getOkHttp(final Context context, boolean followRedirects) {
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder().
                followRedirects(followRedirects).connectTimeout(3L, TimeUnit.MINUTES)
                .readTimeout(3L, TimeUnit.MINUTES)
                .writeTimeout(3L, TimeUnit.MINUTES);
        return httpClient.build();
    }
}
