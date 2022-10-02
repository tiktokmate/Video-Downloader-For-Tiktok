package com.leetlab.videodownloaderfortiktok.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.adapter.IntroAdapter;
import com.leetlab.videodownloaderfortiktok.model.IntroModel;
import com.leetlab.videodownloaderfortiktok.utils.PaperUtil;
import com.leetlab.utils.dialog.AlertDialog;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.ornach.nobobutton.NoboButton;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = "Index";
    private static final int DRAW_OVER_OTHER_APP_PERMISSION = 99;

    @BindView(R.id.get_started_btn)
    NoboButton getStartedBtn;
    @BindView(R.id.intro_pager)
    ViewPager introPager;
    @BindView(R.id.dots_indicator)
    DotsIndicator dotsIndicator;

    private List<IntroModel> intros = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        addIntros();

        IntroAdapter adapter = new IntroAdapter(this, intros);
        introPager.setAdapter(adapter);
        introPager.addOnPageChangeListener(this);
        dotsIndicator.setViewPager(introPager);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.get_started_btn, R.id.skip_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get_started_btn:
                if (introPager.getCurrentItem() == 2) {
                    updateUI();
                } else {
                    int currentPager = introPager.getCurrentItem();
                    currentPager++;
                    introPager.setCurrentItem(currentPager);
                }
                break;
            case R.id.skip_btn:
                updateUI();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        String btnTxt = (position == 2) ? "Let's start" : "Next";
        getStartedBtn.setText(btnTxt);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void addIntros() {
        intros.add(new IntroModel(R.drawable.img1, "Download Photos & Videos (In-App)",
                "Copy Instagram Url and paste that Url in app and download photos and videos", 1));
        intros.add(new IntroModel(R.drawable.img2, "Download Photos & Videos (In-Instagram)",
                "Copy Instagram Url and press the download Button on Bottom of Instagram and download Photos & Videos", 2));
        intros.add(new IntroModel(R.drawable.img3, "Download Display Picture",
                "Paste Username of user and click download to download His/Her DP.", 3));
    }


    private void askForSystemOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION) {
            if (checkDrawOverlayPermission()) {
                PaperUtil.setIntroComplete();
                startActivity(new Intent(IntroActivity.this, HomeActivity.class));
                finish();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Boolean checkDrawOverlayPermission() {
        Boolean aBoolean = false;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(getApplicationContext())) {
                aBoolean = true;
            }
        } else {
            aBoolean = true;
        }
        return aBoolean;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateUI() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
        };

        Permissions.check(IntroActivity.this, permissions, null, null, new PermissionHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onGranted() {
//                if (checkDrawOverlayPermission()) {
                PaperUtil.setIntroComplete();
                startActivity(new Intent(IntroActivity.this, MainActivity.class));
                finish();
//                } else
//                    askForSystemOverlayPermission();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                showDialogForPermissionDenied();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showDialogForPermissionDenied() {
        AlertDialog.init(IntroActivity.this).setRadius(12).setTitle("Allow Permissions")
                .setMessage("To download instagram images and videos you" +
                        "need to allow permissions").setCancelable(false)
                .setNegativeClick("Cancel", dialog -> dialog.dismiss()).setNegativeClick("Got it", dialog -> {
            updateUI();
            dialog.dismiss();
        }).show();
    }
}
