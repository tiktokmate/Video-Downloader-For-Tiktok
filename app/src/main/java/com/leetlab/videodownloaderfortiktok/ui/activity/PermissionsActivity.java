package com.leetlab.videodownloaderfortiktok.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.leetlab.videodownloaderfortiktok.services.ClipboardService;
import com.leetlab.videodownloaderfortiktok.R;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.ornach.nobobutton.NoboButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PermissionsActivity extends AppCompatActivity {

    private static final int DRAW_OVER_OTHER_APP_PERMISSION = 99;

    @BindView(R.id.btn_back)
    NoboButton btnBack;
    @BindView(R.id.btn_permissions)
    TextView btnPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        ButterKnife.bind(this);
        PermissionBatteryOptimization();
        askForSystemOverlayPermission();
        PermissionOverlay();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();
        if (checkDrawOverlayPermission()){
            startActivity(new Intent(PermissionsActivity.this, MainActivity.class));
        }
    }

    @OnClick({R.id.btn_back, R.id.btn_permissions})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                startActivity(new Intent(PermissionsActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.btn_permissions:
                AskForPermissions();
                break;
        }
    }

    private void AskForPermissions() {
        Permissions.check(PermissionsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, null, new PermissionHandler() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                AskForPermissions();
            }
        });
    }

    private void PermissionBatteryOptimization() {
        int permissionCheck = ContextCompat
                .checkSelfPermission(this, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startActivity(new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse("package:" + getPackageName())));
    }

    private void PermissionOverlay() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getApplicationContext().startForegroundService(new Intent(this, ClipboardService.class));

            } else {
                startService(new Intent(this, ClipboardService.class));
            }

        }
    }

    private void askForSystemOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Boolean checkDrawOverlayPermission() {
        Boolean aBoolean = false;
        if (Settings.canDrawOverlays(getApplicationContext())) {
            aBoolean = true;
        }
        return aBoolean;
    }

}
