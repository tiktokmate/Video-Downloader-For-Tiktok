package com.leetlab.videodownloaderfortiktok.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class UserObject implements Serializable {
    private String userName;
    private String userId;
    private String realName;
    private String image;
    private Bitmap bitmap;
    private Boolean isFaved;

    public UserObject(){}

    public Boolean getFaved() {
        return isFaved;
    }

    public void setFaved(Boolean faved) {
        isFaved = faved;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
