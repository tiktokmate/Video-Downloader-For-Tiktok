package com.leetlab.videodownloaderfortiktok.ui.fragment;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoController;
import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.api.ApiClient;
import com.leetlab.videodownloaderfortiktok.api.ApiService;
import com.leetlab.videodownloaderfortiktok.api.URLS;
import com.leetlab.videodownloaderfortiktok.listener.DownloadDialogListener;
import com.leetlab.videodownloaderfortiktok.listener.PrivateAlertDialogListner;
import com.leetlab.videodownloaderfortiktok.model.InstaContent;
import com.leetlab.videodownloaderfortiktok.model.InstaUserModel;
import com.leetlab.videodownloaderfortiktok.model.PostModel;
import com.leetlab.videodownloaderfortiktok.model.UserDetailModel;
import com.leetlab.videodownloaderfortiktok.model.UserMediaModel;
import com.leetlab.videodownloaderfortiktok.ui.activity.Main2Activity;
import com.leetlab.videodownloaderfortiktok.utils.AccountDB;
import com.leetlab.videodownloaderfortiktok.utils.Admob;
import com.leetlab.videodownloaderfortiktok.utils.AesCipher;
import com.leetlab.videodownloaderfortiktok.utils.AppConstants;
import com.leetlab.videodownloaderfortiktok.utils.Downloadtiktokvideos;
import com.leetlab.videodownloaderfortiktok.utils.InternetConectionn;
import com.leetlab.videodownloaderfortiktok.utils.Keys;
import com.leetlab.videodownloaderfortiktok.utils.MyApplication;
import com.leetlab.videodownloaderfortiktok.utils.PaperUtil;
import com.leetlab.videodownloaderfortiktok.utils.SavedPreferences;
import com.leetlab.videodownloaderfortiktok.utils.UIUtil;
import com.leetlab.admob.AdMobUtils;
import com.leetlab.utils.Contants.Constants;
import com.leetlab.utils.dialog.ProgressDialog;
import com.leetlab.utils.dialog.ProgressType;
import com.anjlab.android.iab.v3.BillingProcessor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Retrofit;

import static android.content.Context.CLIPBOARD_SERVICE;

public class InstaDownloaderFragment extends Fragment implements DownloadDialogListener {

    private static final String TAG = "InstaDownloaderFragment";

    @BindView(R.id.et_url)
    EditText etUrl;
    @BindView(R.id.pasteTxt)
    TextView pasteTxt;
    @BindView(R.id.urlLayout)
    ConstraintLayout urlLayout;
    @BindView(R.id.downloadBtn)
    Button downloadBtn;
    //    @BindView(R.id.adView)
//    NativeExpressAdView mAdView;
    @BindView(R.id.contentMain)
    ConstraintLayout contentMain;
    @BindView(R.id.lyt_how_it_works)
    ConstraintLayout lytHowItWorks;
    @BindView(R.id.HowItWorks)
    TextView HowItWorks;
    @BindView(R.id.nativeAdTemplate)
    TemplateView nativeAdTemplate;


    private PrivateAlertDialogListner privateAlertDialogListner;

    private ClipboardManager clipboard;
    private int type = 1;
    ProgressDialog dialog;
    AdMobUtils adMobUtils;

    private String html = "", desc = "", imagina = "", url = "", video = "", videoArray = "";

    private BillingProcessor bp;
    private boolean readyToPurchase = false;

    private UserMediaModel userMediaModel;
    private UserDetailModel userDetailModel;
    private SavedPreferences savedPreferences;
    private InstaUserModel instaUserModel;
    private int mStatusCode = 500;

    VideoController mVideoController;
    boolean isVideo;

    public InstaDownloaderFragment(boolean isVideo) {
        this.isVideo = isVideo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_insta_downloader, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        adMobUtils = new AdMobUtils(getContext(), Admob.APP_ID);
        adMobUtils.initInterstitialAd(Admob.INTESTITIAL_ID);
        if (!Admob.isPremiumFeature())
            showNativeAd();


        userMediaModel = new UserMediaModel();
        savedPreferences = new SavedPreferences();

        if (AccountDB.getAccounts().size() != 0) {
            instaUserModel = AccountDB.getCurrentAccount();
        }

        dialog = new ProgressDialog(getActivity(), ProgressType.HORIZONTAL);
        dialog.setMessage("Resolving url...");
        dialog.setRadius(12);
        clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);

