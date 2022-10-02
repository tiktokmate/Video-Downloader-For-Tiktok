package com.leetlab.videodownloaderfortiktok.model;

import android.os.Parcel;
import android.os.Parcelable;

public class InstaContent implements Parcelable {

    public String url;
    public String videoThumbnailUrl;
    public boolean isVideo;
    public boolean isSelected = true;

    public InstaContent(String url, boolean isVideo, String videoThumbnailurl) {
        this.url = url;
        this.isVideo = isVideo;
        this.videoThumbnailUrl = videoThumbnailurl;
    }

    protected InstaContent(Parcel in) {
        url = in.readString();
        videoThumbnailUrl = in.readString();
        isVideo = in.readByte() != 0;
        isSelected = in.readByte() != 0;
    }

    public static final Creator<InstaContent> CREATOR = new Creator<InstaContent>() {
        @Override
        public InstaContent createFromParcel(Parcel in) {
            return new InstaContent(in);
        }

        @Override
        public InstaContent[] newArray(int size) {
            return new InstaContent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeString(videoThumbnailUrl);
        parcel.writeByte((byte) (isVideo ? 1 : 0));
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }
}
