package com.leetlab.videodownloaderfortiktok.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.leetlab.videodownloaderfortiktok.api.ApiClient;
import com.leetlab.videodownloaderfortiktok.api.ApiService;
import com.leetlab.videodownloaderfortiktok.api.URLS;
import com.leetlab.videodownloaderfortiktok.listener.PrivateAlertDialogListner;
import com.leetlab.videodownloaderfortiktok.model.InstaContent;
import com.leetlab.videodownloaderfortiktok.model.InstaUserModel;
import com.leetlab.videodownloaderfortiktok.model.UserDetailModel;
import com.leetlab.videodownloaderfortiktok.model.UserMediaModel;
import com.leetlab.videodownloaderfortiktok.utils.Admob;
import com.leetlab.videodownloaderfortiktok.utils.AesCipher;
import com.leetlab.videodownloaderfortiktok.utils.AppConstants;
import com.leetlab.videodownloaderfortiktok.utils.Downloadtiktokvideos;
import com.leetlab.videodownloaderfortiktok.utils.Keys;
import com.leetlab.videodownloaderfortiktok.utils.SavedPreferences;
import com.leetlab.admob.AdMobUtils;
import com.leetlab.videodownloaderfortiktok.listener.DownloadDialogListener;
import com.leetlab.videodownloaderfortiktok.ui.fragment.DownloadDialog;
import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.model.PostModel;
import com.leetlab.utils.dialog.ProgressDialog;
import com.leetlab.utils.dialog.ProgressType;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;


public class TransparentActivity extends AppCompatActivity implements DownloadDialogListener {

    private static final int RC_LOGIN = 121;

    public static String TAG = "TransparentActivity";
    Activity activity;
    private ProgressDialog dialog;

    private String html = "", desc = "", imagina = "", url = "", video = "", videoArray = "";
    private AdMobUtils adMobUtils;

    private UserMediaModel userMediaModel;
    private UserDetailModel userDetailModel;
    private SavedPreferences savedPreferences;
    private InstaUserModel instaUserModel;
    private int mStatusCode = 500;

    private PrivateAlertDialogListner privateAlertDialogListner;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);
        ButterKnife.bind(this);
        activity = TransparentActivity.this;
        adMobUtils = new AdMobUtils(this, Admob.APP_ID);
        adMobUtils.initInterstitialAd(Admob.INTESTITIAL_ID);
        dialog = new ProgressDialog(this, ProgressType.HORIZONTAL);
        dialog.setMessage("Resolving url...");
        dialog.setRadius(12);

        savedPreferences = new SavedPreferences();
//        resolveUrl(getUrl(getIntent().getStringExtra("copiedLink")));
        check_url();


    }

    public void check_url() {
        dialog.show();
        String obj = getIntent().getStringExtra("copiedLink");
//        if (obj.length() <= 0) {
//            etUrl.setError("Enter url.");
//            return;
//        }

        if (!obj.contains("tiktok") || TextUtils.isEmpty(obj)) {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            checkingPrivateDataOrNot(obj);

        } catch (Exception unused) {
            Toast.makeText(getApplicationContext(), "Invalid URL", Toast.LENGTH_SHORT).show();
        }
    }


    private void checkingPrivateDataOrNot(String url) {
        AesCipher encrypted = AesCipher.encrypt(Keys.secretKey(), url);
        String data = encrypted.getData();
        Retrofit retrofit = ApiClient.getClient(TransparentActivity.this, true);
        ApiService apiService = retrofit.create(ApiService.class);
        Call<Downloadtiktokvideos> call = apiService.getAPI(URLS.url, data);
        call.enqueue(new retrofit2.Callback<Downloadtiktokvideos>() {
            @Override
            public void onResponse(Call<Downloadtiktokvideos> call, retrofit2.Response<Downloadtiktokvideos> response) {
                dialog.dismiss();
                try {
                    if (response.isSuccessful()) {

                        Downloadtiktokvideos downloadtiktokvideos = response.body();
                        if (!TextUtils.isEmpty(downloadtiktokvideos.getUrl())) {
                            settingProfileData(downloadtiktokvideos);
                            return;
                        }
                    }
                } catch (Exception e) {
                    Log.e("Api", e.getMessage());
                }

                Toast.makeText(TransparentActivity.this, "Something went wrong, Try again!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Downloadtiktokvideos> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(TransparentActivity.this, "Something went wrong, Try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void settingProfileData(Downloadtiktokvideos userDetailModel) {
        PostModel post = new PostModel();
        post.fullName = "fullName";
        post.profileUrl = "";
        post.totalComments = "0";
        post.totalLikes = "0";
        post.caption = "caption";
        post.username = "username";
        List<InstaContent> contents = new ArrayList<>();
        contents.add(new InstaContent(userDetailModel.getUrl(), true, userDetailModel.getThumbnail()));


        post.instaContent = contents;
        DownloadDialog.newInstance(post).show(getSupportFragmentManager(), "download");
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LOGIN && resultCode == RESULT_OK) {
            check_url();
        }
    }

    @Override
    public void onDownloadFinish() {
        AppConstants.clearClipBoard(this);
        if (!Admob.isPremiumFeature())
            adMobUtils.showInterstitialAd((isLoaded, interstitial) -> {
            });
        finish();
    }
}
