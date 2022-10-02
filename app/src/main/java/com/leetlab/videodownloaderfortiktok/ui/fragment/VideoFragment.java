package com.leetlab.videodownloaderfortiktok.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.VideoView;

import com.leetlab.videodownloaderfortiktok.R;
import com.ornach.nobobutton.NoboButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class VideoFragment extends DialogFragment {


    @BindView(R.id.video)
    VideoView video;
    @BindView(R.id.btn_got_it)
    TextView btnGotIt;
    @BindView(R.id.closeBtn)
    NoboButton closeBtn;

    public VideoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

//        String path = "android.resource://" + getContext().getPackageName() + "/" + R.raw.video;
//        video.setVideoURI(Uri.parse(path));
//        video.start();
//        video.setOnPreparedListener(mp -> mp.setLooping(true));

    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    @OnClick({R.id.btn_got_it, R.id.closeBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_got_it:
                getDialog().dismiss();
                break;
            case R.id.closeBtn:
                getDialog().dismiss();
                break;
        }
    }
}