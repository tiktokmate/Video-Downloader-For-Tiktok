package com.leetlab.videodownloaderfortiktok.utils.volley;
import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class VolleyUtil {

    private static final String TAG = "VolleyRequest";
    private RequestQueue queue;

    private VolleyUtil(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static VolleyUtil init(Context context){
        return new VolleyUtil(context);
    }

    public void get(String url, RequestCompleteListener listener){
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                listener.onResponse(true, new JSONObject(response));
            } catch (JSONException e) {
                try {
                    listener.onResponse(false, null);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Log.e(TAG, "ERROR: " +  e.getMessage());
            }
        }, error -> {
            try {
                listener.onResponse(false, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "ERROR: " +  error.getMessage());
        });
        queue.add(request);
    }

    public interface RequestCompleteListener {
        void onResponse(boolean isSuccessFull, JSONObject response) throws JSONException;
    }
}
