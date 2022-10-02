package com.leetlab.videodownloaderfortiktok.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.adapter.DownloadContentAdapter;
import com.leetlab.videodownloaderfortiktok.listener.DownloadClickListener;
import com.leetlab.videodownloaderfortiktok.model.InstaContent;
import com.leetlab.videodownloaderfortiktok.ui.activity.Main2Activity;
import com.leetlab.videodownloaderfortiktok.ui.activity.PreviewActivity;
import com.leetlab.videodownloaderfortiktok.utils.AppConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadContentFragment extends Fragment implements DownloadClickListener {

    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.noRecordTxt)
    TextView noRecordTxt;
    @BindView(R.id.btnDelete1)
    Button btnDelete1;
    @BindView(R.id.btn_share)
    FloatingActionButton btnShare;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.btn_count)
    ConstraintLayout btnCount;
    @BindView(R.id.btn_delete)
    FloatingActionButton btnDelete;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private List<InstaContent> contents = new ArrayList<>();
    private int type;
    private DownloadContentAdapter adapter;
    private Main2Activity activity;
    private Boolean state = false;

    private File[] files;
    private List<InstaContent> videosUrl = new ArrayList<>();
    private List<InstaContent> photosUrl = new ArrayList<>();

    public DownloadContentFragment(List<InstaContent> uris, int type) {
        this.contents = uris;
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_download_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        swipeRefresh.setOnRefreshListener(this::refreshGellary);
        noRecordTxt.setText((type == AppConstants.TYPE_VIDEOS) ? "No Videos Download" : "No Photos Downloaded");
        if (contents.isEmpty()) {
            rvContent.setVisibility(View.GONE);
            return;
        }
        activity = ((Main2Activity) getContext());
        swipeRefresh.setOnRefreshListener(this::refreshGellary);
        Collections.reverse(contents);
        rvContent.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new DownloadContentAdapter(getContext(), contents, this);
        rvContent.setAdapter(adapter);
    }

    private void refreshGellary() {
        Log.e("TAG", "refreshGellary:");
        if (!contents.isEmpty()) {
            contents.clear();
        }
        updateList();
        swipeRefresh.setRefreshing(false);
    }

    private void updateList() {
        getDownloads();
        if (type == 1) {
            if (!photosUrl.isEmpty()) {
                Collections.reverse(photosUrl);
                contents.addAll(photosUrl);
                adapter.notifyDataSetChanged();
//                rvContent.setVisibility(View.GONE);z
            }
        } else {
            if (!videosUrl.isEmpty()) {
                Collections.reverse(videosUrl);
                contents.addAll(videosUrl);
                adapter.notifyDataSetChanged();
                //    rvContent.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void openFullPreview(int position) {
        if (type == AppConstants.TYPE_VIDEOS) {
            Intent intent = new Intent(getContext(), PreviewActivity.class);
            intent.putParcelableArrayListExtra(AppConstants.PREVIEW_OBJ, (ArrayList<? extends Parcelable>) contents);
            intent.putExtra(AppConstants.PREVIEW_POSITION, position);
            intent.putExtra(AppConstants.CONTENT_TYPE, type);
            startActivityForResult(intent, Main2Activity.RC_Download);
        } else {

            Uri uri = Uri.parse(contents.get(position).url);
            Intent i = new Intent();
            i.setAction("android.intent.action.VIEW");
            i.addFlags(1);
            i.setDataAndType(uri, "audio/*");
            startActivity(i);

        }
    }

    @Override
    public void onClick(InstaContent content, int position) {
        if (!adapter.isSelectableActive()) {
            openFullPreview(position);
            return;
        }
        toggleSelection(position);
    }

    @Override
    public void onLongClickListener(InstaContent content, int position) {
        toggleActionMode(position);
    }

    @SuppressLint("RestrictedApi")
    private void toggleSelection(int pos) {
        adapter.toggleSelection(pos);
        int count = adapter.getSelectedItem();
        if (count == 0) {
            adapter.clearSelections();
            btnShare.setVisibility(View.GONE);
            btnCount.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        } else {
            btnShare.setVisibility(View.VISIBLE);
            btnCount.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            tvCount.setText("" + count);
        }
    }

    @SuppressLint("RestrictedApi")
    private void toggleActionMode(int pos) {
        if (!adapter.isSelectableActive()) {
            adapter.enableSelectionMode();
        }
        btnShare.setVisibility(View.VISIBLE);
        btnCount.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.VISIBLE);
        toggleSelection(pos);
    }

    @SuppressLint("RestrictedApi")
    @OnClick({R.id.btn_share, R.id.btn_count, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_share:
                List<File> files = new ArrayList<>();
                if (!files.isEmpty())
                    files.clear();
                for (int i = 0; i < contents.size(); i++) {
                    if (adapter.getSelectedItems().get(i)) {
                        InstaContent content = contents.get(i);
                        files.add(new File(content.url));
                    }
                }
                AppConstants.shareMultipleFiles(files, getContext());
                break;
            case R.id.btn_count:
                adapter.clearSelections();
                btnShare.setVisibility(View.GONE);
                btnCount.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                break;
            case R.id.btn_delete:
                List<String> files1 = new ArrayList<>();
                if (!files1.isEmpty())
                    files1.clear();
                for (int i = 0; i < contents.size(); i++) {
                    if (adapter.getSelectedItems().get(i)) {
                        InstaContent content = contents.get(i);
                        files1.add(content.url);
                    }
                }
                AppConstants.deleteMulipulfiles(files1, getContext());
                for (int i = 0; i < contents.size(); i++) {
                    for (int k = 0; k < files1.size(); k++) {
                        if (contents.get(i).url.equals(files1.get(k))) {
                            contents.remove(contents.get(i));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                adapter.clearSelections();
                btnShare.setVisibility(View.GONE);
                btnCount.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                break;
        }
    }

    private void getDownloads() {
        if (!videosUrl.isEmpty())
            videosUrl.clear();

        if (!photosUrl.isEmpty())
            photosUrl.clear();

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
                    Log.e("TAG", "getDownloads: Photos ");
                }
            }
        }
    }
}
