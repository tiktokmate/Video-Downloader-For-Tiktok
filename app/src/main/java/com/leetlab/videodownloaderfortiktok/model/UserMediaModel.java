package com.leetlab.videodownloaderfortiktok.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserMediaModel implements Parcelable {

    String mediaUrl;
    String displayUrl;
    boolean isVideo;

    protected UserMediaModel(Parcel in) {
        mediaUrl = in.readString();
        displayUrl = in.readString();
        isVideo = in.readByte() != 0;
    }

    public static final Creator<UserMediaModel> CREATOR = new Creator<UserMediaModel>() {
        @Override
        public UserMediaModel createFromParcel(Parcel in) {
            return new UserMediaModel(in);
        }

        @Override
        public UserMediaModel[] newArray(int size) {
            return new UserMediaModel[size];
        }
    };
    public UserMediaModel(){}

    public String getDisplayUrl() {
        return displayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mediaUrl);
        parcel.writeString(displayUrl);
        parcel.writeByte((byte) (isVideo ? 1 : 0));
    }

}

