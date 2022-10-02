package com.leetlab.videodownloaderfortiktok.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leetlab.videodownloaderfortiktok.model.SelectedContent;
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment;
import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.adapter.ContentAdapter;
import com.leetlab.videodownloaderfortiktok.listener.DownloadDialogListener;
import com.leetlab.videodownloaderfortiktok.model.PostModel;
import com.leetlab.videodownloaderfortiktok.utils.DownloadUtil;
import com.leetlab.utils.dialog.AlertDialog;
import com.bumptech.glide.Glide;
import com.jackandphantom.circularimageview.RoundedImage;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.CLIPBOARD_SERVICE;

public class DownloadDialogFragment extends SuperBottomSheetFragment {

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

    private PostModel post;
    private DownloadDialogListener listener;

    public DownloadDialogFragment(PostModel post, DownloadDialogListener listener) {
        this.post = post;
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_download_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Glide.with(getActivity()).load(post.profileUrl).placeholder(getContext().getDrawable(R.drawable.ic_photo_placeholder)).into(profileImg);
        nameTxt.setText(post.fullName);
        usernameTxt.setText("@" + post.username);
        String stats = prettyCount(Integer.valueOf(post.totalLikes)) + " likes " + prettyCount(Integer.valueOf(post.totalComments)) + " comments";
        statsTxt.setText(stats);
        downloadTxt.setText("Download(" + post.instaContent.size() + ")");

        int countSpam = (post.instaContent.size() == 1) ? 1 : 2;
        contentList.setLayoutManager(new GridLayoutManager(getContext(), countSpam));
        ContentAdapter adapter = new ContentAdapter(getContext(), post.instaContent, this::updateDownloadUI);

        contentList.setAdapter(adapter);

        if (post.instaContent.size() > 2) {
            contentList.getLayoutParams().height = 1100;
            contentList.requestLayout();
        }

        if (post.caption == null) {
            captionTxt.setVisibility(View.GONE);
        } else {
            captionTxt.setVisibility(View.VISIBLE);
            captionTxt.setText(post.caption);
        }

    }


    @SuppressLint("ResourceAsColor")
    private void updateDownloadUI(List<SelectedContent> selectedContent) {
        List<SelectedContent> selectedContent1 = selectedContent;
        downloadTxt.setText("Download(" + selectedContent.size() + ")");
        int res = selectedContent.size() == 0 ? R.color.colorGray : R.drawable.bg_download_dialog;
        downloadTxt.setBackgroundResource(res);
        downloadTxt.setEnabled(selectedContent.size() != 0);
    }

    @Override
    public float getCornerRadius() {
        return 28;
    }

    @Override
    public int getPeekHeight() {
        int peekHeight = 478;
        if (post.instaContent.size() <= 2) {
            peekHeight += 592;
        } else {
            peekHeight += 1142;
        }
        if (post.caption != null) {
            peekHeight += 126;
        }
        return peekHeight;
    }

    @OnClick({R.id.closeBtn, R.id.download_txt, R.id.caption_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.closeBtn:
                listener.onDownloadFinish();
                this.dismiss();
                break;
            case R.id.download_txt:
                Permissions.check(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE, null, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        DownloadUtil.download(getContext(), post.instaContent, post.username);
                        listener.onDownloadFinish();
                        dismiss();
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
