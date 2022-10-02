package com.leetlab.videodownloaderfortiktok.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.adapter.PagerAdapter;
import com.leetlab.videodownloaderfortiktok.ui.fragment.PhotoVideosFragment;
import com.leetlab.videodownloaderfortiktok.ui.fragment.StorySaverFragment;
import com.leetlab.videodownloaderfortiktok.utils.AppConstants;
import com.leetlab.videodownloaderfortiktok.utils.SharedPrefManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    private static final int RC_LOGIN = 121;

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.homePager)
    ViewPager homePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setMainLayout();

    }

    private void setMainLayout() {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PhotoVideosFragment(), "Photos and Videos");
        adapter.addFragment(new StorySaverFragment(), "Insta Strories");
        adapter.addFragment(new PhotoVideosFragment(), "Insta DP");
        adapter.addFragment(new StorySaverFragment(), "History");
        homePager.setAdapter(adapter);
        tabs.setupWithViewPager(homePager);
    }

    private void downloadStories() {
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show();
            loadStories();
        } else {
            Toast.makeText(this, "Not Logged In", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, RC_LOGIN);
        }
    }

    private void loadStories() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppConstants.STORY_URL, null, (Response.Listener<JSONObject>) response -> {
            try {
                if (response.has("tray")) {
                    JSONArray array = response.getJSONArray("tray");
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject userObj = array.getJSONObject(i);
//
//                        if (userObj.has("user") && !userObj.isNull("user")) {
//
//                            JSONObject ownerObject = userObj.getJSONObject("user");
//
//                            UserObject userObject = savedPreferences.parsingUserObject(ownerObject);
//
//                            userObjectList.add(userObject);
//                        }
//                        adapter.notifyDataSetChanged();
//                        progressBar.setVisibility(View.GONE);
//
//                        Log.d("stories Size ===", userObjectList.size() + "");
//                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LOGIN && resultCode == RESULT_OK) {
            Toast.makeText(this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadContent(int type) {
        DownloadMediaActivity.run(this, 1);
    }
}
