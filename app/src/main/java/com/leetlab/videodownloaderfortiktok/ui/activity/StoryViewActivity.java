package com.leetlab.videodownloaderfortiktok.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.model.StoryModel;
import com.leetlab.videodownloaderfortiktok.model.UserMediaModel;
import com.leetlab.videodownloaderfortiktok.model.UserObject;
import com.leetlab.videodownloaderfortiktok.ui.fragment.DownloadOptionFragment;
import com.leetlab.videodownloaderfortiktok.utils.Admob;
import com.leetlab.videodownloaderfortiktok.utils.DownloadUtil;
import com.leetlab.videodownloaderfortiktok.utils.widget.StoriesProgressView;
import com.leetlab.admob.AdMobUtils;
import com.leetlab.utils.dialog.ProgressDialog;
import com.leetlab.utils.dialog.ProgressType;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdSize;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class StoryViewActivity extends AppCompatActivity implements DownloadOptionFragment.DownloadClickListener, StoriesProgressView.StoriesListener, View.OnClickListener {


    public static String TAG = "StoryViewActivity";

    private StoriesProgressView storiesProgressView;
    private View reverse;
    private View skip;
    private ImageView story_image;
    private VideoView story_video;
    private FloatingActionButton fabDownload;
    private ArrayList<StoryModel> mStoryList;
    private int totalStories = -1;
    private int currentCount = 0;
    private Context mContext;
    long pressTime = 0L;
    long limit = 500L;
    private ProgressBar progressBar;
    private ImageView btn_back;
    private ImageView iv_profile_image;
    private TextView tv_profile_name;
    private ImageView iv_single_download;
    private ImageView iv_all_download;
    private UserObject mUserModel;

    private ProgressDialog dialog;
    private AdMobUtils adMobUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_view);

        mContext = StoryViewActivity.this;
        bindViews();
        bindData();

        dialog = new ProgressDialog(this, ProgressType.HORIZONTAL)
                .setMessage("Start downloading...")
                .setRadius(8f);

        adMobUtils = new AdMobUtils(this, Admob.APP_ID);
        adMobUtils.initInterstitialAd(Admob.INTESTITIAL_ID);

        loadBannerAd();
    }

    private void loadBannerAd() {
        if (Admob.isPremiumFeature()) {
            return;
        }

        LinearLayout adContainer = findViewById(R.id.adContainer);
        adMobUtils.loadBannerAd(adContainer, Admob.BANNER_ID, AdSize.BANNER);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skip:
                storiesProgressView.skip();
                break;
            case R.id.reverse:
                storiesProgressView.reverse();
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.fabDownload:
//                Toast.makeText(mContext, "Download", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onClick: " + new Gson().toJson(mStoryList));
                Log.e(TAG, "onClick: " + currentCount);
                storiesProgressView.pause();
                DownloadOptionFragment asFragment = new DownloadOptionFragment(this);
                asFragment.show(getSupportFragmentManager(), "sheet");
                break;
        }
    }

    private void bindViews() {
        storiesProgressView = findViewById(R.id.stories);
        reverse = findViewById(R.id.reverse);
        skip = findViewById(R.id.skip);
        story_image = findViewById(R.id.story_image);
        story_video = findViewById(R.id.story_video);
        progressBar = findViewById(R.id.progressBar);
        fabDownload = findViewById(R.id.fabDownload);
        fabDownload.setOnClickListener(this);
        btn_back = findViewById(R.id.btn_back);
        iv_profile_image = findViewById(R.id.iv_profile_image);
        tv_profile_name = findViewById(R.id.tv_profile_name);
        btn_back.setOnClickListener(this);
        reverse.setOnClickListener(this);
        reverse.setOnTouchListener(onTouchListener);

        skip.setOnClickListener(this);
        skip.setOnTouchListener(onTouchListener);
    }

    private void bindData() {
        mStoryList = getIntent().getParcelableArrayListExtra("story_list");
        mUserModel = (UserObject) getIntent().getSerializableExtra("user_model");

        if (mUserModel != null) {
            tv_profile_name.setText(mUserModel.getRealName());
            Glide.with(mContext).load(mUserModel.getImage()).thumbnail(0.2f).into(iv_profile_image);
        } else {
            tv_profile_name.setText("");
        }
        if (mStoryList != null) {
            totalStories = mStoryList.size();

            storiesProgressView.setStoriesCount(mStoryList.size());
            storiesProgressView.setStoryDuration(8000L);
            storiesProgressView.setStoriesListener(this);
            loadStories();
            storiesProgressView.startStories(currentCount);
        }

    }

    private void loadStories() {
        if (mStoryList != null) {
            if (mStoryList.get(currentCount).getType() == 0) {
                progressBar.setVisibility(View.VISIBLE);
                story_image.setVisibility(View.GONE);
                story_video.setVisibility(View.GONE);
                Glide.with(mContext).asBitmap().
                        load(mStoryList.get(currentCount).getFilePath()).
                        thumbnail(0.5f).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                Transition<? super Bitmap> transition) {
                        storiesProgressView.resume();
                        story_image.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        story_image.setImageBitmap(resource);
                    }
                });

            } else {
                progressBar.setVisibility(View.VISIBLE);
                story_video.setVisibility(View.VISIBLE);
                story_image.setVisibility(View.GONE);
                story_video.setVideoPath(mStoryList.get(currentCount).getFilePath());
                story_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        story_video.start();
                        story_video.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });


            }
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        finish();
//        overridePendingTransition(R.anim.enter_signin, R.anim.exit_main);
    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }

    @Override
    public void onNext() {
        if (currentCount < mStoryList.size()) {
            currentCount++;
            loadStories();
        } else {


            finish();
        }

    }

    @Override
    public void onPrev() {

        if (currentCount > 0) {
            currentCount--;
            loadStories();
        }

    }

    @Override
    public void onComplete() {

        finish();
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    @Override
    public void onSingleDownload() {
        dialog.show();
        if (mStoryList != null) {
            StoryModel storyModel = mStoryList.get(currentCount);
            if (storyModel != null) {
                UserMediaModel mediaModel = new UserMediaModel();
                mediaModel.setMediaUrl(storyModel.getFilePath());
                if (storyModel.getType() == 1) {
                    mediaModel.setVideo(true);
                } else {
                    mediaModel.setVideo(false);
                }
                ArrayList<UserMediaModel> mediaList = new ArrayList<>();
                mediaList.add(mediaModel);

                DownloadUtil.downloadStories(this, mediaList, mUserModel.getUserName());

                new Handler().postDelayed(() -> {
                    dialog.dismiss();
                    loadAd();
                    storiesProgressView.resume();
                }, 1000);
            }
        }
    }

    @Override
    public void onDownloadAll() {
        dialog.show();
        if (mStoryList != null) {
            ArrayList<UserMediaModel> mediaList = new ArrayList<>();
            for (int i = 0; i < mStoryList.size(); i++) {
                StoryModel storyModel = mStoryList.get(i);
                UserMediaModel mediaModel = new UserMediaModel();
                mediaModel.setMediaUrl(storyModel.getFilePath());
                if (storyModel.getType() == 1) {
                    mediaModel.setVideo(true);
                } else {
                    mediaModel.setVideo(false);
                }
                mediaList.add(mediaModel);
            }

            DownloadUtil.downloadStories(this, mediaList, mUserModel.getUserName());

            new Handler().postDelayed(() -> {
                dialog.dismiss();
                loadAd();
                storiesProgressView.resume();
            }, 1000);

        }
    }

    private void loadAd() {
        adMobUtils.showInterstitialAd(null);
    }

    @Override
    public void onDismiss() {
        storiesProgressView.resume();
    }
}
