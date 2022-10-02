package com.leetlab.videodownloaderfortiktok.model;

import android.os.Parcel;
import android.os.Parcelable;

public class InstaUserModel implements Parcelable {
    private long uid;
    private String name;
    private String username;
    private String image_url;
    private String mcd;
    private String mid;
    private String csrftoken;
    private String ds_user_id;
    private String sessionid;
    private String rur;
    private String urlgen;
    private String shbid;
    private String shbts;
    private String cookie;
    private boolean is_login;

    public InstaUserModel() { }

    public InstaUserModel(Parcel in) {
        uid = in.readLong();
        name = in.readString();
        username = in.readString();
        image_url = in.readString();
        mcd = in.readString();
        mid = in.readString();
        csrftoken = in.readString();
        ds_user_id = in.readString();
        sessionid = in.readString();
        rur = in.readString();
        urlgen = in.readString();
        shbid = in.readString();
        shbts = in.readString();
        cookie = in.readString();
        is_login = in.readByte() != 0;
    }

    public static final Creator<InstaUserModel> CREATOR = new Creator<InstaUserModel>() {
        @Override
        public InstaUserModel createFromParcel(Parcel in) {
            return new InstaUserModel(in);
        }

        @Override
        public InstaUserModel[] newArray(int size) {
            return new InstaUserModel[size];
        }
    };

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getMcd() {
        return mcd;
    }

    public void setMcd(String mcd) {
        this.mcd = mcd;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getCsrftoken() {
        return csrftoken;
    }

    public void setCsrftoken(String csrftoken) {
        this.csrftoken = csrftoken;
    }

    public String getDs_user_id() {
        return ds_user_id;
    }

    public void setDs_user_id(String ds_user_id) {
        this.ds_user_id = ds_user_id;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getRur() {
        return rur;
    }

    public void setRur(String rur) {
        this.rur = rur;
    }

    public String getUrlgen() {
        return urlgen;
    }

    public void setUrlgen(String urlgen) {
        this.urlgen = urlgen;
    }

    public String getShbid() {
        return shbid;
    }

    public void setShbid(String shbid) {
        this.shbid = shbid;
    }

    public String getShbts() {
        return shbts;
    }

    public void setShbts(String shbts) {
        this.shbts = shbts;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public boolean isIs_login() {
        return is_login;
    }

    public void setIs_login(boolean is_login) {
        this.is_login = is_login;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(uid);
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(image_url);
        dest.writeString(mcd);
        dest.writeString(mid);
        dest.writeString(csrftoken);
        dest.writeString(ds_user_id);
        dest.writeString(sessionid);
        dest.writeString(rur);
        dest.writeString(urlgen);
        dest.writeString(shbid);
        dest.writeString(shbts);
        dest.writeString(cookie);
        dest.writeByte((byte) (is_login ? 1 : 0));
    }

    @Override
    public String toString() {
        return "InstaUserModel{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", image_url='" + image_url + '\'' +
                ", mcd='" + mcd + '\'' +
                ", mid='" + mid + '\'' +
                ", csrftoken='" + csrftoken + '\'' +
                ", ds_user_id='" + ds_user_id + '\'' +
                ", sessionid='" + sessionid + '\'' +
                ", rur='" + rur + '\'' +
                ", urlgen='" + urlgen + '\'' +
                ", shbid='" + shbid + '\'' +
                ", shbts='" + shbts + '\'' +
                ", cookie='" + cookie + '\'' +
                ", is_login=" + is_login +
                '}';
    }
}
