package com.leetlab.videodownloaderfortiktok.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.utils.AppConstants;
import com.leetlab.videodownloaderfortiktok.utils.PaperUtil;
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment;
import com.leetlab.utils.Contants.Constants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InfoFragment extends SuperBottomSheetFragment {

    public InfoFragment() {
    }

    @BindView(R.id.update_layout)
    ConstraintLayout update_layout;
    @BindView(R.id.policy_layout)
    ConstraintLayout policy_layout;
    @BindView(R.id.share_layout)
    ConstraintLayout share_layout;
    @BindView(R.id.rate_layout)
    ConstraintLayout rate_layout;
    @BindView(R.id.HowItWorks)
    ConstraintLayout HowItWorks;
    @BindView(R.id.auto_download)
    Switch auto_download;

    @BindView(R.id.policy_layout1)
    View policy_layout1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q ) {
            policy_layout1.setVisibility(View.GONE);
        }else{
            policy_layout1.setVisibility(View.GONE);
        }


        auto_download.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                PaperUtil.setAutoDownloadEnable();
            } else {
                PaperUtil.setAutoDownloadDisable();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (PaperUtil.isAutoDownload()) {
            auto_download.setChecked(true);
        } else {
            auto_download.setChecked(false);
        }
    }

    @OnClick({R.id.update_layout, R.id.policy_layout, R.id.share_layout, R.id.rate_layout, R.id.HowItWorks})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.update_layout:
                AppConstants.rateApp(getActivity());
                getDialog().dismiss();
                break;
            case R.id.policy_layout:
                AppConstants.openPrivacyPolicy(getActivity(), getString(R.string.privicy_policy_url));
                getDialog().dismiss();
                break;
            case R.id.share_layout:
                AppConstants.shareApp(getActivity(), "Download this app for download instagram Stories, profile picture, Photos and Videos ");
                getDialog().dismiss();
                break;
            case R.id.rate_layout:
                AppConstants.rateApp(getActivity());
                getDialog().dismiss();
                break;
            case R.id.HowItWorks:
                AppConstants.watchYoutubeVideo(getContext(), Constants.YOUTUBE_VIDEO_ID);
                break;
        }
    }
}
