package com.leetlab.videodownloaderfortiktok.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.firebase.messaging.FirebaseMessaging;
import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.adapter.AccountsAdapter;
import com.leetlab.videodownloaderfortiktok.listener.DownloadDialogListener;
import com.leetlab.videodownloaderfortiktok.listener.PrivateAlertDialogListner;
import com.leetlab.videodownloaderfortiktok.model.InstaUserModel;
import com.leetlab.videodownloaderfortiktok.ui.fragment.InfoFragment;
import com.leetlab.videodownloaderfortiktok.ui.fragment.InstaDownloaderFragment;
import com.leetlab.videodownloaderfortiktok.ui.fragment.MainDownloadFragment;
import com.leetlab.videodownloaderfortiktok.ui.fragment.WebviewFragmnet;
import com.leetlab.videodownloaderfortiktok.utils.AccountDB;
import com.leetlab.videodownloaderfortiktok.utils.Admob;
import com.leetlab.videodownloaderfortiktok.utils.AppConstants;
import com.leetlab.videodownloaderfortiktok.utils.PaperUtil;
import com.leetlab.admob.AdMobUtils;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jackandphantom.circularimageview.RoundedImage;
import com.ornach.nobobutton.NoboButton;
import com.pd.chocobar.ChocoBar;
import com.squareup.picasso.Picasso;
import com.leetlab.utils.Contants.Constants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends AppCompatActivity implements PrivateAlertDialogListner, DownloadDialogListener {

    private static final int RC_LOGIN = 121;
    public static final int RC_Download = 122;

    @BindView(R.id.btn_removeAds)
    ImageView btnRemoveAds;
    @BindView(R.id.profile_img)
    RoundedImage profileImg;
    @BindView(R.id.profileLayout)
    ConstraintLayout profileLayout;
    @BindView(R.id.txtViewTitle)
    TextView txtViewTitle;
    @BindView(R.id.btnMenu)
    NoboButton btnMenu;
    @BindView(R.id.layoutHeader)
    ConstraintLayout layoutHeader;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.bottomNav)
    BottomNavigationView bottomNav;


    private final String INSTA_VIEW = "VideoView";
    private final String STORY_VIEW = "mp3view";
    private final String DP_VIEW = "exploreview";
    private final String DOWNLOAD_VIEW = "downloadsView";
    @BindView(R.id.downloadIndicator)
    TextView downloadIndicator;

    private InstaDownloaderFragment instaFragment = new InstaDownloaderFragment(true);
    private InstaDownloaderFragment DpFragment = new InstaDownloaderFragment(false);
    private WebviewFragmnet storyFragmnet = new WebviewFragmnet();
    private MainDownloadFragment downloadFragment = new MainDownloadFragment();
    private Fragment activeFragment = instaFragment;

    private BillingProcessor bp;
    private boolean readyToPurchase = false;

    AdMobUtils adMobUtils;

    public static String URL = "";

    NativeExpressAdView mAdView;
    VideoController mVideoController;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        FirebaseMessaging.getInstance().subscribeToTopic("All");

        if (PaperUtil.IsUnseenMediaAvalabile()) {
            downloadIndicator.setVisibility(View.VISIBLE);
            downloadIndicator.setText("" + PaperUtil.getUnseenMediaSize());
        } else {
            downloadIndicator.setVisibility(View.GONE);
        }

        adMobUtils = new AdMobUtils(getApplicationContext(), Admob.APP_ID);
        adMobUtils.initInterstitialAd(Admob.INTESTITIAL_ID);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if ("android.intent.action.SEND".equals(action) && "text/plain".equals(type)) {
            Log.e("TAG", "onCreate: " + intent.getStringExtra("android.intent.extra.TEXT"));
            URL = intent.getStringExtra("android.intent.extra.TEXT");
        }

        initBillingApi();

        get().add(R.id.mainContainer, downloadFragment).hide(downloadFragment).commit();
        get().add(R.id.mainContainer, DpFragment).hide(DpFragment).commit();
        get().add(R.id.mainContainer, storyFragmnet).hide(storyFragmnet).commit();
        get().add(R.id.mainContainer, instaFragment).commit();

        setupNavigationView();
        initAccount();
        updateHeader(INSTA_VIEW, getResources().getString(R.string.app_name));

        showRatingDialog();
    }

    private void showRatingDialog() {
        if (PaperUtil.isDialogAvailable()) {
            final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                    .threshold(1)
                    .session(1)
                    .onRatingBarFormSumbit(feedback -> {
                    }).build();

            ratingDialog.show();
        }
    }

    private void initBillingApi() {
        bp = new BillingProcessor(this, getResources().getString(R.string.LICENSE_KEY), getResources().getString(R.string.MERCHANT_ID), new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
                Admob.buyPremiumFeature();
                ChocoBar.builder().setActivity(Main2Activity.this).setActionText("Sure")
                        .setActionClickListener(v -> {
                        })
                        .setText(getResources().getString(R.string.Ads_remove))
                        .setDuration(ChocoBar.LENGTH_INDEFINITE)
                        .build()
                        .show();
            }

            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
                ChocoBar.builder().setActivity(Main2Activity.this).setActionText("Sure")
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

    }


    private FragmentTransaction get() {
        return getSupportFragmentManager().beginTransaction();
    }

    private void initAccount() {
        if (AccountDB.getAccounts().size() != 0) {
            InstaUserModel user = AccountDB.getCurrentAccount();
            Picasso.with(this).load(user.getImage_url()).into(profileImg);
        }
        profileLayout.setOnClickListener(v -> changeAccount());
    }

    private void changeAccount() {
        if (AccountDB.getAccounts().size() == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, RC_LOGIN);
            return;
        }
        Dialog dialog = new Dialog(this);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        View contentView = getLayoutInflater().inflate(R.layout.layout_add_account, null);
        RecyclerView listMenu = contentView.findViewById(R.id.listAccounts);

        AccountsAdapter adapter = new AccountsAdapter(this, AccountDB.getAccounts(), instaUserModel -> {
            dialog.dismiss();
            AccountDB.setCurrentAccount(instaUserModel);
            updateAccount();
        });
        listMenu.setLayoutManager(new LinearLayoutManager(this));
        listMenu.setAdapter(adapter);

        TextView txtViewCancel = contentView.findViewById(R.id.txtViewCancel);
        txtViewCancel.setOnClickListener(v -> dialog.dismiss());
        TextView txtViewAddAccount = contentView.findViewById(R.id.txtViewAddAccount);
        txtViewAddAccount.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, RC_LOGIN);
        });
        dialog.setContentView(contentView);
        dialog.show();


    }

    private void updateAccount() {
        InstaUserModel user = AccountDB.getCurrentAccount();
        Picasso.with(this).load(user.getImage_url()).into(profileImg);
        if (activeFragment == instaFragment) {
            instaFragment.check_url(null);
        }
    }

    private void setupNavigationView() {
        bottomNav.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_photos_videos:
                    get().hide(activeFragment).show(instaFragment).commit();
                    activeFragment = instaFragment;
                    //storyFragment.notifyUpdate();
                    updateHeader(INSTA_VIEW, "Video Download");
                    break;
                case R.id.menu_insta_story:
                    get().hide(activeFragment).show(storyFragmnet).commit();
                    activeFragment = storyFragmnet;

                    updateHeader(STORY_VIEW, "Explore");
                    break;
                case R.id.menu_dp_saver:
                    get().hide(activeFragment).show(DpFragment).commit();
                    activeFragment = DpFragment;
                    //downloadFragment.initAdapter();
                    updateHeader(DP_VIEW, "Mp3 Download");
                    break;
                case R.id.menu_downloads:
                    get().hide(activeFragment).show(downloadFragment).commit();
                    activeFragment = downloadFragment;
                    downloadFragment.initAdapter();
                    updateHeader(DOWNLOAD_VIEW, "Downloads");
                    PaperUtil.setUnseenMediaSize(0);
                    updateIndicator();
                    break;
            }
            return true;
        });
    }

    private void updateHeader(String view, String title) {
        if (view.equals(DOWNLOAD_VIEW)) {
            profileLayout.setVisibility(View.GONE);
        } else {
            profileLayout.setVisibility(View.VISIBLE);
        }
        profileLayout.setVisibility(View.GONE);
        txtViewTitle.setText(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LOGIN && resultCode == RESULT_OK) {
            updateAccount();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @OnClick({R.id.btn_removeAds, R.id.profileLayout, R.id.btnMenu, R.id.btnInstagram})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_removeAds:
                if (!BillingProcessor.isIabServiceAvailable(this)) {
                    ChocoBar.builder().setActivity(Main2Activity.this).setActionText("Sure")
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
            case R.id.profileLayout:
                break;
            case R.id.btnMenu:
                InfoFragment infoFragment = new InfoFragment();
                infoFragment.show(getSupportFragmentManager(), "dialog");
                break;
            case R.id.btnInstagram:

                final String appPackageName = "com.zhiliaoapp.musically";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" +
                                    appPackageName)));
                }

                break;
        }
    }


    @Override
    public void OnSignIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, RC_LOGIN);
    }

    @Override
    public void OnDismiss() {

    }

    @Override
    public void onDownloadFinish() {
        AppConstants.clearClipBoard(this);
        get().hide(activeFragment).show(instaFragment).commit();
        activeFragment = instaFragment;
        updateHeader(INSTA_VIEW, getResources().getString(R.string.app_name));
        bottomNav.setSelectedItemId(R.id.menu_photos_videos);
        if (!Admob.isPremiumFeature()) {
            adMobUtils.showInterstitialAd((isLoaded, interstitial) -> {
                if (isLoaded) {
                    Constants.STATE = false;
                }
            });
        }
    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        instaFragment.initAutoDwonaloder();
        if (PaperUtil.isAutoDownloadStateActive()) {
            instaFragment.initAutoDwonaloder();
            PaperUtil.setAutoDownloadState(false);
        }

        if (PaperUtil.IsUnseenMediaAvalabile()) {
            downloadIndicator.setVisibility(View.VISIBLE);
            downloadIndicator.setText("" + PaperUtil.getUnseenMediaSize());
        } else {
            downloadIndicator.setVisibility(View.GONE);
        }
    }


    public void updateIndicator() {
        if (PaperUtil.IsUnseenMediaAvalabile()) {
            downloadIndicator.setVisibility(View.VISIBLE);
            downloadIndicator.setText("" + PaperUtil.getUnseenMediaSize());
        } else {
            downloadIndicator.setVisibility(View.GONE);
        }
    }

}
