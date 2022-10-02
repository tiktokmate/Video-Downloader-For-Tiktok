package com.leetlab.videodownloaderfortiktok.ui.activity;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.listener.DownloadDialogListener;
import com.leetlab.videodownloaderfortiktok.model.InstaDpModel;
import com.leetlab.videodownloaderfortiktok.model.PostModel;
import com.leetlab.videodownloaderfortiktok.services.ClipboardService;
import com.leetlab.videodownloaderfortiktok.ui.fragment.DownloadDialog;
import com.leetlab.videodownloaderfortiktok.ui.fragment.InstaDownloaderFragment;
import com.leetlab.videodownloaderfortiktok.ui.fragment.InstaDpBottomSheet;
import com.leetlab.videodownloaderfortiktok.ui.fragment.InstaDpFragment;
import com.leetlab.videodownloaderfortiktok.ui.fragment.InstaStoryFragmnet;
import com.leetlab.videodownloaderfortiktok.ui.fragment.MainDownloadFragment;
import com.leetlab.videodownloaderfortiktok.ui.fragment.PrivateUserAlert;
import com.leetlab.videodownloaderfortiktok.utils.Admob;
import com.leetlab.videodownloaderfortiktok.utils.AppConstants;
import com.leetlab.videodownloaderfortiktok.utils.InternetConectionn;
import com.leetlab.videodownloaderfortiktok.utils.SettingsFragment;
import com.leetlab.videodownloaderfortiktok.utils.volley.RequestUtil;
import com.leetlab.admob.AdMobUtils;
import com.leetlab.utils.dialog.AlertDialog;
import com.leetlab.utils.dialog.ProgressDialog;
import com.leetlab.utils.dialog.ProgressType;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pd.chocobar.ChocoBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements DownloadDialogListener {

    private static final String TAG = "HomeActivity";

    @BindView(R.id.et_url)
    EditText etUrl;
    @BindView(R.id.bottomNav)
    BottomNavigationView bottomNav;
    //    @BindView(R.id.toolbar)
//    Toolbar toolbar;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.ic_more)
    ImageView icMore;
    @BindView(R.id.ic_insta)
    ImageView icInsta;
    @BindView(R.id.ic_noads)
    ImageView icNoads;

    private ClipboardManager clipboard;
    private int type = 1;
    ProgressDialog dialog;
    AdMobUtils adMobUtils;

    private String html = "", desc = "", imagina = "", url = "", video = "", videoArray = "";

    private BillingProcessor bp;
    private boolean readyToPurchase = false;

    private InstaDownloaderFragment InstaFragment = new InstaDownloaderFragment(true);
    private InstaDpFragment DpFragment = new InstaDpFragment();
    private InstaStoryFragmnet storyFragmnet = new InstaStoryFragmnet();
    private MainDownloadFragment downloadFragment = new MainDownloadFragment();
    private Fragment activeFragment = InstaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bp = new BillingProcessor(this, getResources().getString(R.string.LICENSE_KEY), getResources().getString(R.string.MERCHANT_ID), new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
                Admob.buyPremiumFeature();
                ChocoBar.builder().setActivity(MainActivity.this).setActionText("Sure")
                        .setActionClickListener(v -> {
                        })
                        .setText(getResources().getString(R.string.Ads_remove))
                        .setDuration(ChocoBar.LENGTH_INDEFINITE)
                        .build()
                        .show();


            }

            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
                ChocoBar.builder().setActivity(MainActivity.this).setActionText("Sure")
                        .setActionClickListener(v -> {
                        })
                        .setText(getResources().getString(R.string.No_Service_Avalabile))
                        .setDuration(ChocoBar.LENGTH_INDEFINITE)
                        .build()
                        .show();
            }

            @Override
            public void onBillingInitialized() {

            }

            @Override
            public void onPurchaseHistoryRestored() {
            }
        });


        adMobUtils = new AdMobUtils(this, Admob.APP_ID);
        adMobUtils.initInterstitialAd(Admob.INTESTITIAL_ID);
        if (!Admob.isPremiumFeature())
            adMobUtils.loadBannerAd(adView, Admob.BANNER_ID, AdSize.MEDIUM_RECTANGLE);

        permissionBatteryOptimization();
        CheckUpdate();

        dialog = new ProgressDialog(this, ProgressType.HORIZONTAL);
        dialog.setMessage("Resolving url...");
        dialog.setRadius(12);
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        setupNavigationView();
        startForegroundService();
        startForegroundService();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_instagram:
                Uri uri = Uri.parse("http://instagram.com");
                Intent insta = new Intent(Intent.ACTION_VIEW, uri);
                insta.setPackage("com.instagram.android");
                if (isIntentAvailable(MainActivity.this, insta)) {
                    startActivity(insta);
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com")));
                }
