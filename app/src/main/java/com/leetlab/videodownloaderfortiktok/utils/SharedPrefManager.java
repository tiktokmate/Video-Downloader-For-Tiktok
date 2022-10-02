package com.leetlab.videodownloaderfortiktok.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.leetlab.videodownloaderfortiktok.model.InstaUserModel;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "userDetails";
    public static final String KEY_NAME = "keyName";
    public static final String KEY_USERNAME = "keyUserName";
    public static final String KEY_PHOTO = "keyPhoto";
    public static final String KEY_COOKIE = "keyCookie";
    public static final String KEY_CRF_TOKEN = "keyToken";
    public static final String KEY_SESSION_ID = "keySessionId";
    public static final String KEY_URL_GEN = "keyUrlGen";
    public static final String KEY_DS_USER_ID = "keyDsUserId";
    public static final String KEY_MCD = "keyMcd";
    public static final String KEY_MID = "keyMid";

    // For Points

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static SharedPrefManager mInstance;
    public Context context;

    private SharedPrefManager(Context context) {

        this.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(InstaUserModel user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_PHOTO, user.getImage_url());
        editor.putString(KEY_COOKIE, user.getCookie());
        editor.putString(KEY_CRF_TOKEN, user.getCsrftoken());
        editor.putString(KEY_SESSION_ID, user.getSessionid());
        editor.putString(KEY_URL_GEN, user.getUrlgen());
        editor.putString(KEY_DS_USER_ID, user.getDs_user_id());
        editor.putString(KEY_MCD, user.getMcd());
        editor.putString(KEY_MID, user.getMid());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SESSION_ID, null) != null;
    }

    public InstaUserModel getUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        InstaUserModel userDetailModel = new InstaUserModel();
        userDetailModel.setName(sharedPreferences.getString(KEY_NAME, null));
        userDetailModel.setUsername(sharedPreferences.getString(KEY_USERNAME, null));
        userDetailModel.setImage_url(sharedPreferences.getString(KEY_PHOTO, null));
        userDetailModel.setCookie(sharedPreferences.getString(KEY_COOKIE, null));
        userDetailModel.setCsrftoken(sharedPreferences.getString(KEY_CRF_TOKEN, null));
        userDetailModel.setSessionid(sharedPreferences.getString(KEY_SESSION_ID, null));
        userDetailModel.setUrlgen(sharedPreferences.getString(KEY_URL_GEN, null));
        userDetailModel.setDs_user_id(sharedPreferences.getString(KEY_DS_USER_ID, null));
        userDetailModel.setMcd(sharedPreferences.getString(KEY_MCD, null));
        userDetailModel.setMid(sharedPreferences.getString(KEY_MID, null));
        return userDetailModel;
    }

    public void logout() {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
