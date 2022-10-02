package com.leetlab.videodownloaderfortiktok.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.utils.dialog.AlertDialog;

public class UIUtil {

    public static boolean isValid(String url) {
        if (url.length() <= 6) {
            return false;
        }
        if (url.startsWith("https://www.instagram.com") && url.contains("?")) {
            if (getUrl(url) != null) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String getUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        url = url.substring(0, url.indexOf("?"));
        url = url.concat("?__a=1");
        return url;
    }

    public static void popError(Activity activity) {
        AlertDialog.init(activity).setBackgroundColor(R.color.colorWhite).setCancelable(false)
                .setMessage("Something went wrong please check your internet connection")
                .setTitle("No Internet Connection").animate().setRadius(18).show();
    }

    public static void popMessage(Activity activity) {
        AlertDialog.init(activity).setBackgroundColor(R.color.colorWhite).setCancelable(false)
                .setMessage("The link you are accessing is private, To continue please login first.")
                .setTitle("Oops").animate().setRadius(8).show();
    }

    public static void showDialog(Activity activity) {
//        FragmentManager fm = getFragmentManager();
//        PrivateUserAlert info = new PrivateUserAlert(() -> {
//
//        });
//        info.show(fm, "fragment_info");

//        ImageView imageView = dialog.findViewById(R.id.imageView);
//
//        Glide.with(activity).load(R.drawable.ic_instagram).into(imageView);
//
//        Button dialogButton1 = dialog.findViewById(R.id.cancel_btn);
//        dialogButton1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        Button dialogButton2 = dialog.findViewById(R.id.login_btn);
//        dialogButton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity, LoginActivity.class);
//                intent.putExtra("type", 1);
//                intent.putExtra("isFb", false);
//                activity.startActivity(intent);
//                dialog.dismiss();
//            }
//        });

    }
}
