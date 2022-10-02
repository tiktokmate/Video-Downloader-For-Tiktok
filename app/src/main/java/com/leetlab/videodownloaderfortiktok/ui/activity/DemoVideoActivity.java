package com.leetlab.videodownloaderfortiktok.ui.activity;

import android.os.Bundle;

import com.leetlab.videodownloaderfortiktok.R;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

public class DemoVideoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_video);
        ButterKnife.bind(this);


    }


}