package com.leetlab.videodownloaderfortiktok.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jackandphantom.circularimageview.RoundedImage;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.leetlab.admob.AdMobUtils;
import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.adapter.ContentAdapter;
import com.leetlab.videodownloaderfortiktok.listener.DownloadDialogListener;
import com.leetlab.videodownloaderfortiktok.model.PostModel;
import com.leetlab.videodownloaderfortiktok.model.SelectedContent;
import com.leetlab.videodownloaderfortiktok.utils.Admob;
import com.leetlab.videodownloaderfortiktok.utils.DownloadUtil;
import com.leetlab.utils.dialog.AlertDialog;
import com.leetlab.utils.dialog.ProgressDialog;
import com.leetlab.utils.dialog.ProgressType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.CLIPBOARD_SERVICE;

public class DownloadDialog extends DialogFragment {

    private static final String TAG = "DownloadDialog";
    @BindView(R.id.name_txt)
    TextView nameTxt;
    @BindView(R.id.stats_txt)
    TextView statsTxt;
    @BindView(R.id.username_txt)
    TextView usernameTxt;
    @BindView(R.id.profile_img)
    RoundedImage profileImg;
    @BindView(R.id.rv_content)
    RecyclerView contentList;
    @BindView(R.id.caption_txt)
    TextView captionTxt;
    @BindView(R.id.download_txt)
    TextView downloadTxt;
    @BindView(R.id.root)
    ConstraintLayout root;

    private static String POST_OBJ = "post";
    @BindView(R.id.adView2)
    AdView adView2;
    private List<SelectedContent> selectedContent = new ArrayList<>();

    private PostModel post;
    private DownloadDialogListener listener;
    private ProgressDialog dialog;

    AdMobUtils adMobUtils;

    public static DownloadDialog newInstance(PostModel post) {
        DownloadDialog dialog = new DownloadDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(POST_OBJ, post);
        dialog.setArguments(bundle);
        return dialog;
    }

    private DownloadDialog() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DownloadDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement" + DownloadDialogListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.download_dialog_updated, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            this.dismiss();
            return;
        }

        post = bundle.getParcelable(POST_OBJ);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


        adMobUtils = new AdMobUtils(getContext(), Admob.APP_ID);
        if (!Admob.isPremiumFeature()) {
            adMobUtils.loadBannerAd(adView2, Admob.BANNER_ID, AdSize.BANNER);
        }


        Glide.with(getActivity()).load(post.profileUrl).placeholder(getContext().getDrawable(R.drawable.ic_photo_placeholder)).into(profileImg);
        nameTxt.setText(post.fullName);
        usernameTxt.setText("@" + post.username);
        String stats = prettyCount(Integer.valueOf(post.totalLikes)) + " likes " + prettyCount(Integer.valueOf(post.totalComments)) + " comments";
        statsTxt.setText(stats);
        statsTxt.setVisibility(View.GONE);
        downloadTxt.setText("Download(" + post.instaContent.size() + ")");

        int countSpam = (post.instaContent.size() == 1) ? 1 : 2;
        contentList.setLayoutManager(new GridLayoutManager(getContext(), countSpam));
        ContentAdapter adapter = new ContentAdapter(getContext(), post.instaContent, selectedContent1 -> {
            updateDownloadUI(selectedContent1);
            selectedContent = selectedContent1;
            logUrls();
        });

        contentList.setAdapter(adapter);

        if (post.caption == null) {
            captionTxt.setVisibility(View.GONE);
        } else {
            captionTxt.setVisibility(View.VISIBLE);
            captionTxt.setText(post.caption);
        }

        dialog = new ProgressDialog(getActivity(), ProgressType.HORIZONTAL);
        dialog.setMessage("Start downloading");

    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    private void updateDownloadUI(List<SelectedContent> selectedContent) {
        downloadTxt.setText("Download(" + selectedContent.size() + ")");
        int res = selectedContent.size() == 0 ? R.color.colorGray : R.drawable.bg_download_dialog;
        downloadTxt.setBackgroundResource(res);
        downloadTxt.setEnabled(selectedContent.size() != 0);
    }

    @OnClick({R.id.closeBtn, R.id.download_txt, R.id.caption_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.closeBtn:
                Log.e(TAG, "onViewClicked: Close button");
                Objects.requireNonNull(getDialog()).dismiss();
                listener.onDownloadFinish();
                break;
            case R.id.download_txt:
                Permissions.check(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE, null, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        if (!selectedContent.isEmpty()) {
                            DownloadUtil.downloadContent(getContext(), selectedContent, post);
                        } else {
                            for (int i = 0; i < post.instaContent.size(); i++) {
                                selectedContent.add(new SelectedContent(post.instaContent.get(i).url, post.instaContent.get(i).isVideo));
                            }
                            DownloadUtil.downloadContent(getContext(), selectedContent, post);
                        }
                        dismiss();
                        listener.onDownloadFinish();
                    }
                });
                break;
            case R.id.caption_txt:
                AlertDialog.init(getActivity())
                        .animate()
                        .setTitle("Caption")
                        .setMessage(post.caption)
                        .setPositiveClick("Copy", dialog -> {
                            ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("label", post.caption);
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(getContext(), "Caption Copied", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        })
                        .setNegativeClick("Close", Dialog::dismiss)
                        .show();
                break;
        }
    }

    private void logUrls() {
        for (SelectedContent content : selectedContent) {
            Log.d(TAG, "logUrls: " + content.getMediaUrl());
        }
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        listener.onDownloadFinish();
        super.onDismiss(dialog);
    }

    public String prettyCount(Number number) {
        char[] suffix = {' ', 'K', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }


}
