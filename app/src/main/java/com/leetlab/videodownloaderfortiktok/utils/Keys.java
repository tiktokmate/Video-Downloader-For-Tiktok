package com.leetlab.videodownloaderfortiktok.utils;

public final class Keys {

    static {
        System.loadLibrary("keys");
    }

    public native static String ivKey();

    public native static String secretKey();

    public native static String secretUrl();


  /*  public static String ivKey() {

        return "p2u7ilxhWGYzpm2o";
    }

    public static String secretKey() {

        return "LLPPDo2bc4lPrWht";
    }*/


}