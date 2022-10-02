package com.leetlab.videodownloaderfortiktok.ui.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.listener.DownloadDialogListener;
import com.leetlab.videodownloaderfortiktok.ui.fragment.DownloadDialog;
import com.leetlab.videodownloaderfortiktok.utils.InternetConectionn;
import com.leetlab.videodownloaderfortiktok.utils.SharedPrefManager;
import com.leetlab.videodownloaderfortiktok.utils.UIUtil;
import com.leetlab.videodownloaderfortiktok.utils.volley.RequestUtil;
import com.leetlab.utils.dialog.ProgressDialog;
import com.leetlab.utils.dialog.ProgressType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadMediaActivity extends AppCompatActivity implements DownloadDialogListener {

    private static String MEDIA_TYPE = "MediaType";
    @BindView(R.id.txtViewTitle)
    TextView txtViewTitle;
    @BindView(R.id.et_url)
    EditText etUrl;

    private ClipboardManager clipboard;
    private int type = 0;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_media);
        ButterKnife.bind(this);

        type = getIntent().getIntExtra(MEDIA_TYPE, 1);
        if (type == 1) {
            txtViewTitle.setText("Photos and Videos");
        } else {
            txtViewTitle.setText("DP Saver");
        }

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        dialog = new ProgressDialog(this, ProgressType.HORIZONTAL)
                .setMessage("Resolving url...")
                .setRadius(8);

    }

    @OnClick({R.id.btnBack, R.id.pasteTxt, R.id.downloadBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.pasteTxt:
                if (clipboard != null) {
                    if (clipboard.getPrimaryClip().getItemCount() > 0) {
                        String url = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
                        etUrl.setText(url);
                    }
                }
                break;
            case R.id.downloadBtn:
                if (!InternetConectionn.CheckInternetConnection(this)) {
                    UIUtil.popError(this);
                    return;
                }

                if (type == 1) {
                    if (!etUrl.getText().toString().isEmpty()) {
                        if (UIUtil.isValid(etUrl.getText().toString())) {
                            String link = etUrl.getText().toString();
                            resolveUrl(UIUtil.getUrl(link));
                        }
                    }
                }
        }
    }

    private void resolveUrl(String url) {
        dialog.show();
        RequestUtil.init(this).fetchPost(url, (isSuccessFull, post) -> {
            if (!isSuccessFull) {
                Toast.makeText(this, "Account is Private", Toast.LENGTH_SHORT).show();
                if (SharedPrefManager.getInstance(this).isLoggedIn()) {
                    UIUtil.popMessage(this);
                    return;
                }
//                getPrivateAccountInfo(etUrl.getText().toString());
                dialog.setMessage("Accessing Private Account");
                new Handler().postDelayed(() -> dialog.dismiss(), 2000);
                return;
            }
            dialog.dismiss();
            DownloadDialog.newInstance(post).show(getSupportFragmentManager(), "download");
        });
    }

    public static void run(Context context, int type) {
        Intent intent = new Intent(context, DownloadMediaActivity.class);
        intent.putExtra(MEDIA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    public void onDownloadFinish() {
        Toast.makeText(this, "Ad Load", Toast.LENGTH_SHORT).show();
//        if (!Admob.isPremiumFeature())
//            adMobUtils.showInterstitialAd((isLoaded, interstitial) -> {
//            });
    }
}