//                       startActivity(getPackageManager().getLaunchIntentForPackage("com.instagram.android"));
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.action_tutorial:
                startActivity(new Intent(MainActivity.this, IntroActivity.class));
                break;
        }
        return true;
    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void CheckUpdate() {
        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this)
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(Update update, Boolean isUpdateAvailable) {
                        Log.d("Latest Version", update.getLatestVersion());
                        Log.d("Latest Version Code", String.valueOf(update.getLatestVersionCode()));
                        Log.d("Release notes", update.getReleaseNotes());
                        Log.d("URL", String.valueOf(update.getUrlToDownload()));
                        Log.d("Is update available?", Boolean.toString(isUpdateAvailable));
                        if (isUpdateAvailable) {
                            UpdateOnPlayStore();
                        }
                    }

                    @Override
                    public void onFailed(AppUpdaterError error) {
                        Log.d("AppUpdater Error", "Something went wrong");
                    }
                });
        appUpdaterUtils.start();

    }

    private void UpdateOnPlayStore() {
        new AppUpdater(MainActivity.this)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .setDisplay(Display.NOTIFICATION)
                .showAppUpdated(true)
                .start();

        new AppUpdater(MainActivity.this)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .setDisplay(Display.DIALOG)
                .showAppUpdated(true)
                .setCancelable(false)
                .setButtonDoNotShowAgain("")
                .setButtonDismiss("Latter")
                .setButtonDoNotShowAgainClickListener((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        int id = (type == 1) ? R.id.menu_photos_videos : R.id.menu_dp_saver;
        bottomNav.setSelectedItemId(id);
    }

    private void setupNavigationView() {
        bottomNav.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_photos_videos:
                    etUrl.getText().clear();
                    etUrl.setHint("Paste Instagram URL here");
                    type = 1;
//                    if (!PaperUtil.isPhotoVideosShow()) {
//                        openTutorial(getTutorial(Constantss.TYPE_PHOTO_VIDEOS), Constantss.TYPE_PHOTO_VIDEOS);
//                    }
                    break;
                case R.id.menu_dp_saver:
                    etUrl.getText().clear();
                    etUrl.setHint("Instagram Username or Profile Url");
                    type = 2;
//                    if (!PaperUtil.isDPSaverShow()) {
//                        openTutorial(getTutorial(Constantss.TYPE_DP_SAVER), Constantss.TYPE_DP_SAVER);
//                    }
                    break;
                case R.id.menu_downloads:
                    startActivity(new Intent(MainActivity.this, DownloadActivity.class));
                    break;
            }
            return true;
        });
    }

    @OnClick({R.id.pasteTxt, R.id.downloadBtn, R.id.ic_more, R.id.ic_insta, R.id.btnFBLogin, R.id.ic_noads})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pasteTxt:
                if (clipboard.getText() != null)
                    etUrl.setText(clipboard.getText());
                break;
            case R.id.downloadBtn:
                if (type == 1)
                    VerifyInstagramUrl();
                else
                    instaDPUrlVerification();
                break;
            case R.id.ic_more:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.ic_insta:
                OpenInsta();
                break;
            case R.id.ic_noads:
                if (!BillingProcessor.isIabServiceAvailable(this)) {
                    ChocoBar.builder().setActivity(MainActivity.this).setActionText("Sure")
                            .setActionClickListener(v -> {
                            })
                            .setText(getResources().getString(R.string.No_Service_Avalabile))
                            .setDuration(ChocoBar.LENGTH_INDEFINITE)
                            .build()
                            .show();
                    return;
                }
                bp.purchase(this, getResources().getString(R.string.PRODUCT_ID));
                break;
            case R.id.btnFBLogin:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("isFb", false);
                startActivity(intent);
                break;
        }
    }

    private void OpenInsta() {
        Uri uri = Uri.parse("http://instagram.com");
        Intent insta = new Intent(Intent.ACTION_VIEW, uri);
        insta.setPackage("com.instagram.android");
        if (isIntentAvailable(MainActivity.this, insta)) {
            startActivity(insta);
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com")));
        }
    }


    private void VerifyInstagramUrl() {
        if (!etUrl.getText().toString().isEmpty()) {
            if (etUrl.getText().toString().startsWith("https://www.instagram.com") && etUrl.getText().toString().contains("?")) {
                if (getUrl() != null) {
                    if (InternetConectionn.CheckInternetConnection(this))
                        resolveUrl(getUrl());
                    else {
                        AlertDialog.init(this).setBackgroundColor(R.color.colorWhite).setCancelable(false)
                                .setMessage("Something went wrong please check your internet connection")
                                .setTitle("No Internet Connection").animate().setRadius(18).show();
                    }

                }
            } else {
                etUrl.setError("Instagram url not found");
            }

        } else {
            etUrl.setError("Paste url");
        }
    }

    private void resolveUrl(String url) {
        dialog.show();
        Log.e(TAG, "getUrl: Url: " + url);
        RequestUtil.init(this).fetchPost(url, (isSuccessFull, post) -> {
            dialog.dismiss();
            if (!isSuccessFull) {
                getPrivateAccountInfo(etUrl.getText().toString());
                return;
            }
            showBottomDialog(post);
        });
    }

    private String getUrl() {
        String url = etUrl.getText().toString();
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(this, "Paste Instagram URL", Toast.LENGTH_SHORT).show();
            return null;
        }


