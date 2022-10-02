package com.leetlab.videodownloaderfortiktok.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.listener.PrivateAlertDialogListner;

public class PrivateAccountAlertFragment extends DialogFragment {

    public PrivateAccountAlertFragment() {
    }

    PrivateAlertDialogListner listener;

    public PrivateAccountAlertFragment(PrivateAlertDialogListner listener) {
        this.listener = listener;
    }

    @BindView(R.id.btnFBLogin)
    ConstraintLayout btnFBLogin;
    @BindView(R.id.btn_insta_login)
    ConstraintLayout btn_insta_login;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_private_account_alert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.btnFBLogin, R.id.btn_insta_login, R.id.closeBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.closeBtn:
                listener.OnDismiss();
                getDialog().dismiss();
                break;
            case R.id.btnFBLogin:
                listener.OnSignIn();
                getDialog().dismiss();
                break;
            case R.id.btn_insta_login:
                listener.OnSignIn();
                getDialog().dismiss();
                break;
        }
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

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
    }
}
