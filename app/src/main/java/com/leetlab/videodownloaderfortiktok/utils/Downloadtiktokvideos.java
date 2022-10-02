
package com.leetlab.videodownloaderfortiktok.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Downloadtiktokvideos {

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("music")
    @Expose
    private String music;
    @SerializedName("music_url")
    @Expose
    private String music_url;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("music_thumb")
    @Expose
    private String music_thumb;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("nickname")
    @Expose
    private String nickname;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMusic_thumb() {
        return music_thumb;
    }

    public void setMusic_thumb(String music_thumb) {
        this.music_thumb = music_thumb;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getMusic_url() {
        return music_url;
    }

    public void setMusic_url(String music_url) {
        this.music_url = music_url;
    }
}
