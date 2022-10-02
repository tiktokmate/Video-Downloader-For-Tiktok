package com.leetlab.videodownloaderfortiktok.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.adapter.PagerAdapter;
import com.leetlab.videodownloaderfortiktok.model.InstaContent;
import com.leetlab.videodownloaderfortiktok.ui.fragment.DownloadContentFragment;
import com.leetlab.videodownloaderfortiktok.utils.Admob;
import com.leetlab.videodownloaderfortiktok.utils.AppConstants;
import com.leetlab.admob.AdMobUtils;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadActivity extends AppCompatActivity {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.downloadPager)
    ViewPager downloadPager;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.adView)
    AdView adView;

    AdMobUtils adMobUtils;

    private File[] files;
    private List<InstaContent> videosUrl = new ArrayList<>();
    private List<InstaContent> photosUrl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);

        adMobUtils = new AdMobUtils(this, Admob.APP_ID);
        if (!Admob.isPremiumFeature())
            adMobUtils.loadBannerAd(adView, Admob.BANNER_ID, AdSize.BANNER);

        initAdapter();
    }

    public void initAdapter() {
        getDownloads();
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new DownloadContentFragment(photosUrl, AppConstants.TYPE_PHOTOS), "Photos");
        adapter.addFragment(new DownloadContentFragment(videosUrl, AppConstants.TYPE_VIDEOS), "Videos");

        downloadPager.setAdapter(adapter);
        tabs.setupWithViewPager(downloadPager);
    }

    private void getDownloads() {
        String path = AppConstants.DIRECTORY_PATH;
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                String filePath = file.getAbsolutePath();
                if (filePath.endsWith(".mp4")) {
                    videosUrl.add(new InstaContent(filePath, true, filePath));
                } else {
                    photosUrl.add(new InstaContent(filePath, false, filePath));
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        photosUrl.clear();
        videosUrl.clear();
        initAdapter();
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }
}
