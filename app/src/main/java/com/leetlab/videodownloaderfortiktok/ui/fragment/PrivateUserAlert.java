package com.leetlab.videodownloaderfortiktok.ui.fragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.listener.DownloadDialogListener;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.RetryPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrivateUserAlert extends DialogFragment {


    @BindView(R.id.btn_cancel)
    TextView btnCancel;

    private String video = "", image = "", fullnamex = "", likess = "", name = "", desc = "", saveasImage = "", jpgpng = "", saveasvideo = "", videoArray = "", thirteenSexstr = "", fifteenFourstr = "", seventyTwostr = "", tenEightystr = "", twentyFourstr = "";
    SharedPreferences preferences;
    private String mMediaPath = "", mBaseFolderPath;
    private RetryPolicy retryPolicy;
    private Uri downloadUri, destinationUri;
    private DownloadRequest downloadRequest;
    boolean repostClick = false;
    private static final int REQUEST_WRITE_STORAGE = 112;

    private DownloadDialogListener listener;

    public PrivateUserAlert(DownloadDialogListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_privateuser_dialog, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @OnClick(R.id.btn_cancel)
    public void onViewClicked() {
        listener.onDownloadFinish();
        getDialog().dismiss();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getActivity(), "No permission to write, give permission for app to access storage in the app settings!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }


}
