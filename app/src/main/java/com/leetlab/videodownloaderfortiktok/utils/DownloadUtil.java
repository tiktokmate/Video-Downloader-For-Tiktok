package com.leetlab.videodownloaderfortiktok.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.leetlab.videodownloaderfortiktok.model.InstaContent;
import com.leetlab.videodownloaderfortiktok.model.PostModel;
import com.leetlab.videodownloaderfortiktok.model.SelectedContent;
import com.leetlab.videodownloaderfortiktok.model.UserMediaModel;
import com.esafirm.rxdownloader.RxDownloader;

import java.io.File;
import java.util.List;

import rx.Observer;

public class DownloadUtil {

    public static final String PATH_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/InstagramDownloads/";
    public static int size = 0;
    public static String TAG = "TAG";

    public static void download(Context context, List<InstaContent> contents, String username) {
        String path = AppConstants.FOLDER_NAME + "/" + username + "_" + System.currentTimeMillis() + ".jpg";
        String type = "image/*";
        Toast.makeText(context, "Start Downloading " + contents.size() + " items ...", Toast.LENGTH_SHORT).show();
        for (InstaContent content : contents) {
            if (content.url.contains(".mp4")) {
                path = AppConstants.FOLDER_NAME + "/" + username + "_" + System.currentTimeMillis() + ".mp4";
                type = "video/*";
            }

            RxDownloader.getInstance(context).download(content.url, path, type).subscribe(new Observer<String>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(String s) {

                }

            });
        }
    }

    public static void downloadContent(Context context, List<SelectedContent> contents, PostModel post) {
        size = 0;
        String username = post.username.replace(".", "_");
        String path = AppConstants.FOLDER_NAME + "/" + username + "_" + System.currentTimeMillis() + ".jpg";
        String type = "image/*";
        Toast.makeText(context, "Start Downloading " + contents.size() + " items ...", Toast.LENGTH_SHORT).show();
        for (SelectedContent content : contents) {
            size = size + 1;
            if (content.isVideo()) {

                String name = System.currentTimeMillis() +"";
                if (isAvailable(name)) {
                    Log.e(TAG, "downloadContent: before" );
                    continue;
                }
                Log.e(TAG, "downloadContent: After");
                path = AppConstants.FOLDER_NAME + "/" + name + ".mp4";
                type = "video/*";
            } else {
                String name = System.currentTimeMillis() +"";
                if (isAvailable(name)) {
                    Log.e(TAG, "downloadContent: before" );
                    Toast.makeText(context, "Image Already downloaded!", Toast.LENGTH_SHORT).show();
                    continue;
                }
                Log.e(TAG, "downloadContent: After");
                path = AppConstants.FOLDER_NAME + "/" + name + ".mp3";
                type = "audio/*";
            }
            RxDownloader.getInstance(context).download(content.getMediaUrl(), path, type).subscribe(new Observer<String>() {
                @Override
                public void onCompleted() {
                    Toast.makeText(context, "Downloading Complete!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(String s) {

                }
            });
        }
        if (PaperUtil.IsUnseenMediaAvalabile())
            PaperUtil.setUnseenMediaSize(PaperUtil.getUnseenMediaSize() + size);
        else
            PaperUtil.setUnseenMediaSize(size);
    }

    public static void downloadDp(Context context, String url, String ImageName) {
        String path = AppConstants.FOLDER_NAME + "/" + ImageName + "_" + System.currentTimeMillis() + ".jpg";
        String type = "image/*";

        RxDownloader.getInstance(context).download(url, path, type).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Toast.makeText(context, "Downloading Complete!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String s) {

            }

        });
    }

    public static void downloadStories(Context context, List<UserMediaModel> contents, String username) {
        username = username.replace(".", "_");
        String path = AppConstants.FOLDER_NAME + "/" + username + "_" + System.currentTimeMillis() + ".jpg";
        String type = "image/*";
        for (UserMediaModel content : contents) {
            if (content.isVideo()) {
                path = AppConstants.FOLDER_NAME + "/" + username + "_" + System.currentTimeMillis() + ".mp4";
                type = "video/*";
            }

            RxDownloader.getInstance(context).download(content.getMediaUrl(), path, type).subscribe(new Observer<String>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(String s) {

                }

            });
        }
    }

    public static Boolean isAvailable(String name) {
        Boolean aBoolean = false;
        String path = AppConstants.DIRECTORY_PATH;
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                String filePath = file.getAbsolutePath();
                if (filePath.contains(name)) {
                    aBoolean = true;
                }
//                if (filePath.endsWith(".mp4")) {
//
//                } else {
//
//                }
            }
        }
        return aBoolean;
    }
}
