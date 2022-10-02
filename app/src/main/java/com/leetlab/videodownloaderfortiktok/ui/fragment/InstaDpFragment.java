package com.leetlab.videodownloaderfortiktok.ui.fragment;

import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.model.InstaDpModel;
import com.leetlab.videodownloaderfortiktok.utils.Admob;
import com.leetlab.videodownloaderfortiktok.utils.AppConstants;
import com.leetlab.videodownloaderfortiktok.utils.InternetConectionn;
import com.leetlab.videodownloaderfortiktok.utils.volley.RequestUtil;
import com.leetlab.admob.AdMobUtils;
import com.leetlab.utils.dialog.AlertDialog;
import com.leetlab.utils.dialog.ProgressDialog;
import com.leetlab.utils.dialog.ProgressType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.CLIPBOARD_SERVICE;

public class InstaDpFragment extends Fragment {

    @BindView(R.id.et_url)
    EditText etUrl;
    @BindView(R.id.pasteTxt)
    TextView pasteTxt;
    @BindView(R.id.urlLayout)
    ConstraintLayout urlLayout;
    @BindView(R.id.downloadBtn)
    Button downloadBtn;
    //    @BindView(R.id.adView)
//    AdView adView;
    @BindView(R.id.contentMain)
    ConstraintLayout contentMain;
    @BindView(R.id.nativeAdTemplate)
    TemplateView nativeAdTemplate;


    public InstaDpFragment() {
    }

    private ClipboardManager clipboard;
    private int type = 1;
    ProgressDialog dialog;
    AdMobUtils adMobUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_insta_dp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        dialog = new ProgressDialog(getActivity(), ProgressType.HORIZONTAL);
        dialog.setMessage("Resolving url...");
        dialog.setRadius(12);
        clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);

        adMobUtils = new AdMobUtils(getContext(), Admob.APP_ID);
        adMobUtils.initInterstitialAd(Admob.INTESTITIAL_ID);
        if (!Admob.isPremiumFeature())
            showNativeAd();
    }

    @OnClick({R.id.pasteTxt, R.id.downloadBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pasteTxt:
                if (clipboard.getText() != null)
                    etUrl.setText(clipboard.getText());
                break;
            case R.id.downloadBtn:
                instaDPUrlVerification();
                break;
        }
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
            if (InternetConectionn.CheckInternetConnection(getContext())) {
                instaDPApiRequest(etUrl.getText().toString().trim());
                dialog.show();
            } else {
                dialog.dismiss();
                AlertDialog.init(getActivity()).setBackgroundColor(R.color.colorWhite).setCancelable(false)
                        .setMessage("Something went wrong please check your internet connection")
                        .setTitle("No Internet Connection").animate().setRadius(18).show();
            }
        }

    }

    private void showNativeAd() {
        AdLoader.Builder builder = new AdLoader.Builder(getContext(), Admob.NATIVE_AD_ID);
        AdLoader loader = builder.forUnifiedNativeAd(unifiedNativeAd -> {
            nativeAdTemplate.setNativeAd(unifiedNativeAd);
        }).build();
        loader.loadAd(new AdRequest.Builder().build());
    }

    private void instaDPApiRequest(String username) {
        RequestUtil.init(getActivity()).fetchInstaDP(AppConstants.BASE_URL + username + AppConstants.END_URL, new RequestUtil.InstaDpListener() {
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
                    mBottomSheet.show(getFragmentManager(), mBottomSheet.getTag());
                } else {
                    AlertDialog.init(getActivity()).setBackgroundColor(R.color.colorWhite).setCancelable(false)
                            .setTitle("User Not Exist").animate().setRadius(18)
                            .setMessage("This user doesn't exist on instagram.please enter correct username")
                            .show();
                    dialog.dismiss();
                }
            }
        });
    }
}
