package com.leetlab.videodownloaderfortiktok.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class PostModel implements Parcelable {

    public String profileUrl;
    public String fullName;
    public String username;
    public String caption;
    public String totalLikes;
    public String totalComments;
    public List<InstaContent> instaContent = new ArrayList<>();

    public PostModel() {
    }

    public PostModel(Parcel in) {
        profileUrl = in.readString();
        fullName = in.readString();
        username = in.readString();
        caption = in.readString();
        totalLikes = in.readString();
        totalComments = in.readString();
        instaContent = in.createTypedArrayList(InstaContent.CREATOR);
    }


    public static final Creator<PostModel> CREATOR = new Creator<PostModel>() {
        @Override
        public PostModel createFromParcel(Parcel in) {
            return new PostModel(in);
        }

        @Override
        public PostModel[] newArray(int size) {
            return new PostModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(profileUrl);
        parcel.writeString(fullName);
        parcel.writeString(username);
        parcel.writeString(caption);
        parcel.writeString(totalLikes);
        parcel.writeString(totalComments);
        parcel.writeTypedList(instaContent);
    }
}
