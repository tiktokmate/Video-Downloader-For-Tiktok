package com.leetlab.videodownloaderfortiktok.utils;


import android.text.format.DateUtils;
import android.util.Log;

import io.paperdb.Paper;

public class PaperUtil {

    private static final String INTRO = "intro";
    private static final String AUTO_DOWNLOAD = "AUTO_DOWNLOAD";
    private static final String DOWNLOADNUMBERS = "DOWNLOADNUMBERS";
    private static final String AUTO_DOWNLOAD_STATE = "auto_download_state";
    private static final String DOWNLOAD_COUNTER = "download_counter";
    private static final String RATING_TIMESTAMP = "rating_timestamp";

    public static void setIntroComplete() {
        Paper.book().write(INTRO, true);
    }

    public static boolean isIntroComplete() {
        return Paper.book().read(INTRO, false);
    }

    public static boolean isPhotoVideosShow() {
        return Paper.book().read("photo_videos", false);
    }

    public static boolean isDPSaverShow() {
        return Paper.book().read("dp_saver", false);
    }

    public static void setPhotoVideosShown() {
        Paper.book().write("photo_videos", true);
    }

    public static void setDPSaverShown() {
        Paper.book().write("dp_saver", true);
    }

    public static void setAutoDownloadEnable() {
        Paper.book().write(AUTO_DOWNLOAD, true);
    }

    public static Boolean isAutoDownload() {
        return Paper.book().read(AUTO_DOWNLOAD, false);
    }

    public static void setAutoDownloadDisable() {
        Paper.book().write(AUTO_DOWNLOAD, false);
    }

    public static void setUnseenMediaSize(int unseenMediaSize) {
        Paper.book().write(INTRO, unseenMediaSize);
    }

    public static void setAutoDownloadState(boolean state) {
        Paper.book().write(AUTO_DOWNLOAD_STATE, state);
    }

    public static boolean isAutoDownloadStateActive() {
        return Paper.book().read(AUTO_DOWNLOAD_STATE, false);
    }

    public static boolean isDialogAvailable() {
//        if (isDialogShownToday()) {
//            long lastTime = Paper.book().read(RATING_TIMESTAMP, 0L);
//            Log.d("ratingDialog", "Last " + lastTime);
//            Log.d("ratingDialog", "Already Show");
//            return false;
//        }
        int currentCounter = Paper.book().read(DOWNLOAD_COUNTER, 0);
        Log.d("ratingDialog", "Counter: " + currentCounter);
        if (currentCounter == 2) {
            Paper.book().write(DOWNLOAD_COUNTER, 0);
            setCurrentRatingDialogTimestamp(System.currentTimeMillis());
            return true;
        } else {
            int counter = currentCounter + 1;
            Log.d("ratingDialog", "Counter: " + counter);
            Paper.book().write(DOWNLOAD_COUNTER, counter);
            return false;
        }
    }

    public static void setCurrentRatingDialogTimestamp(long timestamp) {
        Paper.book().write(RATING_TIMESTAMP, timestamp);
    }

    public static boolean isDialogShownToday() {
        long lastTime = Paper.book().read(RATING_TIMESTAMP, 0L);
        return DateUtils.isToday(lastTime);
    }

    public static int getUnseenMediaSize() {
        return Paper.book().read(INTRO);
    }

    public static boolean IsUnseenMediaAvalabile() {
        Boolean aBoolean = false;
        if (Paper.book().contains(INTRO)) {
            if (Paper.book().read(INTRO).equals(0)) {
                aBoolean = false;
            } else {
                aBoolean = true;
            }
        }
        return aBoolean;
    }


}
