package com.leetlab.videodownloaderfortiktok.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.utils.dialog.AlertDialog;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    private final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        handlePermissions();

    }



    private void handlePermissions() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        Permissions.check(SplashActivity.this, permissions, null, null, new PermissionHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onGranted() {
                updateUI();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                showDialogForPermissionDenied();
            }
        });
    }

    private void updateUI() {
        new Handler().postDelayed(() -> {
            Intent intent;
            intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
            finish();
        }, 1000);
    }

    private void showDialogForPermissionDenied() {
        AlertDialog.init(SplashActivity.this).setRadius(12).setTitle("Allow Permissions")
                .setMessage("To download Instagram photos and videos you " +
                        "need to allow permissions").setCancelable(false)
                .setNegativeClick("Cancel", dialog -> dialog.dismiss()).setPositiveClick("Allow", dialog -> {
            handlePermissions();
            dialog.dismiss();
        }).show();
    }
}
