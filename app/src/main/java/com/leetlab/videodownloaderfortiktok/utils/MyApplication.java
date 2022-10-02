package com.leetlab.videodownloaderfortiktok.utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.downloader.PRDownloader;
import com.google.android.gms.ads.MobileAds;
import com.leetlab.videodownloaderfortiktok.R;
import com.onesignal.OneSignal;

import io.paperdb.Paper;

public class MyApplication extends Application implements DefaultLifecycleObserver {

    public static final String AUTO_DOWNLOAD_LOGS = "autoDownloadLogs";

    @Override
    public void onCreate() {
        super.onCreate();

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        PRDownloader.initialize(getApplicationContext());
        Paper.init(this);
        //One Signal
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
//        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        setUpNotificationchannel();
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip()) {
            Log.d(AUTO_DOWNLOAD_LOGS, "have primary clip");
            String url = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
            if (url.contains("https://www.instagram.com/")) {
                Log.d(AUTO_DOWNLOAD_LOGS, "Contains instagram");
                if (PaperUtil.isAutoDownload()) {
                    PaperUtil.setAutoDownloadState(true);
                    Log.d(AUTO_DOWNLOAD_LOGS, "Auto Download State True");
                }
            }
        } else {
            Log.d(AUTO_DOWNLOAD_LOGS, "Don't have primary clip");
        }
    }


    public void setUpNotificationchannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_HIGH));
        }

    }
}
