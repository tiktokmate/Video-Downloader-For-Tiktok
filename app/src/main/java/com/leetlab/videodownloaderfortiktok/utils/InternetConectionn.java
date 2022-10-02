package com.leetlab.videodownloaderfortiktok.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConectionn {
    public static Boolean CheckInternetConnection(Context context) {
        Boolean connection = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (info != null) {
            connection = true;
        } else {
            connection = false;
        }

        return connection;

    }
}
