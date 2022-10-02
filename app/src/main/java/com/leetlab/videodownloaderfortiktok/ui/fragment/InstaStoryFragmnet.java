package com.leetlab.videodownloaderfortiktok.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.adapter.StoriesAdapter;
import com.leetlab.videodownloaderfortiktok.model.StoryModel;
import com.leetlab.videodownloaderfortiktok.model.UserObject;
import com.leetlab.videodownloaderfortiktok.ui.activity.LoginActivity;
import com.leetlab.videodownloaderfortiktok.ui.activity.StoryViewActivity;
import com.leetlab.videodownloaderfortiktok.utils.AccountDB;
import com.leetlab.videodownloaderfortiktok.utils.InstaStoriesLoader;
import com.leetlab.videodownloaderfortiktok.utils.SavedPreferences;
import com.leetlab.videodownloaderfortiktok.utils.VolleyRequestUtil;
import com.leetlab.utils.Contants.Constants;
import com.leetlab.utils.dialog.ProgressDialog;
import com.leetlab.utils.dialog.ProgressType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InstaStoryFragmnet extends Fragment implements StoriesAdapter.StorySelectListener {

    private static final int RC_LOGIN = 121;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.listStories)
    RecyclerView listStories;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.lyt_account_login)
    ConstraintLayout lytAccountLogin;
    @BindView(R.id.btnLogin)
    ConstraintLayout btnLogin;
    @BindView(R.id.btn_insta_login)
    ConstraintLayout btnInstaLogin;

    private SavedPreferences savedPreferences;
    private List<UserObject> userObjectList = new ArrayList<>();
    private ProgressDialog dialog;
    private StoriesAdapter adapter;

    public InstaStoryFragmnet() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_insta_story_fragmnet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        dialog = new ProgressDialog(getActivity(), ProgressType.HORIZONTAL)
                .setMessage("Loading Stories...")
                .setRadius(8f);

        savedPreferences = new SavedPreferences();
        loadStories();

        swipeRefresh.setOnRefreshListener(() -> {
            swipeRefresh.setRefreshing(false);
            loadStories();
        });

        bindViews();

    }

    private void bindViews() {
        btnInstaLogin.setOnClickListener(loginListener);
        btnLogin.setOnClickListener(loginListener);
    }

    private View.OnClickListener loginListener = v -> {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivityForResult(intent, RC_LOGIN);
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadStories();
    }

    public void loadStories() {
        if (AccountDB.getAccounts().size() == 0) {
            lytAccountLogin.setVisibility(View.VISIBLE);
            swipeRefresh.setVisibility(View.GONE);
            return;
        }

        lytAccountLogin.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        listStories.setVisibility(View.GONE);
        userObjectList.clear();
        VolleyRequestUtil.init(getContext()).get(Constants.STORY_URL, AccountDB.getCurrentAccount(), (isSuccessFull, response) -> {
            if (isSuccessFull) {
                if (response.has("tray")) {
                    JSONArray array = response.getJSONArray("tray");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject userObj = array.getJSONObject(i);
                        if (userObj.has("user") && !userObj.isNull("user")) {
                            JSONObject ownerObject = userObj.getJSONObject("user");
                            UserObject userObject = savedPreferences.parsingUserObject(ownerObject);
                            userObjectList.add(userObject);
                        }
                    }

                    updateUI();
                }
            } else {
                Toast.makeText(getContext(), "Login Again", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void updateUI() {
        progressBar.setVisibility(View.GONE);
        listStories.setVisibility(View.VISIBLE);
        adapter = new StoriesAdapter(getContext(), userObjectList, this, false);
        listStories.setLayoutManager(new LinearLayoutManager(getContext()));
        listStories.setAdapter(adapter);
    }

    public void notifyUpdate() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStorySelect(UserObject userObject) {
        InstaStoriesLoader.init(getContext(), userObject, AccountDB.getCurrentAccount(), new InstaStoriesLoader.StoriesLoadListener() {
            @Override
            public void onStart() {
                dialog.show();
            }

            @Override
            public void onSuccessful(ArrayList<StoryModel> stories) {
                dialog.dismiss();
                Intent intent = new Intent(getContext(), StoryViewActivity.class);
                intent.putExtra("isFromNet", true);
                intent.putParcelableArrayListExtra("story_list", stories);
                intent.putExtra("user_model", userObject);
                startActivity(intent);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
