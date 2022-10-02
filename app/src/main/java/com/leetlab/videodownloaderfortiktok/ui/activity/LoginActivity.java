package com.leetlab.videodownloaderfortiktok.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.model.Cookies;
import com.leetlab.videodownloaderfortiktok.model.InstaUserModel;
import com.leetlab.videodownloaderfortiktok.utils.AccountDB;
import com.leetlab.videodownloaderfortiktok.utils.MySingleton;
import com.leetlab.videodownloaderfortiktok.utils.SavedPreferences;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.leetlab.utils.Contants.Constants;

import org.json.JSONObject;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private ViewHolder viewHolder;
    private boolean isFb = false;
    private InstaUserModel instaUserModel;
    private SavedPreferences savedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewHolder = new ViewHolder(getWindow().getDecorView());

        savedPreferences = new SavedPreferences();
        bindData();
        settingListeners();

    }

    private void settingListeners() {
        viewHolder.swipeRefresh.setOnRefreshListener(() -> bindData());
    }

    private void bindData() {
        viewHolder.webView.getSettings().setLoadsImagesAutomatically(true);
        viewHolder.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        viewHolder.webView.getSettings().setBuiltInZoomControls(true);
        viewHolder.webView.getSettings().setJavaScriptEnabled(true);
        viewHolder.webView.getSettings().setDomStorageEnabled(true);
        viewHolder.webView.getSettings().setUserAgentString(isFb ? "Instagram 9.3.0 Android (22/5.1; 480dpi; 1080x1776; LG; Google Nexus 5 - 5.1.0 - API 22 - 1080x1920; armani; qcom; en_US)" : "Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9300 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
        viewHolder.webView.loadUrl("https://www.instagram.com/accounts/login/");
        CookieManager.getInstance().removeAllCookie();
        viewHolder.webView.addJavascriptInterface(new javaScriptInterface(), "java_obj");
        viewHolder.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("WEB URL ===", url + "");
                viewHolder.webView.loadUrl(url);
                instaCookie(viewHolder.webView, url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                viewHolder.progressBar.setVisibility(View.VISIBLE);
                if (viewHolder.swipeRefresh.isRefreshing()) {
                    viewHolder.swipeRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                viewHolder.progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onLoadResource(WebView view, String str) {
                super.onLoadResource(view, str);
                if (isValid(str)) {
                    /*UtilMethods.LogMethod("LoginActivityloadR", str);*/
                    if (str.equals("https://www.instagram.com/") ||
                            str.contains("one_tap_web_login/") ||
                            str.contains("only_stories")) {
                        viewHolder.progressBar.setVisibility(View.VISIBLE);
                        viewHolder.webView.setVisibility(View.VISIBLE);
                    }
                }
                if (isFb && str.startsWith("https://graph.instagram.com/logging_client_events")) {
                    viewHolder.webView.loadUrl("javascript:(function(){var l,x=document.getElementsByClassName('coreSpriteFacebookIconInverted');if(x.length>0){l=x[0]}else{l=document.getElementsByClassName('glyphsSpriteFB_Logo')[0]}e=document.createEvent('HTMLEvents');e.initEvent('click',true,true);l.dispatchEvent(e);})()");
                }
            }
        });

    }

    public final class javaScriptInterface {
        @JavascriptInterface
        public void showDescription(String str) {
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("====>html=");
            stringBuilder.append(str);
            printStream.println(stringBuilder.toString());
        }

        @JavascriptInterface
        public void showSource(String str) {
            if (isValid(str)) {
                str = extractUrl(str, "'feed',", "});</script");
                if (isValid(str)) {
                    try {
//                        FeedFirstBean feedFirstBean = (FeedFirstBean) new C0348qf().a(str, FeedFirstBean.class);
//                        if (!(feedFirstBean == null || feedFirstBean.getUser() == null)) {
//                            LoginActivity.this.c = feedFirstBean.getUser();
//                            e.a().b(new Kw(str, false));
//                        }
                    } catch (Exception unused) {
                    }
                }
            }
        }
    }

    public static boolean isValid(String str) {
        return (str == null || str.trim().length() == 0) ? false : true;
    }

    public static String extractUrl(String str, String str2, String str3) {
        try {
            return str.substring(str.indexOf(str2), str.indexOf(str3) + 1).substring(str2.length());
        } catch (Exception unused) {
            return "";
        }
    }

    private void instaCookie(WebView webView, String str) {
        webView.loadUrl("javascript:window.java_obj.showSource(document.getElementsByTagName('html')[0].innerHTML);");
        webView.loadUrl("javascript:window.java_obj.showDescription(document.querySelector('meta[name=\"share-description\"]').getAttribute('content'));");
        viewHolder.progressBar.setVisibility(View.GONE);
        CookieSyncManager.getInstance().sync();
        String cookie = CookieManager.getInstance().getCookie(str);
        if (cookie != null) {
            int i = 0;
            for (String str2 : cookie.split(";")) {
                if (str2.contains("mcd=") ||
                        str2.contains("mid=") ||
                        str2.contains("csrftoken=") ||
                        str2.contains("ds_user_id=") ||
                        str2.contains("sessionid=") ||
                        str2.contains("rur=") ||
                        str2.contains("urlgen=")) {
                    i++;
                }
            }
            if (cookie.contains("sessionid") && !cookie.contains("sessionid%3D%3") && i >= 5) {
                Cookies.setCook(cookie);
                instaUserModel = new InstaUserModel();
                instaUserModel.setCookie(cookie);
                instaUserModel.setCsrftoken(Cookies.getInstance().getCsrftoken());
                instaUserModel.setSessionid(Cookies.getInstance().getSessionid());
                instaUserModel.setUrlgen(Cookies.getInstance().getUrlgen());
                instaUserModel.setDs_user_id(Cookies.getInstance().getDs_user_id());
                instaUserModel.setMcd(Cookies.getInstance().getMcd());
                instaUserModel.setMid(Cookies.getInstance().getMid());
                instaUserModel.setIs_login(true);

                String url = String.format("https://i.instagram.com/api/v1/users/%s/info",
                        instaUserModel.getDs_user_id());
                Log.d("Session Id ===", instaUserModel.getSessionid() + "");
                parsingUserData(url);
            }
        }
    }

    private void parsingUserData(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                InstaUserModel instaUserModel1 = savedPreferences.parsingInstaUserModel(response);
                instaUserModel1.setCsrftoken(instaUserModel.getCsrftoken());
                instaUserModel1.setSessionid(instaUserModel.getSessionid());
                instaUserModel1.setUrlgen(instaUserModel.getUrlgen());
                instaUserModel1.setDs_user_id(instaUserModel.getDs_user_id());
                instaUserModel1.setMcd(instaUserModel.getMcd());
                instaUserModel1.setMid(instaUserModel.getMid());
                AccountDB.addAccount(instaUserModel1);
                AccountDB.setCurrentAccount(instaUserModel1);
                setResult(RESULT_OK);
                finish();

            }
        }, error -> error.printStackTrace()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+");
                headers.put("Cookie", "ds_user_id=" + instaUserModel.getDs_user_id() + ";sessionid=" + instaUserModel.getSessionid());
                return headers;

            }
        };

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constants.STATE = false;
    }

    static
    class ViewHolder {
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.web_view)
        WebView webView;
        @BindView(R.id.swipe_refresh)
        SwipeRefreshLayout swipeRefresh;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
