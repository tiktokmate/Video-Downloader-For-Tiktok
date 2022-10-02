package com.leetlab.videodownloaderfortiktok.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.leetlab.videodownloaderfortiktok.BuildConfig;
import com.leetlab.videodownloaderfortiktok.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.FileProvider;

public class AppConstants {
    public final static String STORY_URL = "https://i.instagram.com/api/v1/feed/reels_tray/";
    public final static String TYPE_DP_SAVER = "dp_saver";
    public static final String FOLDER_NAME = "Instagram Downloader";
    public static final String CONTENT_TYPE = "type";
    public static String BASE_URL = "https://www.instagram.com/";
    public static String END_URL = "/?__a=1";
    public static final int TYPE_PHOTOS = 1;
    public static final int TYPE_VIDEOS = 2;
    public static final String PREVIEW_OBJ = "preview_obj";
    public static final String PREVIEW_POSITION = "preview_pos";
    public static final String DIRECTORY_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + FOLDER_NAME;


    public static void rateApp(Activity activity) {
        Uri uri = Uri.parse("market://details?id=" + activity.getApplication().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getApplication().getPackageName())));
        }
    }

    public static void shareApp(Activity activity, String text) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, activity.getResources().getString(R.string.app_name));
            String sAux = "\n" + text + "\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + activity.getApplication().getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            activity.startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception ignored) {
        }
    }

    public static void openPrivacyPolicy(Activity activity, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(browserIntent);
    }

    public static void moreApps(Activity activity, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(browserIntent);
    }

    public static void shareMultipleFiles(List<File> files, Context context) {
        ArrayList<Uri> uris = new ArrayList<>();
        for (File file : files) {
//            uris.add(Uri.fromFile(file));
            uris.add(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file));
        }
        final Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("*/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        context.startActivity(Intent.createChooser(intent, context.getString(com.leetlab.utils.R.string.ids_msg_share)));
    }

    public static void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    public static void deleteMulipulfiles(List<String> files1, Context context) {
        Toast.makeText(context, "" + files1.size(), Toast.LENGTH_SHORT).show();
        for (int i = 0; i < files1.size(); i++) {
            DeleteItem(files1.get(i), context);
        }
    }

    public static void DeleteItem(String uri, Context context) {
        final File file = new File(uri);
        try {
            if (file.exists()) {
                boolean del = file.delete();
                Toast.makeText(context, "File Deleted", Toast.LENGTH_SHORT).show();
                if (del) {
                    MediaScannerConnection.scanFile(
                            context,
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
            } else {
                Toast.makeText(context, "Not found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void clearClipBoard(Context context) {
        ClipboardManager clipService = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("", "");
        clipService.setPrimaryClip(clipData);
    }
}
