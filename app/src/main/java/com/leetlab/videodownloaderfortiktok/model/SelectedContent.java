package com.leetlab.videodownloaderfortiktok.model;

public class SelectedContent {
    private String mediaUrl;
    private boolean isVideo;

    public SelectedContent(String mediaUrl, boolean isVideo) {
        this.mediaUrl = mediaUrl;
        this.isVideo = isVideo;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public boolean isVideo() {
        return isVideo;
    }
}