//        if (url.substring(0, url.indexOf("?"))
        url = url.substring(0, url.indexOf("?"));
        url = url.concat("?__a=1");
        return url;
    }

    private void instaDPUrlVerification() {
        dialog.show();
        if (etUrl.getText().toString().trim().isEmpty()) {
            etUrl.setError("Enter User Name First!");
            dialog.dismiss();
        } else if (etUrl.getText().toString().trim().contains("https://instagram.com/")) {
            String mMainString = etUrl.getText().toString().trim();
            String[] strings = mMainString.split("/");
            String x = strings[strings.length - 1];
            String[] strings1 = x.split("\\?");
            dialog.show();
            instaDPApiRequest(strings1[0]);
        } else {
            if (InternetConectionn.CheckInternetConnection(this)) {
                instaDPApiRequest(etUrl.getText().toString().trim());
                dialog.show();
            } else {
                dialog.dismiss();
                AlertDialog.init(this).setBackgroundColor(R.color.colorWhite).setCancelable(false)
                        .setMessage("Something went wrong please check your internet connection")
                        .setTitle("No Internet Connection").animate().setRadius(18).show();
            }
        }

    }

    private void instaDPApiRequest(String username) {
        RequestUtil.init(this).fetchInstaDP(AppConstants.BASE_URL + username + AppConstants.END_URL, new RequestUtil.InstaDpListener() {
            @Override
            public void onResponse(boolean isSuccessFull, InstaDpModel post) {
                if (isSuccessFull) {
                    dialog.dismiss();
                    InstaDpBottomSheet mBottomSheet = new InstaDpBottomSheet(() -> {
                        adMobUtils.showInterstitialAd((isLoaded, interstitial) -> {
                        });
                    });
                    Bundle bundle = new Bundle();
                    bundle.putString("dp", post.profileUrl);
                    bundle.putString("username", post.username);
                    bundle.putString("fullName", post.fullName);
                    mBottomSheet.setArguments(bundle);
                    mBottomSheet.show(getSupportFragmentManager(), mBottomSheet.getTag());
                } else {
                    AlertDialog.init(MainActivity.this).setBackgroundColor(R.color.colorWhite).setCancelable(false)
                            .setTitle("User Not Exist").animate().setRadius(18)
                            .setMessage("This user doesn't exist on instagram.please enter correct username")
                            .show();
                    dialog.dismiss();
                }
            }
        });
    }

    private void showBottomDialog(PostModel post) {
        DownloadDialog.newInstance(post).show(getSupportFragmentManager(), "download");
    }

    private void startForegroundService() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(this, ClipboardService.class));

            } else {
                startService(new Intent(this, ClipboardService.class));
            }

        }
    }

    private void getPrivateAccountInfo(String link) {
        if (link != null && !link.equals("")) {
            url = link.replaceAll("", "");
            if (SettingsFragment.reg.getBack(url, "(http(s)?:\\/\\/(.+?\\.)?[^\\s\\.]+\\.[^\\s\\/]{1,9}(\\/[^\\s]+)?)").isEmpty()) {
                Toast.makeText(this, "Check URL pasted, It seems to be wrong!", Toast.LENGTH_SHORT).show();
            } else {
                video = "";
                imagina = "";
                desc = "";
                videoArray = "";
                url = SettingsFragment.reg.getBack(url, "(http(s)?:\\/\\/(.+?\\.)?[^\\s\\.]+\\.[^\\s\\/]{1,9}(\\/[^\\s]+)?)");

                AsyncHttpClient client = new AsyncHttpClient();
                client.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36");
                client.get(url, new TextHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        dialog.show();
                    }

                    @Override
                    public void onFinish() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                        dialog.dismiss();
                        AlertDialog.init(MainActivity.this).setBackgroundColor(R.color.colorWhite).setCancelable(false)
                                .setMessage("Something went wrong please check your internet connection")
                                .setTitle("No Internet Connection").animate().setRadius(18).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                        html = responseBody;
                        Log.d("Responsess", responseBody);
                        for (Header header : headers) {
                            Log.e("header : ", header.getValue());
                        }
                        if (url.contains("instagram.com")) {
                            instagram();
                        }
                    }
                });
            }
        }
    }

    public void instagram() {
        video = SettingsFragment.reg.getBack(html, "property=\"og:video\" content=\"([^\"]+)\"");
        imagina = SettingsFragment.reg.getBack(html, "property=\"og:image\" content=\"([^\"]+)\"");
        desc = SettingsFragment.reg.getBack(html, "property=\"og:description\" content=\"([^\"]+)\"");
        showDialogPrivateAccount();
    }

    private void showDialogPrivateAccount() {
        if (!video.isEmpty() || !imagina.isEmpty() || !videoArray.isEmpty()) {
            FragmentManager fm = getSupportFragmentManager();
            PrivateUserAlert info = new PrivateUserAlert(() -> {

            });
            Bundle args = new Bundle();
            info.setArguments(args);
            info.show(fm, "fragment_info");
        } else {
            Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private void permissionBatteryOptimization() {
        int permissionCheck = ContextCompat
                .checkSelfPermission(this, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startActivity(new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse("package:" + getPackageName())));
    }

    @Override
    public void onDownloadFinish() {
        if (!Admob.isPremiumFeature())
            adMobUtils.showInterstitialAd((isLoaded, interstitial) -> {
            });
    }

}
