package com.leetlab.videodownloaderfortiktok.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import com.leetlab.videodownloaderfortiktok.BuildConfig;
import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.adapter.PreviewPagerAdapter;
import com.leetlab.videodownloaderfortiktok.model.InstaContent;
import com.leetlab.videodownloaderfortiktok.utils.Admob;
import com.leetlab.videodownloaderfortiktok.utils.AppConstants;
import com.leetlab.admob.AdMobUtils;
import com.google.android.gms.ads.AdSize;
import com.leetlab.utils.Contants.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewActivity extends AppCompatActivity {

    @BindView(R.id.vp_preview)
    ViewPager vpPreview;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.videoview)
    VideoView videoview;
    @BindView(R.id.video_container)
    FrameLayout videoContainer;
    @BindView(R.id.adContainer)
    LinearLayout adContainer;

    private List<InstaContent> contents = new ArrayList<>();
    private int currentPosition = 0;
    private int type;
    private AdMobUtils adMobUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_more));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        contents = getIntent().getParcelableArrayListExtra(AppConstants.PREVIEW_OBJ);
        currentPosition = getIntent().getIntExtra(AppConstants.PREVIEW_POSITION, 0);
        type = getIntent().getIntExtra(AppConstants.CONTENT_TYPE, 1);

        if (type == AppConstants.TYPE_PHOTOS)
            setPhotosPreview();
        else
            setVideoPreview();

        adMobUtils = new AdMobUtils(this, Admob.APP_ID);
        adMobUtils.loadBannerAd(adContainer, Admob.BANNER_ID, AdSize.BANNER);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constants.STATE = false;
    }

    private void setVideoPreview() {
        vpPreview.setVisibility(View.GONE);
        videoContainer.setVisibility(View.VISIBLE);
        videoview.setVideoURI(Uri.parse(contents.get(currentPosition).url));
        videoview.start();
        videoview.setOnPreparedListener(mp -> mp.setLooping(true));
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoview);
        videoview.setMediaController(mediaController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                share(1);
                break;
            case R.id.menu_share_instagram:
                share(2);
                break;
            case R.id.menu_share_whatsapp:
                share(3);
                break;
            case R.id.menu_delete:
                delete();
                break;
        }
        return true;
    }

    private void share(int shareType) {
        String url = null;
        if (type == AppConstants.TYPE_PHOTOS) {
            int index = vpPreview.getCurrentItem();
            url = contents.get(index).url;

        } else {
            contents.get(currentPosition);
            url = contents.get(currentPosition).url;
        }

        switch (shareType) {
            case 1:
                if (url != null)
                    shareImage(url, 0);
                else
                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                if (url != null)
                    shareImage(url, 2);
                else
                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                if (url != null)
                    shareImage(url, 3);
                else
                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void delete() {
        String url = null;
        if (type == AppConstants.TYPE_PHOTOS) {
            int index = vpPreview.getCurrentItem();
            url = contents.get(index).url;

        } else {
            contents.get(currentPosition);
            url = contents.get(currentPosition).url;
        }

        if (url != null)
            DeleteItem(url);
        else
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
    }

    private void setPhotosPreview() {
        PreviewPagerAdapter adapter = new PreviewPagerAdapter(this, contents);
        vpPreview.setAdapter(adapter);
        vpPreview.setCurrentItem(currentPosition);
        vpPreview.setVisibility(View.VISIBLE);
        videoContainer.setVisibility(View.GONE);
    }

    public void shareImage(String uri, int type) {
        Uri mainUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", new File(uri));
        if (mainUri.toString().endsWith(".jpg")) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("image/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, mainUri);
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (type == 3)
                sharingIntent.setPackage("com.whatsapp");
            else if (type == 2)
                sharingIntent.setPackage("com.instagram.android");
            try {
                startActivity(Intent.createChooser(sharingIntent, "Share Image using"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No application found to open this file.", Toast.LENGTH_LONG).show();
            }
        } else if (mainUri.toString().endsWith(".mp4")) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("video/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, mainUri);
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                startActivity(Intent.createChooser(sharingIntent, "Share video using"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No application found to open this file.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void DeleteItem(String uri) {
        final File file = new File(uri);
        try {
            if (file.exists()) {
                boolean del = file.delete();
                Toast.makeText(this, "File Deleted", Toast.LENGTH_SHORT).show();
                if (del) {
                    MediaScannerConnection.scanFile(
                            this,
                            new String[]{uri, uri},
                            new String[]{"image/jpg", "video/mp4"},
                            new MediaScannerConnection.MediaScannerConnectionClient() {
                                public void onMediaScannerConnected() {
                                }

                                public void onScanCompleted(String path, Uri uri) {
                                    Log.d("video path: ", path);
                                }
                            });
                }
                onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (Admob.isPremiumFeature()) {
            super.onDestroy();
        }

        super.onDestroy();

    }

}
