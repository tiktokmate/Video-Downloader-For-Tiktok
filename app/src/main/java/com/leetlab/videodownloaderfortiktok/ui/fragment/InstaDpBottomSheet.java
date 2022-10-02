package com.leetlab.videodownloaderfortiktok.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leetlab.videodownloaderfortiktok.utils.DownloadUtil;
import com.leetlab.admob.AdMobUtils;
import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.listener.DownloadDialogListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jackandphantom.circularimageview.RoundedImage;
import com.ornach.nobobutton.NoboButton;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InstaDpBottomSheet extends BottomSheetDialogFragment {


    @BindView(R.id.tvFullName2)
    TextView tvFullname;
    @BindView(R.id.tvUsername)
    TextView tvUserName;
    @BindView(R.id.img_dp)
    RoundedImage imgDp;
    @BindView(R.id.btn_download)
    TextView btnDownload;
    @BindView(R.id.closeBtn)
    NoboButton closeBtn;
    @BindView(R.id.imageView2)
    RoundedImage ivProfile;
    private AdMobUtils adMobUtils;

    private DownloadDialogListener listener;

    public InstaDpBottomSheet(DownloadDialogListener listener) {
        this.listener = listener;
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    private String Url = "";
    private String userName = "";

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_insta_dp_bottom_sheet, null);
        dialog.setContentView(contentView);
        ButterKnife.bind(this, contentView);
        Bundle bundle = getArguments();
        tvFullname.setText(bundle.getString("fullName"));
        tvUserName.setText(bundle.getString("username"));
        Url = bundle.getString("dp");
        userName = bundle.getString("username");
        Picasso.with(getContext()).load(bundle.getString("dp")).placeholder(R.drawable.ic_content_placeholder).into(imgDp);
        Picasso.with(getContext()).load(bundle.getString("dp")).into(ivProfile);
    }


    @OnClick({R.id.closeBtn, R.id.btn_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.closeBtn:
                getDialog().dismiss();
                break;
            case R.id.btn_download:
                DownloadUtil.downloadDp(getContext(), Url, userName);
                getDialog().dismiss();
                break;
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        listener.onDownloadFinish();
        super.onDismiss(dialog);
    }
}
