package com.leetlab.utils.Contants;

import android.os.Environment;

import com.daimajia.androidanimations.library.Techniques;

public class Constants {
    public static Techniques technique = Techniques.BounceInDown;
    public static int duration = 400;

    public static final String STORY_URL = "https://i.instagram.com/api/v1/feed/reels_tray/";
    public static final String YOUTUBE_VIDEO_ID = "0S37fyKKNCM";
    public static final String CONTENT_TYPE = "type";
    public static final int TYPE_PHOTOS = 1;
    public static Boolean STATE = true;
    public static final int TYPE_VIDEOS = 2;
    public static final String PREVIEW_OBJ = "preview_obj";
    public static final String PREVIEW_POSITION = "preview_pos";
    public static final String FOLDER_NAME = "Insta Story Saver";
    private static final String AUTO_DOWNLOAD_STATE = "auto_download_state";
//    private static final String AUTO_DOWNLOAD_LOGS = "autoDownloadLogs";
    public static final String DIRECTORY_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + FOLDER_NAME + "/";
}
