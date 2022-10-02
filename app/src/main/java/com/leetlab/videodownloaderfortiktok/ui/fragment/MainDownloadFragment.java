package com.leetlab.videodownloaderfortiktok.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.adapter.PagerAdapter;
import com.leetlab.videodownloaderfortiktok.model.InstaContent;
import com.leetlab.videodownloaderfortiktok.utils.AppConstants;
import com.leetlab.utils.Contants.Constants;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainDownloadFragment extends Fragment {


    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.downloadPager)
    ViewPager downloadPager;


    public MainDownloadFragment() {
    }

    private File[] files;
    private List<InstaContent> videosUrl = new ArrayList<>();
    private List<InstaContent> photosUrl = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_download, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initAdapter();

    }

    public void initAdapter() {
        photosUrl.clear();
        videosUrl.clear();
        getDownloads();
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        adapter.addFragment(new DownloadContentFragment(videosUrl, Constants.TYPE_VIDEOS), "Videos");
        adapter.addFragment(new DownloadContentFragment(photosUrl, Constants.TYPE_PHOTOS), "Mp3");
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



}