        if (!"".equals(Main2Activity.URL)) {
            check_url(Main2Activity.URL);
        }
    }

    private void showNativeAd() {
        AdLoader.Builder builder = new AdLoader.Builder(getContext(), Admob.NATIVE_AD_ID);
        AdLoader loader = builder.forUnifiedNativeAd(unifiedNativeAd -> {
            nativeAdTemplate.setNativeAd(unifiedNativeAd);
        }).build();
        loader.loadAd(new AdRequest.Builder().build());
    }

    public void runAutoDownload() {

    }

    public void initAutoDwonaloder() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip()) {
            Log.d(MyApplication.AUTO_DOWNLOAD_LOGS, "Primary Clip");
            android.content.ClipDescription description = clipboard.getPrimaryClipDescription();
            android.content.ClipData data = clipboard.getPrimaryClip();
            if (data != null && description != null && description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                String url = (String) clipboard.getText();
                verify_url(url);
                System.out.println("data=" + data + "description=" + description + "url=" + url);
                Log.d(MyApplication.AUTO_DOWNLOAD_LOGS, "Data available");
            } else {
                Log.d(MyApplication.AUTO_DOWNLOAD_LOGS, "Data not available");
            }
        } else {
            Log.d(MyApplication.AUTO_DOWNLOAD_LOGS, "No Primary Clip");
        }

        if (PaperUtil.isAutoDownload()) {
            if (readFromClipboard(getContext()) != null) {
                Log.e(TAG, "onViewCreated: " + readFromClipboard(getContext()));
                String url = readFromClipboard(getContext());
                if (verify_url(url)) {
                    check_url(url);
                }
            } else {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @OnClick({R.id.pasteTxt, R.id.downloadBtn, R.id.lyt_how_it_works, R.id.HowItWorks})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pasteTxt:
                if (clipboard.getText() != null)
                    etUrl.setText(clipboard.getText());
                break;
            case R.id.downloadBtn:
                if (!InternetConectionn.CheckInternetConnection(getContext())) {
                    UIUtil.popError(getActivity());
                    return;
                }
                check_url(etUrl.getText().toString());
                break;
            case R.id.lyt_how_it_works:
            case R.id.HowItWorks:
                AppConstants.watchYoutubeVideo(getContext(), Constants.YOUTUBE_VIDEO_ID);
                break;
        }
    }

    public Boolean verify_url(String obj) {
        if (obj.length() <= 0) {
            return false;
        }
        if (!obj.contains("instagram.com")) {
            return false;
        }

        etUrl.setText(obj);
        return true;

    }

    public void check_url(String obj) {

        if (!obj.contains("tiktok") || TextUtils.isEmpty(obj)) {
            Toast.makeText(getContext(), "Invalid URL", Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            getTikTokAPI(obj);

        } catch (Exception unused) {
            Log.e(TAG, "check_url: " + unused.getMessage());
            Toast.makeText(getContext(), "Invalid URL", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        privateAlertDialogListner = (PrivateAlertDialogListner) context;
    }


    public void getTikTokAPI(final String url) {
        dialog.show();
        AesCipher encrypted = AesCipher.encrypt(Keys.secretKey(), url);
        String data = encrypted.getData();
        Retrofit retrofit = ApiClient.getClient(getActivity(), true);
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

                Toast.makeText(getActivity(), "Something went wrong, Try again!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Downloadtiktokvideos> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong, Try again!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void settingProfileData(Downloadtiktokvideos userDetailModel) {
        PostModel post = new PostModel();
        post.fullName = userDetailModel.getNickname();
        post.profileUrl =  userDetailModel.getAvatar();
        post.totalComments = "0";
        post.totalLikes = "0";
        post.caption = "caption";
        post.username =  userDetailModel.getUsername();
        List<InstaContent> contents = new ArrayList<>();
        if (isVideo) {
            contents.add(new InstaContent(userDetailModel.getUrl(), isVideo, userDetailModel.getThumbnail()));
            post.caption = userDetailModel.getTitle();
        } else {
            contents.add(new InstaContent(userDetailModel.getMusic_url(), isVideo, userDetailModel.getMusic_thumb()));
            post.caption = userDetailModel.getMusic();
        }

        post.instaContent = contents;
        DownloadDialog.newInstance(post).show(getFragmentManager(), "download");
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onDownloadFinish() {
        AppConstants.clearClipBoard(Objects.requireNonNull(getActivity()));
        if (!Admob.isPremiumFeature()) {
            adMobUtils.showInterstitialAd(null);
        }
    }

    @SuppressLint("NewApi")
    public String readFromClipboard(Context context) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                    .getSystemService(context.CLIPBOARD_SERVICE);
            return clipboard.getText().toString();
        } else {
            ClipboardManager clipboard = (ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);

            // Gets a content resolver instance
            ContentResolver cr = context.getContentResolver();

            // Gets the clipboard data from the clipboard
            ClipData clip = clipboard.getPrimaryClip();
            if (clip != null) {

                String text = null;
                String title = null;

                ClipData.Item item = clip.getItemAt(0);
                Uri uri = item.getUri();

                if (text == null) {
                    text = coerceToText(context, item).toString();
                }

                return text;
            }
        }
        return "";
    }

    @SuppressLint("NewApi")
    public CharSequence coerceToText(Context context, ClipData.Item item) {
        CharSequence text = item.getText();
        if (text != null) {
            return text;
        }
        Uri uri = item.getUri();
        if (uri != null) {
            FileInputStream stream = null;
            try {
                AssetFileDescriptor descr = context.getContentResolver()
                        .openTypedAssetFileDescriptor(uri, "text/*", null);
                stream = descr.createInputStream();
                InputStreamReader reader = new InputStreamReader(stream,
                        "UTF-8");

                StringBuilder builder = new StringBuilder(128);
                char[] buffer = new char[8192];
                int len;
                while ((len = reader.read(buffer)) > 0) {
                    builder.append(buffer, 0, len);
                }
                return builder.toString();

            } catch (FileNotFoundException e) {

            } catch (IOException e) {
                Log.w("ClippedData", "Failure loading text", e);
                return e.toString();

            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                    }
                }
            }
            return uri.toString();
        }
        Intent intent = item.getIntent();
        if (intent != null) {
            return intent.toUri(Intent.URI_INTENT_SCHEME);
        }
        return "";
    }

}
