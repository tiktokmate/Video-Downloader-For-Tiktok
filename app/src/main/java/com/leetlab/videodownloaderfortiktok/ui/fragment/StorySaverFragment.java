package com.leetlab.videodownloaderfortiktok.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.model.InstaUserModel;
import com.leetlab.videodownloaderfortiktok.model.UserObject;
import com.leetlab.videodownloaderfortiktok.utils.SavedPreferences;
import com.leetlab.videodownloaderfortiktok.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StorySaverFragment extends Fragment {

    @BindView(R.id.layout_not_logged_in)
    ConstraintLayout layoutNotLoggedIn;
    @BindView(R.id.recyclerview_strories)
    RecyclerView recyclerviewStrories;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private List<UserObject> userObjectList = new ArrayList<>();
    private SavedPreferences savedPreferences;
    private InstaUserModel currentUserModel;

    public StorySaverFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_story_saver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        savedPreferences = new SavedPreferences();
        currentUserModel = SharedPrefManager.getInstance(getContext()).getUser();


    }

//    private void loadStories() {
//        progressBar.setVisibility(View.VISIBLE);
//        userObjectList.clear();
//        String url = "https://i.instagram.com/api/v1/feed/reels_tray/";
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//
//                    if (response.has("tray")) {
//                        JSONArray array = response.getJSONArray("tray");
//                        for (int i = 0; i < array.length(); i++) {
//                            JSONObject userObj = array.getJSONObject(i);
//
//                            if (userObj.has("user") && !userObj.isNull("user")) {
//
//                                JSONObject ownerObject = userObj.getJSONObject("user");
//
//                                UserObject userObject = savedPreferences.parsingUserObject(ownerObject);
//
//                                userObjectList.add(userObject);
//                            }
//                            adapter.notifyDataSetChanged();
//                            progressBar.setVisibility(View.GONE);
//
//                            Log.d("stories Size ===", userObjectList.size() + "");
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("User-Agent", "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+");
//                headers.put("Cookie", "ds_user_id=" + currentUserModel.getDs_user_id() + ";sessionid=" + currentUserModel.getSessionid());
//
//                Log.d("Cookies ===", "ds_user_id=" + currentUserModel.getDs_user_id() + ";sessionid=" + currentUserModel.getSessionid());
//
//                return headers;
//            }
//        };
//
//        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
//    }
}
