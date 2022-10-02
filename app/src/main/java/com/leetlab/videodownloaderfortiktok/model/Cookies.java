package com.leetlab.videodownloaderfortiktok.model;

import android.util.Log;

import java.io.Serializable;

public class Cookies implements Serializable {
    private String all;
    private String csrftoken;
    private String ds_user_id;
    private String mcd;
    private String mid;
    private String rur;
    private String sessionid;
    private String shbid;
    private String shbts;
    private String urlgen;

    private static class CookiesInstance {
        private static final Cookies INSTANCE = new Cookies();

        private CookiesInstance() {

        }
    }

    public static Cookies getInstance() {
        return CookiesInstance.INSTANCE;
    }

    public static void setCook(String str) {
        String[] split = str.split(";");
        Cookies instance = getInstance();
        instance.setAll(str);
        for (String str2 : split) {
            Log.e("----cook----", str2);
            String str22 = str2.trim();
            if (str22.contains("mcd=")) {
                instance.setMcd(str22.substring(4));
            } else if (str22.contains("mid=")) {
                instance.setMid(str22.substring(4));
            } else if (str22.contains("csrftoken=")) {
                instance.setCsrftoken(str22.substring(10));
            } else if (str22.contains("ds_user_id=")) {
                instance.setDs_user_id(str22.substring(11));
            } else if (str22.contains("sessionid=")) {
                instance.setSessionid(str22.substring(10));
            } else if (str22.contains("rur=")) {
                instance.setRur(str22.substring(4));
            } else if (str22.contains("urlgen=")) {
                instance.setUrlgen(str22.substring(7));
            } else if (str22.contains("shbid=")) {
                instance.setShbid(str22.substring(6));
            } else if (str22.contains("shbts=")) {
                instance.setShbts(str22.substring(6));
            }
        }
    }

    private Cookies() {

    }

    public String getAll() {
        return this.all;
    }

    public String getCsrftoken() {
        return this.csrftoken;
    }

    public String getDs_user_id() {
        return this.ds_user_id;
    }

    public String getMcd() {
        return this.mcd;
    }

    public String getMid() {
        return this.mid;
    }

    public String getRur() {
        return this.rur;
    }

    public String getSessionid() {
        return this.sessionid;
    }

    public String getShbid() {
        return this.shbid;
    }

    public String getShbts() {
        return this.shbts;
    }

    public String getUrlgen() {
        return this.urlgen;
    }

    public void setAll(String str) {
        this.all = str;
    }

    public void setCsrftoken(String str) {
        this.csrftoken = str;
    }

    public void setDs_user_id(String str) {
        this.ds_user_id = str;
    }

    public void setMcd(String str) {
        this.mcd = str;
    }

    public void setMid(String str) {
        this.mid = str;
    }

    public void setRur(String str) {
        this.rur = str;
    }

    public void setSessionid(String str) {
        this.sessionid = str;
    }

    public void setShbid(String str) {
        this.shbid = str;
    }

    public void setShbts(String str) {
        this.shbts = str;
    }

    public void setUrlgen(String str) {
        this.urlgen = str;
    }


}
