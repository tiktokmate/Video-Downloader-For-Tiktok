package com.leetlab.videodownloaderfortiktok.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.utils.Admob;
import com.leetlab.videodownloaderfortiktok.utils.AppConstants;
import com.leetlab.admob.AdMobUtils;
import com.leetlab.utils.info.InfoUtil;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.download_path_txt)
    TextView downloadPathTxt;
    @BindView(R.id.version_txt)
    TextView versionTxt;
    @BindView(R.id.adView)
    AdView adView;

    AdMobUtils adMobUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        adMobUtils = new AdMobUtils(this, Admob.APP_ID);
        if (!Admob.isPremiumFeature())
            adMobUtils.loadBannerAd(adView, Admob.BANNER_ID, AdSize.BANNER);

        downloadPathTxt.setText(AppConstants.DIRECTORY_PATH);
        versionTxt.setText("v1.0");

    }

    @OnClick({R.id.back_btn, R.id.update_layout, R.id.policy_layout, R.id.share_layout, R.id.rate_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.update_layout:
                InfoUtil.rateApp(this);
                break;
            case R.id.policy_layout:
                InfoUtil.openPrivacyPolicy(this, getResources().getString(R.string.privicy_policy_url));
                break;
            case R.id.share_layout:
                InfoUtil.shareApp(this, "Hey! I found a app for downloading photos and videos from Instagram, Check it out.");
                break;
            case R.id.rate_layout:
                InfoUtil.rateApp(this);
                break;
        }
    }
}
