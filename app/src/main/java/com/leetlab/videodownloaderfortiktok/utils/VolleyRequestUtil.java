package com.leetlab.videodownloaderfortiktok.utils;

import android.content.Context;

import com.leetlab.videodownloaderfortiktok.model.InstaUserModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyRequestUtil {

    private static final String TAG = "VolleyRequest";
    private RequestQueue queue;

    private VolleyRequestUtil(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static VolleyRequestUtil init(Context context) {
        return new VolleyRequestUtil(context);
    }

    public void get(String url, InstaUserModel instaUserModel, RequestCompleteListener listener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    listener.onResponse(true, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        listener.onResponse(false, null);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }, error -> {
            error.printStackTrace();
            try {
                listener.onResponse(false, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+");
                headers.put("Cookie", "ds_user_id=" + instaUserModel.getDs_user_id() + ";sessionid=" + instaUserModel.getSessionid());
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public interface RequestCompleteListener {
        void onResponse(boolean isSuccessFull, JSONObject response) throws JSONException;
    }

}
