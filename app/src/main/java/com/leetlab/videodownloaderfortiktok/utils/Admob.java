package com.leetlab.videodownloaderfortiktok.utils;

import io.paperdb.Paper;

public class Admob {

    //    ..Production..
//    public static String APP_ID = "ca-app-pub-2962311823240464~4202114753";
//    public static String BANNER_ID = "ca-app-pub-2962311823240464/7726426792";
//    public static String INTESTITIAL_ID = "ca-app-pub-2962311823240464/7698506636";

    //    TestAds
    public static String APP_ID = "ca-app-pub-3940256099942544~334751113";
    public static String BANNER_ID = "ca-app-pub-3940256099942544/6300978111";
    public static String INTESTITIAL_ID = "ca-app-pub-3940256099942544/1033173712";
    public static String NATIVE_AD_ID = "ca-app-pub-3940256099942544/2247696110";



    private static final String ADS = "Ads";
    public static void buyPremiumFeature() {
        Paper.book().write(ADS, true);
    }
    public static boolean isPremiumFeature() {
        return Paper.book().read(ADS, false);
    }
}