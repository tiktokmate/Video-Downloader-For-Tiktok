package com.leetlab.videodownloaderfortiktok.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.ui.activity.TransparentActivity;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class ClipboardService extends Service {

    private static final Object REQUEST_CODE = 99;
    private View mOverlayView, mOverlayView1;
    private ClipboardManager mClipboardManager;
    private WindowManager mWindowManager, mWindowManager1;
    private ImageView downloadImg;
    int mVisibility = 0;
    private static String TAG = "ClipboardService";

    @Override
    public void onCreate() {
        super.onCreate();
        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mClipboardManager.addPrimaryClipChangedListener(mOnPrimaryClipChangedListener);

        System.out.println("Service started running..");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.InstaDownloader.InstaVideoDownloader";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Quick Download instagram videos and photos using our app..")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mClipboardManager != null) {
            mClipboardManager.removePrimaryClipChangedListener(
                    mOnPrimaryClipChangedListener);

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private ClipboardManager.OnPrimaryClipChangedListener mOnPrimaryClipChangedListener =
            new ClipboardManager.OnPrimaryClipChangedListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onPrimaryClipChanged() {
                    Log.e(TAG, "onPrimaryClipChanged: onPrimaryClipChanged");
                    if (mClipboardManager.getPrimaryClip() != null) {
                        final String charSequence = mClipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
                        System.out.println("" + charSequence);
                        if (charSequence.contains("tiktok")) {
                            ShowDownloadIcon(charSequence);
                            Log.e(TAG, "onPrimaryClipChanged: ShowDownloadIcon ");
                        }
                    }
                }
            };

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void ShowDownloadIcon(final String link) {
        Log.e(TAG, "ShowDownloadIcon: ShowDownloadIcon");
        mOverlayView = LayoutInflater.from(ClipboardService.this).inflate(R.layout.lyt_download_icon, null);
        WindowManager.LayoutParams params;
        if (checkDrawOverlayPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);

            } else {
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
            }

            //Specify the view position
            params.gravity = Gravity.BOTTOM | Gravity.LEFT;
            params.x = 70;
            params.y = 110;

            YoYo.with(Techniques.Shake)
                    .duration(1500)
                    .repeat(1)
                    .playOn(mOverlayView);


            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            mWindowManager.addView(mOverlayView, params);
            downloadImg = (ImageView) mOverlayView.findViewById(R.id.downloadButton);
            downloadImg.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), TransparentActivity.class);
                intent.putExtra("copiedLink", link);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                mOverlayView.setVisibility(View.INVISIBLE);
                mVisibility = 1;

            });
            Display display = mWindowManager.getDefaultDisplay();
            final Point size = new Point();
            display.getSize(size);
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mVisibility == 0) {
                    mWindowManager.removeView(mOverlayView);
                } else {
                    mVisibility = 0;
                }
            });
            thread.start();
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


}