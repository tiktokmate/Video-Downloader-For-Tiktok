package com.leetlab.videodownloaderfortiktok.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsFragment {

    public interface PassMessageToParent {
        public void onMessageReceived(String message);
    }

    PassMessageToParent passMessageToParent;

    int REQUEST_DIRECTORY = 198;
    private String mBaseFolderPath;

    public static class convertCharacters {
        public static String xmlchars(String xml) {
            return xml.replaceAll("&#123;", "{").replaceAll("&#125;", "}").replaceAll("&amp;", "&").replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&quot;", "\"").replaceAll("&apos;", "'");
        }

    }

    public static class getIntent {

        public static String dataIntentText(Activity activity) {

            //get the received intent
            Bundle extras = activity.getIntent().getExtras();

            return extras.get("android.intent.extra.TEXT").toString();

        }

        public static String dataIntentURL(Activity activity) {
            Bundle extras = activity.getIntent().getExtras();
            return reg.getBack(extras.get("android.intent.extra.TEXT").toString(), "(http:s?((?!\\s).)*");
        }
    }

    public static class getlistpackage {
        public static boolean doesPackageExist(String targetPackage, Context context) {
            PackageManager pm = context.getPackageManager();
            try {
                PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }
            return true;
        }
    }

    public static class httpRequest {
        public static String get(String url) {
            try {
                URL curl = new URL(url);
                HttpURLConnection connect = (HttpURLConnection) curl.openConnection();
                connect.setRequestMethod("GET");
                connect.setRequestProperty("USER-AGENT", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/601.3.9 (KHTML, like Gecko) Version/9.0.2 Safari/601.3.9");
                connect.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;5.0");
                connect.setUseCaches(false);
                connect.setAllowUserInteraction(false);
                BufferedReader r = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                return total.toString();
            } catch (Exception e) {
                return e.getMessage();
            }
        }
    }

    public static class json {
        public static String jsonObject(String json, String key) {
            try {
                JSONObject j = new JSONObject(convertCharacters.xmlchars(json));
                return j.getString(key);
            } catch (Exception e) {
                return "";
            }
        }
    }

    public static class movefile {

        public static void mf(File file, File dir) throws IOException {
            File newFile = new File(dir, file.getName());
            FileChannel outputChannel = null;
            FileChannel inputChannel = null;
            try {
                outputChannel = new FileOutputStream(newFile).getChannel();
                inputChannel = new FileInputStream(file).getChannel();
                inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                inputChannel.close();
                file.delete();
            } finally {
                if (inputChannel != null) inputChannel.close();
                if (outputChannel != null) outputChannel.close();
            }
        }
    }

    public static class notifications {
        public static void notify(String title, String text, int icon, Activity activity, Class mainActivity) {
            Intent resultIntent = new Intent(activity, mainActivity);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            activity,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(activity);
            mBuilder.setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setContentIntent(resultPendingIntent);
            NotificationManager mNotifyMgr =
                    (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);
            mNotifyMgr.notify(8, mBuilder.build());
        }
    }

    public static class player {
        public static void mPlayer(String filepath, Activity activity) {
            File file = new File(filepath);
            if (!reg.getBack(filepath, "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "video/*");

                    //open movie player in Nexus plus All Google based ROMs
                    if (getlistpackage.doesPackageExist("com.google.android.gallery3d", activity)) {
                        intent.setClassName("com.google.android.gallery3d", "com.android.gallery3d.app.MovieActivity");
                        //open movie player in Sony Xperia android devices
                    } else if (getlistpackage.doesPackageExist("com.android.gallery3d", activity)) {
                        intent.setClassName("com.android.gallery3d", "com.android.gallery3d.app.MovieActivity");
                        //open movie player in Samsung TouchWiz based ROMs
                    } else if (getlistpackage.doesPackageExist("com.cooliris.media", activity)) {
                        intent.setClassName("com.cooliris.media", "com.cooliris.media.MovieView");
                    }

                    activity.startActivity(intent);

                } catch (Exception e) {
                    Log.d("filePlayError: ", e.getLocalizedMessage());

                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "video/*");
                        activity.startActivity(intent);
                    } catch (Exception j) {
                        Log.d("filePlayError: ", j.getLocalizedMessage());
                        Toast.makeText(activity, "Sorry, Play video not working properly , try again!", Toast.LENGTH_LONG).show();
                    }

                }

                Log.d("mPlayer_if_case: ", "1 case");

            } else if (!reg.getBack(filepath, "((\\.3ga|\\.aac|\\.aif|\\.aifc|\\.aiff|\\.amr|\\.au|\\.aup|\\.caf|\\.flac|\\.gsm|\\.kar|\\.m4a|\\.m4p|\\.m4r|\\.mid|\\.midi|\\.mmf|\\.mp2|\\.mp3|\\.mpga|\\.ogg|\\.oma|\\.opus|\\.qcp|\\.ra|\\.ram|\\.wav|\\.wma|\\.xspf)$)").isEmpty()) {

                Log.d("mPlayer_if_case: ", "2 case");

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "audio/*");
                activity.startActivity(intent);

            } else if (!reg.getBack(filepath, "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty()) {

                Log.d("mPlayer_if_case: ", "3 case");

                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "image/*");
                    activity.startActivity(intent);
                } catch (Exception e) {
                    Log.d("filePlayError: ", e.getLocalizedMessage());
                    Toast.makeText(activity, "Sorry. We can't Display Images. try again", Toast.LENGTH_LONG).show();
                }

            }


        }

        public static void mPlayerStream(String filepath, Activity activity) {

            if (!reg.getBack(filepath,
                    "((\\.mp4(\\?[^?]+)?$)" +
                            "|(\\.webm(\\?[^?]+)?$)" +
                            "|(\\.ogg(\\?[^?]+)?$)" +
                            "|(\\.mpK(\\?[^?]+)?$)" +
                            "|(\\.avi(\\?[^?]+)?$)" +
                            "|(\\.mkv(\\?[^?]+)?$)" +
                            "|(\\.flv(\\?[^?]+)?$)" +
                            "|(\\.mpg(\\?[^?]+)?$)" +
                            "|(\\.wmv(\\?[^?]+)?$)" +
                            "|(\\.vob(\\?[^?]+)?$)" +
                            "|(\\.ogv(\\?[^?]+)?$)" +
                            "|(\\.mov(\\?[^?]+)?$)" +
                            "|(\\.qt(\\?[^?]+)?$)" +
                            "|(\\.rm(\\?[^?]+)?$)" +
                            "|(\\.rmvb(\\?[^?]+)?$)" +
                            "|(\\.asf(\\?[^?]+)?$)" +
                            "|(\\.m4p(\\?[^?]+)?$)" +
                            "|(\\.m4v(\\?[^?]+)?$)" +
                            "|(\\.mp2(\\?[^?]+)?$)" +
                            "|(\\.mpeg(\\?[^?]+)?$)" +
                            "|(\\.mpe(\\?[^?]+)?$)" +
                            "|(\\.mpv(\\?[^?]+)?$)" +
                            "|(\\.m2v(\\?[^?]+)?$)" +
                            "|(\\.3gp(\\?[^?]+)?$)" +
                            "|(\\.f4p(\\?[^?]+)?$)" +
                            "|(\\.f4a(\\?[^?]+)?$)" +
                            "|(\\.f4b(\\?[^?]+)?$)" +
                            "|(\\.f4v(\\?[^?]+)?$))").isEmpty()) {

                try {

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);

                    intent.setDataAndType(Uri.parse(filepath), "video/*");
                    Log.e("videomustwork", filepath);
                    //open movie player in Nexus plus All Google based ROMs
                    if (getlistpackage.doesPackageExist("com.google.android.gallery3d", activity)) {
                        intent.setClassName("com.google.android.gallery3d", "com.android.gallery3d.app.MovieActivity");
                        //open movie player in Sony Xperia android devices
                    } else if (getlistpackage.doesPackageExist("com.android.gallery3d", activity)) {
                        intent.setClassName("com.android.gallery3d", "com.android.gallery3d.app.MovieActivity");
                        //open movie player in Samsung TouchWiz based ROMs
                    } else if (getlistpackage.doesPackageExist("com.cooliris.media", activity)) {
                        intent.setClassName("com.cooliris.media", "com.cooliris.media.MovieView");
                    }
                    activity.startActivity(intent);
                } catch (Exception e) {
                    Log.d("filePlayError: ", e.getLocalizedMessage());

                    try {
                        Intent intentj = new Intent();
                        intentj.setAction(Intent.ACTION_VIEW);
                        intentj.setDataAndType(Uri.parse(filepath), "video/*");
                        activity.startActivity(intentj);
                    } catch (Exception e1) {
                        Log.d("filePlayError: ", e1.getLocalizedMessage());
                        Toast.makeText(activity, "Sorry, Stream not working properly , but you can download video", Toast.LENGTH_LONG).show();
                    }
                }

            } else if (!reg.getBack(filepath,
                    "((\\.3ga(\\?[^?]+)?$)" +
                            "|(\\.aac(\\?[^?]+)?$)" +
                            "|(\\.aif(\\?[^?]+)?$)" +
                            "|(\\.aifc(\\?[^?]+)?$)" +
                            "|(\\.aiff(\\?[^?]+)?$)" +
                            "|(\\.amr(\\?[^?]+)?$)" +
                            "|(\\.au(\\?[^?]+)?$)" +
                            "|(\\.aup(\\?[^?]+)?$)" +
                            "|(\\.caf(\\?[^?]+)?$)" +
                            "|(\\.flac(\\?[^?]+)?$)" +
                            "|(\\.gsm(\\?[^?]+)?$)" +
                            "|(\\.kar(\\?[^?]+)?$)" +
                            "|(\\.m4a(\\?[^?]+)?$)" +
                            "|(\\.m4p(\\?[^?]+)?$)" +
                            "|(\\.m4r(\\?[^?]+)?$)" +
                            "|(\\.mid(\\?[^?]+)?$)" +
                            "|(\\.midi(\\?[^?]+)?$)" +
                            "|(\\.mmf(\\?[^?]+)?$)" +
                            "|(\\.mp2(\\?[^?]+)?$)" +
                            "|(\\.mp3(\\?[^?]+)?$)" +
                            "|(\\.mpga(\\?[^?]+)?$)" +
                            "|(\\.ogg(\\?[^?]+)?$)" +
                            "|(\\.oma(\\?[^?]+)?$)" +
                            "|(\\.opus(\\?[^?]+)?$)" +
                            "|(\\.qcp(\\?[^?]+)?$)" +
                            "|(\\.ra(\\?[^?]+)?$)" +
                            "|(\\.ram(\\?[^?]+)?$)" +
                            "|(\\.wav(\\?[^?]+)?$)" +
                            "|(\\.wma(\\?[^?]+)?$)" +
                            "|(\\.xspf(\\?[^?]+)?$))").isEmpty()) {

                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(filepath), "audio/*");
                    activity.startActivity(intent);

                } catch (Exception j) {
                    Log.d("filePlayError: ", j.getLocalizedMessage());
                }
            }
        }
    }

    public static class reg {
        public static String getBack(String html, String regex) {
            Pattern patt = Pattern.compile(regex);
            Matcher match = patt.matcher(html);
            while (match.find()) {
                return match.group(1);
            }
            return "";
        }

        public static ArrayList<String> getBackArray(String html, String regex) {
            Pattern patt = Pattern.compile(regex);
            Matcher match = patt.matcher(html);
            ArrayList<String> mylist = new ArrayList<>();
            while (match.find()) {
                mylist.add(match.group(1));
            }
            return mylist;
        }
    }

    public static class runApp {
        public static void start(String packagename, Context context) {
            if (getlistpackage.doesPackageExist(packagename, context.getApplicationContext())) {
                Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(packagename);
                context.startActivity(LaunchIntent);
            } else {
                try {
                    context.startActivity((new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packagename))).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } catch (Exception e) {
                    context.startActivity((new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packagename))).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }

        }
    }

    public static class share {

        public static void mShare(String filepath, Activity activity) {

            Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse(String.valueOf(filepath)));
            File file = new File(filepath);

            if (!filepath.contains(".mp4")) {
                intent.setDataAndType(Uri.fromFile(file), "image/*");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                activity.startActivity(Intent.createChooser(intent, "Share Image using"));
            } else {
                intent.setDataAndType(Uri.fromFile(file), "video/*");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                activity.startActivity(Intent.createChooser(intent, "Share video using"));

            }

        }

        public static void mShareText(String text, Activity activity) {
            Intent myapp = new Intent(Intent.ACTION_SEND);
            myapp.setType("text/plain");
            myapp.putExtra(Intent.EXTRA_TEXT, text);
            activity.startActivity(myapp);

        }
    }
}