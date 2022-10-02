package com.leetlab.videodownloaderfortiktok.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.model.StoryModel;
import com.leetlab.utils.dialog.ProgressDialog;
import com.leetlab.utils.dialog.ProgressType;
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadOptionFragment extends SuperBottomSheetFragment {

    private ArrayList<StoryModel> list = new ArrayList<>();
    int currentPoss;
    private ProgressDialog dialog;
    private DownloadClickListener listener;

    public DownloadOptionFragment() {
    }

    public DownloadOptionFragment(DownloadClickListener listener) {
        this.listener = listener;
    }

    @BindView(R.id.btn_download)
    ConstraintLayout btn_download;
    @BindView(R.id.btn_download_all)
    ConstraintLayout btn_download_all;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_download_option, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        dialog = new ProgressDialog(getActivity(), ProgressType.HORIZONTAL)
                .setMessage("Start downloading...")
                .setRadius(8f);

    }

    @OnClick({R.id.btn_download, R.id.btn_download_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_download:
                getDialog().dismiss();
                if (listener != null) {
                    listener.onSingleDownload();
                }
                break;
            case R.id.btn_download_all:
                getDialog().dismiss();
                if (listener != null) {
                    listener.onDownloadAll();
                }
                break;
        }
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        listener.onDismiss();
    }

    @Override
    public int getPeekHeight() {
        return 430;
    }

    @Override
    public int getBackgroundColor() {
        return getContext().getResources().getColor(R.color.colorWhite);
    }

    public interface DownloadClickListener {
        void onSingleDownload();

        void onDownloadAll();

        void onDismiss();
    }
}
